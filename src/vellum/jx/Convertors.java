package vellum.jx;

/**
 *
 * @author evan.summers
 */
public class Convertors {

    public static Integer coerceInteger(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        return defaultValue;
    }
    
    
    public static int coerceInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        return defaultValue;
    }

    public static int coerceInt(Object value) throws JMapException {
        if (value == null) {
            throw new JMapException("Null integer");            
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new JMapException("Not integer: " + value);
    }

    public static long coerceLong(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return defaultValue;
    }
    
    public static long coerceLong(Object value) throws JMapException {
        if (value == null) {
            throw new JMapException("Null long integer");
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new JMapException("Not long integer: " + value);
    }    

}
