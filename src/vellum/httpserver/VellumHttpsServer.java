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
import com.sun.net.httpserver.HttpsServer;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.lifecycle.Shutdownable;
import vellum.security.HttpsConfiguratorFactory;
import vellum.ssl.SSLContexts;
import vellum.util.ExtendedProperties;

/**
 *
 * @author evan.summers
 */
public class VellumHttpsServer implements Shutdownable, RejectedExecutionHandler {

    Logger logger = LoggerFactory.getLogger(VellumHttpsServer.class);
    SSLContext sslContext;
    HttpsServer httpsServer;
    ExtendedProperties properties; 
    ThreadPoolExecutor executor;
    String name; 
    
    public VellumHttpsServer() {
    }

    public void start(ExtendedProperties properties, X509TrustManager trustManager,
            HttpHandler handler) throws Exception {
        start(new HttpsServerProperties(properties), 
                SSLContexts.create(properties, trustManager), handler);
    }

    public void start(HttpsServerProperties properties, SSLContext sslContext,
            HttpHandler handler) throws Exception {
        executor = new ThreadPoolExecutor(100, 200, 60, TimeUnit.SECONDS, 
            new ArrayBlockingQueue<Runnable>(10));
        name = handler.getClass().getSimpleName();
        executor.setRejectedExecutionHandler(this);        
        InetSocketAddress socketAddress = new InetSocketAddress(properties.getPort());
        httpsServer = HttpsServer.create(socketAddress, 16);
        httpsServer.setHttpsConfigurator(HttpsConfiguratorFactory.
                createHttpsConfigurator(sslContext, properties.isClientAuth()));
        httpsServer.setExecutor(executor);
        httpsServer.createContext("/", handler);
        httpsServer.start();
        logger.info("init {}", properties);
    }

    public void createContext(String contextName, HttpHandler httpHandler) {
        httpsServer.createContext(contextName, httpHandler);
    }

    @Override
    public void shutdown() {
        if (httpsServer != null) {
            httpsServer.stop(0);
            executor.shutdown();
        }  
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.error("rejectedExecution {}", name);
    }
    
    @Override
    public String toString() {
        return executor.toString();
    }
        
}
