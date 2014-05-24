/*
 */
package vellum.jx;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.data.Millis;
import vellum.exception.ParseException;

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

    public JMap(Collection<JEntry> entries) {
        for (JEntry entry : entries) {
            super.put(entry.getKey(), entry.getValue());
        }
    }

    public JMap(JEntry... entries) {
        for (JEntry entry : entries) {
            super.put(entry.getKey(), entry.getValue());
        }
    }
    
    public JMap add(String key, Object value) {
        super.put(key, value);
        return this;
    }
    
    public List getList(String key) {
        return (List) super.get(key);
    }

    public JMap getMap(String key) {
        if (!containsKey(key)) {
            logger.warn("empty {}", key);
            return new JMap();
        } else {
            return (JMap) get(key);
        }
    }

    public List<JMap> getListMap(String key) {
        if (!containsKey(key)) {
            logger.warn("empty {}", key);
            return new ArrayList();
        } else {
            return (List<JMap>) get(key);
        }
    }
    
    public Object getObject(String key) throws JMapException {
        Object value = get(key);
        if (value == null) {
            throw new JMapException(key);
        }
        return value;
    }
    
    public String getString(String key) throws JMapException {
        return getObject(key).toString();
    }

    public String getString(String key, String defaultValue) {
        Object value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    public char[] getChars(String key) throws JMapException {
        return getString(key).toCharArray();
    }

    public int getInt(String key, int defaultValue) {
        return Convertors.coerceInt(get(key), defaultValue);
    }

    public int getInt(String key) throws JMapException {
        return Convertors.coerceInt(get(key));
    }
    
    public long getLong(String key) throws JMapException {
        return Convertors.coerceLong(get(key));
    }

    public long getMillis(String key) throws ParseException, JMapException {
        return Millis.parse(getString(key));
    }    
    
    public long getMillis(String key, long defaultValue) throws ParseException {
        Object value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Millis.parse(value.toString());
    }    
    
    public boolean getBoolean(String key) throws JMapException {
        return Convertors.coerceBoolean(getObject(key));
    }
        
    public boolean getBoolean(String key, boolean defaultValue) {
        return Convertors.coerceBoolean(get(key), defaultValue);
    }
    
    public long getLong(String key, long defaultValue) {
        return Convertors.coerceLong(get(key), defaultValue);
    }

    public Integer getInteger(String key) throws JMapException {
        return Convertors.coerceInt(getObject(key));
    }
    
    public Integer getInteger(String key, Integer defaultValue) {
        return Convertors.coerceInteger(get(key), defaultValue);
    }

    public Integer getInteger(String key, int defaultValue) {
        return Convertors.coerceInteger(get(key), defaultValue);
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
