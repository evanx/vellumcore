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
package vellum.util;

import vellum.format.Delimiters;

/**
 * Utility methods related to classes.
 * Utility methods for specific types eg {@code String}, are found in {@code Strings}.
 *
 * @author evan.summers
 */
public class Args {

    public static String format(Object ... args) {
        return formatDelimiter(Delimiters.COMMA, args);
    }

    public static String formatDelimiter(String delimiter, Object... args) {
        return Strings.join(delimiter, args);
    }
    
    public static String formatDelimiterSquash(String delimiter, Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (arg != null && arg.toString() != null && !arg.toString().isEmpty()) {
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
                builder.append(arg);
            }
        }
        return builder.toString();
    }
    
    public static boolean equals(Object object, Object... args) {
        for (Object arg : args) {
            if (arg != null && arg.equals(object)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalsIdentity(Object object, Object... args) {
        for (Object arg : args) {
            if (arg != null && arg == object) {
                return true;
            }
        }
        return false;
    }

    public static Object coalesce(Object ... args) {
        for (Object arg : args) {
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }

}
