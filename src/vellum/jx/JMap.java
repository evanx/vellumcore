/*
 */
package vellum.jx;

import com.google.gson.Gson;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author evan.summers
 */
public class JMap extends HashMap<String, Object> {
    
    public JMap() {
    }

    public Collection getCollection(String key) {
        return (Collection) super.get(key);
    }

    public JMap add(String key, Object value) {
        super.put(key, value);
        return this;        
    }
    
    public Map getMap(String key) {
        return (Map) super.get(key);
    }

    public String getString(String key) throws JMapException {
        Object value = super.get(key);
        if (value == null) {
            throw new JMapException(key);
        }
        return value.toString();
    }

    public char[] getChars(String key) throws JMapException {
        return getString(key).toCharArray();
    }
    
    public String getString(String key, String defaultValue) {
        Object value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }
    
    public int getInt(String key, int defaultValue) {
        return Convertors.coerceInt(super.get(key), defaultValue);
    }

    public Integer getInteger(String key) {
        return Convertors.coerceInteger(super.get(key), null);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}