/*
 * Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package vellum.config;

import vellum.type.ComparableTuple;

/**
 *
 * @author evan.summers
 */
public class ConfigEntry {
    final String type;
    final String name;
    final ComparableTuple key;
    final ConfigProperties properties = new ConfigProperties();

    public ConfigEntry(String type, String name) {
        this.type = type;
        this.name = name;
        this.key = ComparableTuple.create(type, name);
    }

    public ComparableTuple getKey() {
        return key;
    }
    
    public String getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    public ConfigProperties getProperties() {
        return properties;
    }    
}
