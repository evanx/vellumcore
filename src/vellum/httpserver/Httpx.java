/*
 * Source https://code.google.com/p/vellum by @evanxsummers
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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.data.Maps;
import vellum.exception.DisplayException;
import vellum.exception.DisplayMessage;
import vellum.jx.JMap;
import vellum.jx.JMapException;
import vellum.jx.JMaps;
import vellum.parameter.Entry;
import vellum.parameter.StringMap;
import vellum.parameter.Parameters;
import vellum.util.JsonStrings;
import vellum.util.Lists;
import vellum.util.Streams;
import vellum.util.Strings;

/**
 *
 * @author evan.summers
 */
public class Httpx {

    private static Logger logger = LoggerFactory.getLogger(Httpx.class);
    HttpExchange delegate;
    PrintStream out;
    JMap parameterMap;
    JMap cookieMap;
    JMap dataMap;
    String urlQuery;
    String requestBody;
    String[] args;
    boolean headersParsed = false;
    boolean acceptGzip = false;
    boolean agentWget = false;

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

    public void setCookie(JMap map, long ageMillis) throws JMapException {
        Object path = map.getString("path", null);
        String version = map.getString("version", null);
        for (String key : map.keySet()) {
            Object value = map.get(key);
            StringBuilder builder = new StringBuilder();
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

    public void parseHeaders() {
        headersParsed = true;
        for (String key : delegate.getRequestHeaders().keySet()) {
            List<String> values = delegate.getRequestHeaders().get(key);
            logger.trace("parseHeaders {} {}", key, values);
            if (key.equals("Accept-encoding")) {
                if (values.contains("gzip")) {
                    acceptGzip = true;
                }
            } else if (key.equals("User-agent")) {
                for (String value : values) {
                    if (value.toLowerCase().contains("wget")) {
                        agentWget = true;
                    }
                }
            }
        }
    }

    public boolean isAgentWget() {
        if (!headersParsed) {
            parseHeaders();
        }
        return agentWget;
    }

    public boolean isAcceptGzip() {
        if (!headersParsed) {
            parseHeaders();
        }
        return acceptGzip;
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

    public void sendResponse(String contentType, byte[] bytes) throws IOException {
        delegate.getResponseHeaders().set("Content-type", contentType);
        delegate.getResponseHeaders().set("Content-length",
                Integer.toString(bytes.length));
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        getPrintStream().write(bytes);
    }

    public void sendResponseFile(String contentType, String fileName) throws IOException {
        delegate.getResponseHeaders().add("Content-disposition",
                "attachment; filename=" + fileName);
        delegate.getResponseHeaders().set("Content-type", contentType);
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }

    public void sendResponse(String contentType, boolean ok) throws IOException {
        delegate.getResponseHeaders().set("Content-type", contentType);
        if (ok) {
            delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            delegate.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        }
    }

    public void sendEmptyOkResponse() throws IOException {
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }

    public void sendPlainResponse(String responseString, Object ... args) 
            throws IOException {
        responseString = String.format(responseString, args) + "\n";
        byte[] responseBytes = responseString.getBytes();
        delegate.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,
                responseBytes.length);
        delegate.getResponseHeaders().set("Content-type", "text/plain");
        delegate.getResponseBody().write(responseBytes);
        delegate.close();
    }
    
    public void sendError(Exception e) {
        if (e instanceof DisplayException) {
        } else {
            e.printStackTrace(System.err);
        }
        sendError(e.getMessage());
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

    public PrintStream getPrintStream() {
        if (out == null) {
            out = new PrintStream(delegate.getResponseBody());
        }
        return out;
    }

    public void sendResponse(StringMap map) throws IOException {
        sendResponse("text/json", true);
        getPrintStream().println(JsonStrings.buildJson(map));
    }

    public void sendResponse(JMap map) throws IOException {
        logger.trace("sendResponse {}", map);
        if (map.getText() != null) {
            sendResponse("text/plain", map.getText().getBytes());
        } else {
            sendResponse("text/json", map.toString().getBytes());
        }
    }

    public JMap parseJsonMap() throws IOException {
        return JMaps.parse(readString());
    }

    public void close() {
        delegate.close();
    }

    public String readString() throws IOException {
        return Streams.readString(delegate.getRequestBody());
    }

    public SSLSession getSSLSession() {
        return ((HttpsExchange) delegate).getSSLSession();
    }

    public X509Certificate getPeerCertficate() throws SSLPeerUnverifiedException {
        return ((HttpsExchange) delegate).getSSLSession().getPeerCertificateChain()[0];        
    }
}
