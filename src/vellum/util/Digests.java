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

import java.security.MessageDigest;
import java.util.Arrays;
import vellum.util.Chars;

/**
 *
 * @author evan.summers
 */
public class Digests {
    public final static String DIGEST_ALG = "SHA-256";

    public static byte[] digest(char[] chars) throws Exception {
        byte[] bytes = Chars.getBytes(chars);
        byte[] digestBytes = MessageDigest.getInstance(DIGEST_ALG).digest(bytes);
        Arrays.fill(bytes, (byte) 0);
        return digestBytes;
    }
    
}
