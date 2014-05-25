/*
 * Source https://github.com/evanx by @evanxsummers

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
import java.net.HttpURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class RedirectPortHttpHandler implements HttpHandler {
    
    Logger logger = LoggerFactory.getLogger(RedirectPortHttpHandler.class);
    int redirectPort;
            
    public RedirectPortHttpHandler(int redirectPort) {
        this.redirectPort = redirectPort;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String hostAddress = httpExchange.getRequestHeaders().getFirst("Host");
        logger.info("host {}", hostAddress);
        String redirectUrl = String.format("https://%s:%d", hostAddress, redirectPort);
        logger.info("redirect {}", hostAddress, redirectUrl);
        httpExchange.getResponseHeaders().add("Location", redirectUrl);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_PERM, -1);
        httpExchange.close();
    }
}
