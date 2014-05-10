/*
 */
package vellum.jx;

import java.util.Map;

/**
 *
 * @author evan.summers
 */
public class JEntry {
    String key;
    Object value;

    public JEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public JEntry(Map.Entry entry) {
        this(entry.getKey().toString(), entry.getValue());        
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return String.format("{%s: %s}", key, value);
    }
        
}    
    
