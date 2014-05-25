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
package vellum.mail;

import java.io.IOException;
import vellum.data.Emails;
import vellum.jx.JMap;
import vellum.jx.JMapException;
import vellum.util.Args;
import vellum.util.Streams;

/**
 *
 * @author evan.summers
 */
public class MailerProperties {

    byte[] logoBytes;
    String organisation;
    String from;
    String username;
    String password;
    String host = "localhost";
    int port = 25;
    boolean enabled = false;

    public MailerProperties() {
    }
    
    public void init(JMap properties) throws JMapException, IOException {
        enabled = properties.getBoolean("enabled", true);
        host = properties.getString("host", host);
        port = properties.getInt("port", port);
        username = properties.getString("username", null);
        password = properties.getString("password", null);
        from = properties.getString("from");
        organisation = properties.getString("organisation", null);
        if (organisation == null) {
            organisation = Emails.getDomain(from);
        }
        String logoImagePath = properties.getString("logo", null);
        if (logoImagePath != null) {
            logoBytes = Streams.readBytes(logoImagePath);
        }
    }
    
    public void init(byte[] logoBytes, String organisation, String from) {
        this.logoBytes = logoBytes;
        this.organisation = organisation;
        this.from = from;
        this.enabled = true;
    }

    public void setLogoBytes(byte[] logoBytes) {
        this.logoBytes = logoBytes;
    }
    
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    public byte[] getLogoBytes() {
        return logoBytes;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getFrom() {
        return from;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }       

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return Args.format(organisation, from, logoBytes.length);
    }
    
    
}