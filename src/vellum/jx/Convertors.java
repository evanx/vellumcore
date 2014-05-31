/*
 * Source https://github.com/evanx by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership. The ASF licenses this file to
       you under the Apache License, Version 2.0 (the "License").
       You may not use this file except in compliance with the
       License. You may obtain a copy of the License at:

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
 */
package vellum.jx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class Convertors {

    final static Logger logger = LoggerFactory.getLogger(Convertors.class);

    public static boolean coerceBoolean(Object value) throws JMapsException {
        if (value == null) {
            throw new JMapsException("Null boolean");
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new JMapsException("Not boolean: " + value.getClass());
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

    public static int coerceInt(Object value) throws JMapsException {
        if (value == null) {
            throw new JMapsException("Null integer");
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new JMapsException("Not integer: " + value.getClass());
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

    public static long coerceLong(Object value) throws JMapsException {
        if (value == null) {
            throw new JMapsException("Null long integer");
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new JMapsException("Not long integer: " + value.getClass());
    }
}
