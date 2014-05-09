/*
 * Source https://github.com/evanx by @evanxsummers
 * 
 */
package vellum.mail;

import vellum.data.Emails;
import vellum.util.Args;
import vellum.util.ExtendedProperties;
import vellum.util.Streams;

/**
 *
 * @author evan.summers
 */
public class MailerProperties {

    byte[] logoBytes;
    String organisation;
    String from;
    String username;
    String password;
    String host = "localhost";
    int port = 25;
    boolean enabled = false;

    public MailerProperties() {
    }
    
    public void init(ExtendedProperties properties) {
        enabled = properties.getBoolean("enabled", true);
        host = properties.getString("host", host);
        port = properties.getInt("port", port);
        username = properties.getString("username", null);
        password = properties.getString("password", null);
        from = properties.getString("from");
        organisation = properties.getString("organisation", null);
        if (organisation == null) {
            organisation = Emails.getDomain(from);
        }
        String logoImagePath = properties.getString("logo", null);
        if (logoImagePath != null) {
            logoBytes = Streams.readBytes(logoImagePath);
        }
    }
    
    public void init(byte[] logoBytes, String organisation, String from) {
        this.logoBytes = logoBytes;
        this.organisation = organisation;
        this.from = from;
        this.enabled = true;
    }

    public void setLogoBytes(byte[] logoBytes) {
        this.logoBytes = logoBytes;
    }
    
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    public byte[] getLogoBytes() {
        return logoBytes;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getFrom() {
        return from;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }       

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return Args.format(organisation, from, logoBytes.length);
    }
    
    
}