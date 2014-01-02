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
package vellum.security;

import java.security.Key;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import vellum.exception.Exceptions;
import vellum.util.Base64;

/**
 *
 * @author evan.summers
 */
public class PemCerts {

    public static final String BEGIN_PRIVATE_KEY = formatPem("BEGIN PRIVATE KEY");
    public static final String END_PRIVATE_KEY = formatPem("END PRIVATE KEY");
    public static final String BEGIN_CERT = formatPem("BEGIN CERTIFICATE");
    public static final String END_CERT = formatPem("END CERTIFICATE");
    private static final String dashes = "-----";

    private static String formatPem(String label) {
        return dashes + label + dashes;
    }
    
    public static String buildKeyPem(Key privateKey) throws Exception, CertificateException {
        StringBuilder builder = new StringBuilder();
        builder.append(BEGIN_PRIVATE_KEY);
        builder.append('\n');
        builder.append(Base64.encode(privateKey.getEncoded()));
        builder.append(END_PRIVATE_KEY);
        builder.append('\n');
        return builder.toString();
    }

    public static String buildCertPem(Certificate cert) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(BEGIN_CERT);
            builder.append('\n');
            builder.append(Base64.encode(cert.getEncoded()));
            builder.append(END_CERT);
            builder.append('\n');
            return builder.toString();
        } catch (CertificateEncodingException e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static byte[] decodePemDer(String pem) throws Exception {
        int index = pem.lastIndexOf(dashes);
        if (index > 0) {
            pem = pem.substring(0, index);
            index = pem.lastIndexOf(dashes);
            pem = pem.substring(0, index);
            index = pem.lastIndexOf(dashes);
            pem = pem.substring(index + dashes.length());
        }
        return Base64.decode(pem);
    }

}
