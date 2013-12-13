/*
 * Source https://github.com/evanx by @evanxsummers
 * 
 */
package vellum.connection;

import vellum.util.ExtendedProperties;

/**
 *
 * @author evan.summers
 */
public class DataSourceProperties {
    String driver;
    String name;
    String url;
    String user;
    String password;
    boolean enabled;
    Integer poolSize; 
    
    public DataSourceProperties() {
    }

    public DataSourceProperties(ExtendedProperties props) {
        this(props.getString("driver"),
                props.getString("url"),
                props.getString("user"),
                props.getString("password", null),
                props.getBoolean("enabled", true),
                props.getInt("poolSize", 0));
    }
    
    public DataSourceProperties(String driver, String url, String user, String password, 
            boolean enabled, Integer poolSize) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.enabled = enabled;
        this.poolSize = poolSize;
    }
    
    public String getDriver() {
        return driver;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Integer getPoolSize() {
        return poolSize;
    } 
}
