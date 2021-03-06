/*
 Source https://github.com/evanx by @evanxsummers
`
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. The ASF licenses this file to
 you under the Apache License, Version 2.0 (the "License").
 You may not use this file except in compliance with the
 License. You may obtain a copy of the License at:

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.  
 */
package vellum.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.data.Millis;
import vellum.exception.DisplayException;
import vellum.exception.DisplayMessage;
import vellum.jx.JMap;
import vellum.jx.JMapException;
import vellum.jx.JMapsException;
import vellum.jx.JMapFormatter;
import vellum.jx.JMaps;
import vellum.parameter.Entry;
import vellum.parameter.Parameters;
import vellum.storage.StorageException;
import vellum.util.Lists;
import vellum.util.Streams;
import vellum.util.Strings;

/**
 *
 * @author evan.summers
 */
public class Httpx {

    private static final Logger logger = LoggerFactory.getLogger(Httpx.class);
    HttpExchange delegate;
    PrintStream out;
    JMap parameterMap;
    JMap cookieMap;
    JMap dataMap;
    JMap headerMap;
    String urlQuery;
    String requestBody;
    String[] args;

    public Httpx(HttpExchange httpExchange) {
        this.delegate = httpExchange;
    }

    public HttpExchange getDelegate() {
        return delegate;
    }
    
    public String getHostUrl() {
        return "https://" + getRequestHeader("Host");
    }

    public String getReferer() {
        return getRequestHeader("Referer");
    }
    
    public boolean isLocalhost() throws UnknownHostException {
        return delegate.getRemoteAddress().getAddress().getHostAddress().equals("127.0.0.1");        
    }
    
    public String getRemoteHostName() {
        return delegate.getRemoteAddress().getHostName();
    }

    public String getRemoteHostAddress() {
        return delegate.getRemoteAddress().getAddress().getHostAddress();
    }
    
    public String getQuery() {
        return delegate.getRequestURI().getQuery();
    }

    public String getPath() {
        return delegate.getRequestURI().getPath();
    }

    public String[] getPathArgs() {
        if (args == null) {
            args = delegate.getRequestURI().getPath().substring(1).split("/");
        }
        return args;
    }

    public String getLastPathArg() {
        String path = delegate.getRequestURI().getPath();
        int index = path.lastIndexOf("/");
        if (index > 0) {
            return path.substring(index + 1);
        }
        throw new IllegalArgumentException(path);
    }

    public int getPathLength() {
        return getPathArgs().length;
    }

    public JMap getParameterMap() throws IOException {
        if (parameterMap == null) {
            parseParameterMap();
        }
        return parameterMap;
    }

    private void parseParameterMap() throws IOException {
        parameterMap = new JMap();
        urlQuery = delegate.getRequestURI().getQuery();
        if (delegate.getRequestMethod().equals("POST")) {
            urlQuery = readString();
        }
        if (urlQuery == null) {
            return;
        }
        int index = 0;
        while (index < urlQuery.length()) {
            int endIndex = urlQuery.indexOf("&", index);
            if (endIndex > 0) {
                put(urlQuery.substring(index, endIndex));
                index = endIndex + 1;
            } else if (index < urlQuery.length()) {
                put(urlQuery.substring(index));
                return;
            }
        }
    }

    private void put(String string) {
        Entry<String, String> entry = Parameters.parseEntry(string);
        if (entry != null) {
            String value = Strings.decodeUrl(entry.getValue());
            parameterMap.put(entry.getKey(), value);
        }
    }

    public void clearCookie(Collection<String> keys) {
        for (String key : keys) {
            delegate.getResponseHeaders().add("Set-cookie",
                    String.format("%s=; Expires=Thu, 01 Jan 1970 00:00:00 GMT", key));
        }
    }

    public void setCookie(JMap map, long ageMillis) throws JMapsException {
        setCookie(map, null, null, ageMillis);
    }
    
