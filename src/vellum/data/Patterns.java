/*
 * Source https://github.com/evanx by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
 */
package vellum.data;

import java.util.regex.Pattern;

/**
 *
 * @author evan.summers
 */
public class Patterns {
    public static final Pattern TAG = 
            Pattern.compile(".*<\\w*>.*");
    
    private static final String USERNAME_CONTENT = 
            "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*";
    private static final String DOMAIN_CONTENT = 
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    
    public static Pattern USERNAME = 
            Pattern.compile(String.format("^%s$", USERNAME_CONTENT));
    public static Pattern DOMAIN = 
            Pattern.compile(String.format("^%s$", DOMAIN_CONTENT));
    public static Pattern EMAIL = 
            Pattern.compile(String.format("^%s@%s$", 
            USERNAME_CONTENT, DOMAIN_CONTENT));
    public static Pattern INTEGER = 
            Pattern.compile(String.format("^[0-9]+$", DOMAIN_CONTENT));

    public static boolean matchesUserName(String string) {
        return USERNAME.matcher(string).matches();
    }
    
    public static boolean matchesDomain(String string) {
        return DOMAIN.matcher(string).matches();
    }
    
    public static boolean matchesEmail(String string) {
        return EMAIL.matcher(string).matches();
    }

    public static boolean matchesInteger(String string) {
        return INTEGER.matcher(string).matches();
    }
    
    public static boolean matchesTag(String string) {
        return TAG.matcher(string).matches();
    }
    
}
