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
package vellum.crypto;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.util.Base64;

/**
 *
 * @author evan.summers
 */
public class Passwords {
    static final Logger logger = LoggerFactory.getLogger(Passwords.class);
    
    public static final int HASH_MILLIS = 200;
    public static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int ITERATION_COUNT = 100*1000;
    public static final int KEY_SIZE = 160;
    public static final int SALT_LENGTH = 16;
    public static final int ENCODED_SALT_LENGTH = 24;
    
    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
            
    public static byte[] hashPassword(char[] password, byte[] salt)
            throws GeneralSecurityException {
        return hashPassword(password, salt, ITERATION_COUNT, KEY_SIZE);
    }

    public static byte[] hashPassword(char[] password, byte[] salt,
            int iterationCount, int keySize) throws GeneralSecurityException {
        long timestamp = System.currentTimeMillis();
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (IllegalArgumentException e) {
            throw new GeneralSecurityException("key size " + keySize, e);
        } finally {
            long duration = System.currentTimeMillis() - timestamp;
            if (duration < HASH_MILLIS) {
                logger.warn("hashPassword {}ms", duration);
            }
        }
    }

    public static boolean matches(char[] password, String passwordHash, String salt) 
            throws GeneralSecurityException {
        return matches(password, passwordHash, salt, ITERATION_COUNT, KEY_SIZE);
    }

    public static boolean matches(char[] password, String passwordHash, String salt, int iterationCount, int keySize) 
            throws GeneralSecurityException {
        return matches(password, Base64.decode(passwordHash), Base64.decode(salt), iterationCount, keySize);
    }
    
    public static boolean matches(char[] password, byte[] passwordHash, byte[] salt) 
            throws GeneralSecurityException {
        return matches(password, passwordHash, salt, ITERATION_COUNT, KEY_SIZE);
    }

    public static boolean matches(char[] password, byte[] passwordHash, byte[] salt,
            int iterationCount, int keySize) throws GeneralSecurityException {
        return Arrays.equals(passwordHash, hashPassword(password, salt, 
                iterationCount, keySize));
    }
}