    public void setCookie(JMap map, String path, String version, long ageMillis) throws JMapsException {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            StringBuilder builder = new StringBuilder();
            if (value == null) value = "";
            builder.append(String.format("%s=%s; Max-age=%d", key, value, ageMillis / 1000));
            if (path != null) {
                builder.append("; Path=").append(path);
            }
            if (version != null) {
                builder.append("; Version=").append(version);
            }
            delegate.getResponseHeaders().add("Set-cookie", builder.toString());
        }
    }

    public JMap getCookieMap() {
        if (cookieMap == null) {
            parseCookieMap(parseFirstRequestHeader("Cookie"));
        }
        return cookieMap;
    }

    private void parseCookieMap(List<String> cookies) {
        cookieMap = new JMap();
        if (cookies != null) {
            for (String cookie : cookies) {
                logger.trace("parseCookieMap cookie {}", cookie);
                int index = cookie.indexOf("=");
                if (index > 0) {
                    String key = cookie.substring(0, index);
                    String value = cookie.substring(index + 1);
                    cookieMap.put(key, value);
                }
            }
        }
    }

    public List<String> parseFirstRequestHeader(String key) {
        logger.trace("parseFirstRequestHeader {}", key);
        String text = delegate.getRequestHeaders().getFirst(key);
        if (text != null) {
            List<String> list = new ArrayList();
            for (String string : text.split(";")) {
                list.add(string.trim());
            }
            return list;
        }
        return null;
    }

    public Map<String, String> parseFirstRequestHeaders() {
        Map<String, String> map = new HashMap();
        for (String key : delegate.getRequestHeaders().keySet()) {
            String valueString = delegate.getRequestHeaders().getFirst(key);
            logger.trace("first request header: {}: {}", key, valueString);
            map.put(key, valueString);
        }
        return map;
    }
    
    public Headers getRequestHeaders() {
        return delegate.getRequestHeaders();
    }    
    
    public String getPathString(int index) {
        return getPathString(index, null);
    }

    public String getPathString(int index, String defaultValue) {
        String[] args = getPathArgs();
        if (args.length > index) {
            return args[index];
        }
        return defaultValue;
    }

    public void parsePathParameters(Map pathParameterMap, int fromIndex) {
        List<String> args = Lists.subList(getPathArgs(), fromIndex);
        for (String arg : args) {
            int index = arg.indexOf('=');
            if (index < 1) {
                throw new IllegalArgumentException(arg);
            } else {
                Parameters.put(pathParameterMap, arg);
            }
        }
    }

    public void setResponseHeader(String key, String value) throws IOException {
        delegate.getResponseHeaders().set(key, value);
    }

    public String getRequestHeader(String key) {
        return delegate.getRequestHeaders().getFirst(key);
    }

    public List<String> listRequestHeaders(String key) {
        return delegate.getRequestHeaders().get(key);
    }

    public Collection<String> listRequestHeaders() {
        return delegate.getRequestHeaders().keySet();
    }
    

    public void setCors(int cacheSeconds) {
        delegate.getResponseHeaders().set("Access-control-allow-headers", "if-modified-since");
        delegate.getResponseHeaders().set("Access-control-allow-origin", "*");
        delegate.getResponseHeaders().set("Cache-control", "max-age=" + cacheSeconds);
    }
    
    public void sendResponse(String contentType, String string) throws IOException {
        logger.trace("sendResponse {} string [{}]", contentType, string.trim());
        sendResponse(contentType, string.getBytes());
    }

    public void sendResponseHeaders(String contentType, int length) throws IOException {
        delegate.getResponseHeaders().set("Content-type", contentType);
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }
    
    public void sendResponse(String contentType, byte[] bytes) throws IOException {
        delegate.getResponseHeaders().set("Content-type", contentType);
        delegate.getResponseHeaders().set("Content-length", Integer.toString(bytes.length));
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
        getPrintStream().write(bytes);
    }

    public void sendResponse(int statusCode, String contentType, byte[] bytes) throws IOException {
        delegate.getResponseHeaders().set("Content-type", contentType);
        delegate.getResponseHeaders().set("Content-length", Integer.toString(bytes.length));
        delegate.sendResponseHeaders(statusCode, bytes.length);
        getPrintStream().write(bytes);
    }
    
    public void sendResponseFile(String contentType, String fileName) throws IOException {
        delegate.getResponseHeaders().add("Content-disposition",
                "attachment; filename=" + fileName);
        delegate.getResponseHeaders().set("Content-type", contentType);
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }

    public void sendEmptyOkResponse() throws IOException {
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }

    public void sendPlainError(String responseString) {
        try {
            byte[] responseBytes = responseString.getBytes();
            delegate.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,
                    responseBytes.length);
            delegate.getResponseHeaders().set("Content-type", "text/plain");
            delegate.getResponseBody().write(responseBytes);
        } catch (IOException e) {
            logger.warn("sendPlainError", e);
        }
    }
    
    public void sendPlainResponse(String responseString) 
            throws IOException {
        if (!responseString.endsWith("\n")) {
            responseString += "\n";
        }
        byte[] responseBytes = responseString.getBytes();
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK,
                responseBytes.length);
        delegate.getResponseHeaders().set("Content-type", "text/plain");
        delegate.getResponseBody().write(responseBytes);
    }
    
    public void sendError(Throwable error) {
        try {
            if (error instanceof JMapException) {
                JMapException mapException = (JMapException) error;
                JMap responseMap = mapException.getMap();
                responseMap.put("errorMessage", error.getMessage());
                sendResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, "text/json",
                        JMapFormatter.formatMap(responseMap).getBytes());
                return;
            }
            if (error instanceof DisplayException) {
            } else if (error instanceof StorageException) {
            } else {
                error.printStackTrace(System.err);
            }
            sendError(error.getMessage());
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public void sendError(DisplayMessage message) {
        sendError(message.getDisplayMessage());
    }

    public void sendError(String messageFormat, Object ... args) {
        try {
            logger.warn(String.format(messageFormat, args), parameterMap);
            sendResponse(JMaps.mapValue("errorMessage", String.format(messageFormat, args)));
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public OutputStream getOutputStream() {
        return delegate.getResponseBody();
    }
    
    public PrintStream getPrintStream() {
        if (out == null) {
            out = new PrintStream(delegate.getResponseBody());
        }
        return out;
    }

    public void sendResponse(JMap map) throws IOException {
        sendResponse("text/json", map.toJson());
    }

    public JMap parseJsonMap() throws IOException {
        String string = readString();
        logger.info("parseJsonMap {}", string);
        return JMaps.parseMap(string);
    }

    public void close() {
        delegate.close();
    }

    public String readString() throws IOException {
        return Streams.readString(delegate.getRequestBody());
    }

    public InputStream getInputStream() throws IOException {
        return delegate.getRequestBody();
    }
    
    public SSLSession getSSLSession() {
        return ((HttpsExchange) delegate).getSSLSession();
    }

    public X509Certificate getPeerCertficate() throws SSLPeerUnverifiedException {
        return ((HttpsExchange) delegate).getSSLSession().getPeerCertificateChain()[0];        
    }

}
