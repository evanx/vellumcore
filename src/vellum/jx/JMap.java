/*
 */
package vellum.jx;

import com.google.gson.Gson;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class JMap extends HashMap<String, Object> {
    final static Logger logger = LoggerFactory.getLogger(JMap.class);
    
    String text;
    
    public JMap() {
    }

    public JMap(String text) {
        this.text = text;
    }
    
    public JMap(JEntry... entries) {
        for (JEntry entry : entries) {
            super.put(entry.getKey(), entry.getValue());
        }
    }

    public JMap(Properties properties) {
        for (Object key : properties.keySet()) {
            put(key.toString(), properties.get(key));
        }
    }

    public JMap(String section, Properties properties) {
        for (Object key : properties.keySet()) {
            String name = key.toString();
            if (name.startsWith(section)) {
                if (name.charAt(section.length()) == '.') {
                    name = name.substring(section.length() + 1);
                    put(name, properties.get(key));
                    logger.info("property {} {}", name, properties.get(key));
                }
            }
        }
    }
    
    public Collection getCollection(String key) {
        return (Collection) super.get(key);
    }

    public JMap add(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public JMap getMap(String key) {
        return (JMap) super.get(key);
    }

    public Object getObject(String key) throws JMapException {
        Object value = super.get(key);
        if (value == null) {
            throw new JMapException(key);
        }
        return value;
    }
    
    public String getString(String key) throws JMapException {
        return getObject(key).toString();
    }

    public String getString(String key, String defaultValue) {
        Object value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    public char[] getChars(String key) throws JMapException {
        return getString(key).toCharArray();
    }

    public int getInt(String key, int defaultValue) {
        return Convertors.coerceInt(super.get(key), defaultValue);
    }

    public int getInt(String key) throws JMapException {
        return Convertors.coerceInt(super.get(key));
    }
    
    public long getLong(String key) throws JMapException {
        return Convertors.coerceLong(getObject(key).toString());
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Convertors.coerceBoolean(super.get(key), defaultValue);
    }
    
    public long getLong(String key, long defaultValue) {
        return Convertors.coerceLong(super.get(key), defaultValue);
    }

    public Integer getInteger(String key) throws JMapException {
        return Convertors.coerceInt(getObject(key));
    }
    
    public Integer getInteger(String key, Integer value) {
        return Convertors.coerceInteger(super.get(key), value);
    }

    public Integer getInteger(String key, int value) {
        return Convertors.coerceInteger(super.get(key), value);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getText() {
        return text;
    }
    
    @Override
    public String toString() {
        return toJson();
    }
}
