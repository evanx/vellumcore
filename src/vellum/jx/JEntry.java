/*
 */
package vellum.jx;

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

    @Override
    public String toString() {
        return String.format("{%s: %s}", key, value);
    }
        
}    
    
