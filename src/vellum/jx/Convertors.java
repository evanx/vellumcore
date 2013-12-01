
package vellum.jx;

/**
 *
 * @author evan.summers
 */
public class Convertors {

    public static int coerceInt(Object value, int defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        return defaultValue;
    }

    public static Integer coerceInteger(Object value, Integer defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof String) {
            return new Integer((String) value);
        }
        if (value instanceof Integer) {
            return ((Integer) value);
        }
        return defaultValue;
    }
    
}
