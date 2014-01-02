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

import java.io.IOException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

/**
 *
 * @author evan.summers
 */
public class Certificates {
    public static final String LOCAL_DNAME = 
            "CN=localhost, OU=local, O=local, L=local, S=local, C=local";

    public static String formatDname(String cn, String ou, String o, String l, 
            String s, String c) {
        StringBuilder builder = new StringBuilder();
        appendf(builder, "", "cn=%s", cn);
        appendf(builder, "", "ou=%s", ou);
        appendf(builder, "", "o=%s", o);
        appendf(builder, "", "l=%s", l);
        appendf(builder, "", "s=%s", s);
        appendf(builder, "", "c=%s", c);
        return builder.toString();
    }

    public static void appendf(StringBuilder builder, String delimiter, String format, Object arg) {
        if (arg != null) {
            if (builder.length() > 0) {
                builder.append(delimiter);
            }
            builder.append(String.format(format, arg));
        }
    }
    
    public static X509Certificate findRootCert(KeyStore keyStore, String alias) throws Exception {
        return findRootCert(keyStore.getCertificateChain(alias));
    }
    
    public static X509Certificate findRootCert(Certificate[] chain) throws Exception {
        for (Certificate cert : chain) {
            if (cert instanceof X509Certificate) {
                X509Certificate x509Cert = (X509Certificate) cert;
                if (x509Cert.getSubjectDN().equals(x509Cert.getIssuerDN())) {
                    return x509Cert;
                }
            }
        }
        return null;
    }

    public static String getCommonName(Principal principal) throws CertificateException {
        return get("CN", principal);
    }    
    
    public static String getOrgUnit(Principal principal) throws CertificateException {
        return get("OU", principal);
    }    
    
    public static String getOrg(Principal principal) throws CertificateException {
        return get("O", principal);
    }    

    public static String get(String type, Principal principal) throws CertificateException {
        String dname = principal.getName();
        try {
            LdapName ln = new LdapName(dname);
            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase(type)) {
                    return rdn.getValue().toString();
                }
            }
            throw new InvalidNameException(dname);
        } catch (Exception e) {
            throw new CertificateException(e.getMessage());
        }
    }    
    
    public static boolean equals(X509Certificate cert, X509Certificate other) {
        if (cert.getSubjectDN().equals(other.getSubjectDN())) {
            if (Arrays.equals(cert.getPublicKey().getEncoded(),
                    other.getPublicKey().getEncoded())) {
                return true;
            }
        }
        return false;
    }
    
    public static X509Certificate[] toArray(Collection<X509Certificate> certificates) {
        X509Certificate[] array = new X509Certificate[certificates.size()];
        int index = 0;
        for (X509Certificate certificate: certificates) {
            array[index++] = certificate;
        }
        return array;
    }       
}
