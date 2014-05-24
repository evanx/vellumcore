package vellum.jx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class Convertors {

    final static Logger logger = LoggerFactory.getLogger(Convertors.class);

    public static boolean coerceBoolean(Object value) throws JMapException {
        if (value == null) {
            throw new JMapException("Null boolean");
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new JMapException("Not boolean: " + value);
    }

    public static boolean coerceBoolean(Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        logger.warn("coerceBoolean {} {}", value.getClass(), value.toString());
        return defaultValue;
    }

    public static Integer coerceInteger(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        logger.warn("coerceInteger {} {}", value.getClass(), value.toString());
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
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        logger.warn("coerceInt {} {}", value.getClass(), value.toString());
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
        logger.warn("coerceLong {} {}", value.getClass(), value.toString());
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
