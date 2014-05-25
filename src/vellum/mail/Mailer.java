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
package vellum.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class Mailer {
    static Logger logger = LoggerFactory.getLogger(Mailer.class);

    MailerProperties properties;
    Session session;

    public Mailer(MailerProperties properties) {
        this.properties = properties;
    }

    public void send(String recipient, String subject, String htmlContent) 
            throws MessagingException, IOException {
        if (properties == null || !properties.isEnabled()) {
            logger.warn("disabled {} {}", recipient, subject);
            return;
        }
        logger.info("email {}: {}", recipient, subject);
        Properties props = new Properties();
        props.put("mail.smtp.host", properties.getHost());
        props.put("mail.smtp.port", properties.getPort());
        if (properties.getUsername() != null) {
            props.put("mail.smtp.auth", true);
            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(properties.getUsername(), 
                            properties.getPassword());
                }
            };
            session = Session.getInstance(props, auth);
        } else {
            session = Session.getInstance(props);
        }
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(properties.getFrom()));
        message.setSentDate(new Date());
        message.setSubject(subject);
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
        message.setHeader("Organization", properties.getOrganisation());
        BodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlContent, "text/html");
        MimeMultipart multipart = new MimeMultipart("related");
        multipart.addBodyPart(htmlPart);
        if (properties.getLogoBytes() != null) {
            BodyPart dataPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(
                    new ByteArrayInputStream(properties.getLogoBytes()), "image/png");
            dataPart.setDataHandler(new DataHandler(source));
            dataPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(dataPart);
        }
        message.setContent(multipart);
        Transport.send(message);
    }
}