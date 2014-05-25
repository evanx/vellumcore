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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author evan.summers
 */
public class MimeTypes {
    public static final Map<String, String> mimeTypes = map();

    private static Map map() {
        Map<String, String> map = new HashMap();
        map.put("json", "text/json");
        map.put("txt", "text/plain");
        map.put("html", "text/html");
        map.put("ico", "image/x-icon");
        map.put("png", "image/png");
        map.put("jpg", "image/jpeg");
        map.put("jpeg", "image/jpeg");
        map.put("css", "text/css");
        map.put("js", "text/javascript");
        map.put("sh", "text/x-shellscript");
        map.put("woff", "application/font-woff");
        map.put("svg", "image/svg+xml");
        map.put("otf", "application/font-sfnt");
        map.put("ttf", "application/font-sfnt");
        map.put("eot", "application/vnd.ms-fontobject");
        return Collections.unmodifiableMap(map);
    }
    
    public static String getContentType(String path, String defaultContentType) {
        int index = path.lastIndexOf(".");
        if (index > 0) {
            String ext = path.substring(index + 1);
            String type = MimeTypes.mimeTypes.get(ext);
            if (type != null) {
                return type;
            }
        }
        return defaultContentType;
    }
}
