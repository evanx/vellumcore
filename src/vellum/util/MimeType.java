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
package vellum.util;

/**
 *
 * @author evan.summers
 */
public enum MimeType {
        JSON("text/json"),
        TXT("text/plain"),
        HTML("text/html"),
        ICO("image/x-icon"),
        PNG("image/png"),
        JPG("image/jpeg"),
        JPEG("image/jpeg"),
        CSS("text/css"),
        JS("text/javascript"),
        SH("text/x-shellscript"),
        WOFF("application/font-woff"),
        SVG("image/svg+xml"),
        OTF("application/font-sfnt"),
        TTF("application/font-sfnt"),
        EOT("application/vnd.ms-fontobject");

    String mimeType;
        
    private MimeType(String mimeType) {
        this.mimeType = mimeType;                
    }        
        
}
