/*
 Source https://code.google.com/p/vellum by @evanxsummers

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
package vellum.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.jx.JMap;
import vellum.util.MimeTypes;
import vellum.util.Streams;

/**
 *
 * @author evan.summers
 */
public class WebHttpHandler implements HttpHandler {
    Logger logger = LoggerFactory.getLogger(WebHttpHandler.class);
    Map<String, byte[]> cache = new HashMap();
    String appPackage;
    String appDir;
    String defaultPath = "index.html";
    boolean caching = false;
    
    public WebHttpHandler(String appPackage, JMap properties) {
        this.appPackage = appPackage;
        this.caching = properties.getBoolean("caching", false);
        this.appDir = properties.getString("appDir", null);
        this.defaultPath = properties.getString("defaultPath", defaultPath);
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String contentType = null;
        int index = path.lastIndexOf(".");
        if (index > 0) {
            contentType = MimeTypes.mimeTypes.get(path.substring(index + 1));
        }
        if (contentType == null) {
            contentType = "text/html";
            path = defaultPath;
        }
        logger.info("handle {} {}", path, contentType);
        try {
            httpExchange.getResponseHeaders().set("Content-type", contentType);
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            byte[] bytes = get(path);
            httpExchange.getResponseBody().write(bytes);
            logger.trace("path", path, bytes.length);
        } catch (IOException e) {
            logger.warn("handle {} {}", path, e.getMessage());
        } catch (Throwable t) {
            logger.warn("handle: " + path, t);
        } finally {
            httpExchange.close();
        }
    }

    private byte[] get(String path) throws IOException {
        if (caching) {
            byte[] bytes = cache.get(path);
            if (bytes != null) {
                return bytes;
            }
            bytes = getBytes(path);
            cache.put(path, bytes);
            return bytes;
        }             
        return getBytes(path);
    }
    
    private byte[] getBytes(String path) throws IOException {
        File file = new File(appDir, path);
        if (file.exists()) {
            byte[] bytes = Streams.readBytes(file);
            return bytes;
        }
        String resourcePath = appPackage + '/' + path;
        if (path.startsWith("/")) {
            resourcePath = appPackage + path;
        }
        logger.trace("get {}", resourcePath);
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
        return Streams.readBytes(resourceStream);
    }
    
}
