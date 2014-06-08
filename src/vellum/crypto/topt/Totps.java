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
package vellum.crypto.topt;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.util.Strings;

/**
 *
 * @author evan.summers
 */
public class Totps {

    static Logger logger = LoggerFactory.getLogger(Totps.class);
    static final int VARIANCE = 6;
    
    static long getCurrentTimeIndex() {
        return System.currentTimeMillis()/1000/30;
    }

    public static String generateSecret() {
        byte[] buffer = new byte[10];
        new SecureRandom().nextBytes(buffer);
        return new String(new Base32().encode(buffer));
    }
    
    public static String getQRBarcodeURL(String email, String secret) {
        String chl = "otpauth%3A%2F%2Ftotp%2F" + email + "%3Fsecret%3D" + secret;
        System.out.println(Strings.decodeUrl(chl));
        return "http://chart.apis.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=" + chl;
    }

    public static boolean verifyCode(String secret, int code)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return verifyCode(secret, code, getCurrentTimeIndex(), VARIANCE);
    }
    
    public static boolean verifyCode(String secret, int code, int variance)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return verifyCode(secret, code, getCurrentTimeIndex(), variance);
    }
    
    public static boolean verifyCode(String secret, int code, long timeIndex, int variance) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        for (int i = -variance; i <= variance; i++) {
            if (getCode(decodedKey, timeIndex + i) == code) {
                return true;
            }
        }
        return false;
    }

    static List<Integer> getCodeList(String secret, long timeIndex, int variance)
            throws NoSuchAlgorithmException, InvalidKeyException {
        List<Integer> list = new ArrayList();
        for (int i = -variance; i <= variance; i++) {
            list.add(getCode(new Base32().decode(secret), timeIndex + i));
        }
        return list;
    }

    public static int getCurrentCode(String secret)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return getCode(secret, getCurrentTimeIndex());
    }
    
    public static int getCode(String secret, long timeIndex)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return getCode(new Base32().decode(secret), timeIndex);
    }

    static int getCode(byte[] secret, long timeIndex) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1");
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(timeIndex);
        byte[] timeBytes = buffer.array();
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(timeBytes);
        int offset = hash[19] & 0xf;
        long truncatedHash = hash[offset] & 0x7f;
        for (int i = 1; i < 4; i++) {
            truncatedHash <<= 8;
            truncatedHash |= hash[offset + i] & 0xff;
        }
        return (int) (truncatedHash %= 1000000);
    }
}
