/*
 * Source https://code.google.com/p/vellum by @evanxsummers

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
package vellum.hype;

/**
 *
 * @author evan.summers
 */
public class Utils {

    public static boolean contains(char[] array, char ch) {
        for (char item : array) {
            if (item == ch) return true;
        }
        return false;
    }

    public static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    public static boolean isWord(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }

    public static boolean isWhitespace(String string) {
        if (string == null || string.length() == 0) return false;
        for (char ch : string.toCharArray()) {
            if (!isWhitespace(ch)) return false;
        }
        return true;
    }
    
    public static boolean isWord(String string) {
        if (string == null || string.length() == 0) return false;
        for (char ch : string.toCharArray()) {
            if (!isWord(ch)) return false;
        }
        return true;
    }
    
}
