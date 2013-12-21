/*
 */
package vellum.jx;

import com.google.gson.Gson;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author evan.summers
 */
public class JMap extends HashMap<String, Object> {
    
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

    public String getString(String key) throws JMapException {
        Object value = super.get(key);
        if (value == null) {
            throw new JMapException(key);
        }
        return value.toString();
    }

    public boolean isEmpty(String... keys) {
        for (String key : keys) {
            Object value = super.get(key);
            if (value == null) {
                return true;
            }
            if (value.toString().isEmpty()) {
                return true;
            }
        }
        return false;
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

    public long getLong(String key, long defaultValue) {
        return Convertors.coerceLong(super.get(key), defaultValue);
    }

    public long getLong(String key) throws JMapException {
        Object value = super.get(key);
        if (value == null) {
            throw new JMapException(key);
        }
        return Long.parseLong(value.toString());
    }

    public Integer getInteger(String key) {
        return Convertors.coerceInteger(super.get(key), null);
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
