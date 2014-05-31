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
public class JMapFormatter {

    final static Logger logger = LoggerFactory.getLogger(JMapFormatter.class);

    public static String escape(String string) {
        string = string.replaceAll("\"", "\\\"");
        return string;
    }

    public static String formatString(String string) {
        if (string == null) {
            return String.format("\"\"");
        } else {
            return String.format("\"%s\"", escape(string));
        }
    }

    public static String formatPrimitive(Object object) {
        if (object == null) {
            return "null";
        } else if (object instanceof CharSequence) {
            return formatString(object.toString());
        } else {
            return object.toString();
        }
    }

    public static String formatArray(Iterable iterable) {
        StringBuilder builder = new StringBuilder();
        for (Object item : iterable) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(formatObject(item));
        }
        return String.format("[%s]", builder.toString());
    }

    public static String formatObject(Object object) {
        if (object == null) {
            return "null";            
        } else if (object instanceof Iterable) {
            return formatArray((Iterable) object);
        } else if (object instanceof CharSequence) {
            return formatString(object.toString());
        } else if (object instanceof JMap) {
            return formatMap((JMap) object);
        } else {
            return formatPrimitive(object);
        }
    }

    public static String formatMap(JMap map) {
        StringBuilder builder = new StringBuilder();
        for (JEntry entry : JMaps.list(map)) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(formatEntry(entry));
        }
        return String.format("{%s}", builder.toString());
        
    }
    
    public static String formatEntry(JEntry map) {
        return String.format("%s: %s", formatKey(map.getKey()), formatObject(map.getValue()));
    }
    
    public static String formatKey(String key) {
        if (key.matches("^\\w*$")) {
            return key;
        }
        return String.format("\"%s\"", key);
    }
}
