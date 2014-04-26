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
package vellum.httpserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.lifecycle.Shutdownable;
import vellum.util.ExtendedProperties;

/**
 *
 * @author evan.summers
 */
public class VellumHttpServer implements Shutdownable {
    private Logger logger = LoggerFactory.getLogger(VellumHttpServer.class);
    HttpServer delegate;
    HttpServerProperties properties; 
    ThreadPoolExecutor executor = 
            new ThreadPoolExecutor(16, 96, 0, TimeUnit.MILLISECONDS, 
            new ArrayBlockingQueue<Runnable>(32));
    
    public VellumHttpServer() {
    }

    public void start(ExtendedProperties properties, HttpHandler httpHandler) 
            throws Exception {
        start(new HttpServerProperties(properties), httpHandler);
    }
    
    public void start(HttpServerProperties properties, HttpHandler httpHandler) 
            throws Exception {
        InetSocketAddress socketAddress = new InetSocketAddress(properties.getPort());
        delegate = HttpServer.create(socketAddress, 4);
        delegate.setExecutor(executor);
        delegate.createContext("/", httpHandler);
        delegate.start();
        logger.info("start", properties.getPort());
    }

    public void createContext(String contextName, HttpHandler httpHandler) {
        delegate.createContext(contextName, httpHandler);
    }

    @Override
    public void shutdown() {
        if (delegate != null) {
            delegate.stop(0); 
            executor.shutdown();
        }  
    }
}
