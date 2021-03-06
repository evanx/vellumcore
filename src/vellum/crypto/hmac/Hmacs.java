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
package vellum.crypto.hmac;

import java.security.GeneralSecurityException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import vellum.util.Base64;

public final class Hmacs {
    final static String algorithm = "HmacSHA256";
    
    public static String generateSecret() throws GeneralSecurityException {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.encode(secretKey.getEncoded());
    }

    public static String mac(String secret, String message) throws GeneralSecurityException {
        return Base64.encode(mac(secret, message.getBytes()));
    }
    
    public static byte[] mac(String secret, byte[] message) throws GeneralSecurityException {
        SecretKey secretKey = new SecretKeySpec(Base64.decode(secret), algorithm);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(message);
    }
}
