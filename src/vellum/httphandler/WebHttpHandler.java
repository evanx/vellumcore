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
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.util.Streams;

/**
 *
 * @author evan.summers
 */
public class WebHttpHandler implements HttpHandler {

    Logger logger = LoggerFactory.getLogger(WebHttpHandler.class);
    Map<String, String> mimeTypes = new HashMap();
    Map<String, byte[]> cache = new HashMap();
    String webPath;
    
    public WebHttpHandler(String webPath) {
        this.webPath = webPath;
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("html", "text/html");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "text/javascript");
        mimeTypes.put("sh", "text/x-shellscript");
        mimeTypes.put("woff", "application/font-woff");
        mimeTypes.put("pem", "application/x-pem-file");
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String contentType = null;
        int index = path.lastIndexOf(".");
        if (index > 0) {
            contentType = mimeTypes.get(path.substring(index + 1));
        }
        if (contentType == null) {
            contentType = "text/html";
            path = "app.html";
        }
        try {
            httpExchange.getResponseHeaders().set("Content-type", contentType);
            byte[] bytes = cache.get(path);
            if (bytes == null) {
                String resourcePath = webPath + '/' + path;
                if (path.startsWith("/")) {
                    resourcePath = webPath + path;
                } 
                logger.trace("get {}", resourcePath);
                InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
                bytes = Streams.readBytes(resourceStream);
                cache.put(path, bytes);
            }
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            httpExchange.getResponseBody().write(bytes);
            logger.trace("path", path, bytes.length);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        } finally {
            httpExchange.close();
        }
        logger.info("handle {} [{}]", path, contentType);
    }
}
