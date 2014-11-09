/*
 Source https://github.com/evanx by @evanxsummers

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

import vellum.jx.JMap;
import vellum.util.Args;

/**
 *
 * @author evan.summers
 */
public class HttpsServerProperties {
    int port;
    boolean enabled = true;
    boolean clientAuth = false;

    public HttpsServerProperties(JMap properties) {
        this(properties.getInt("port", 8443),
                properties.getBoolean("clientAuth", false),
                properties.getBoolean("enabled", true));
    }
    
    public HttpsServerProperties(int port) {
        this.port = port;
    }

    public HttpsServerProperties(int port, boolean clientAuth, boolean enabled) {
        this.port = port;
        this.clientAuth = clientAuth;
        this.enabled = enabled;
    }
    
    public int getPort() {
        return port;
    }

    public boolean isClientAuth() {
        return clientAuth;
    }
    
    public boolean isEnabled() {
        return enabled;
    }  

    @Override
    public String toString() {
        return Args.format(port, clientAuth, enabled);
    }
    
    
}
