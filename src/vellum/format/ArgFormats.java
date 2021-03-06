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
package vellum.format;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import vellum.util.Strings;
import vellum.util.Types;

/**
 *
 * @author evan.summers
 */
public class ArgFormats {

    public static ArgFormats formatter = new ArgFormats();
    public static ArgFormats verboseFormatter = new ArgFormats();
    public static ArgFormats displayFormatter = new ArgFormats();

    public static final String DEFAULT_DELIMITER = ", ";
    public static final String COMMA_DELIMITER = ", ";
    public static final String SINGLE_QUOTE = "'";
    public static final String DOUBLE_QUOTE = "\"";
    
    static {
        verboseFormatter.verbose = true;
        displayFormatter.displayable = true;
    }
    
    boolean displayable = true;
    boolean verbose = false;
    String delimiter = COMMA_DELIMITER;
    String quote = DOUBLE_QUOTE;
            
    public ArgFormats() {
    }
    
    public ArgFormats(boolean displayable, String delimiter) {
        this.displayable = displayable;
        this.delimiter = delimiter;
    }

    public String formatThrowable(Throwable arg) {
        if (!Strings.isEmpty(arg.getMessage())) {
            return arg.getMessage();
        }
        return arg.getClass().getSimpleName();
    }
    
    public String format(Object arg) {
        if (arg == null) {
            if (displayable) return "";
            return "null";
        } else if (arg instanceof Class) {
            return ((Class) arg).getSimpleName();
        } else if (arg instanceof Throwable) {
            return formatThrowable((Throwable) arg);
        } else if (arg instanceof Calendar) {
            Calendar calendar = (Calendar) arg;
            return CalendarFormats.timestampZoneFormat.format(calendar.getTime());
        } else if (arg instanceof Date) {
            return CalendarFormats.timestampFormat.format((Date) arg);
        } else if (Strings.isEmpty(arg.toString())) {
            return "empty";
        } else if (arg instanceof byte[]) {
            return String.format("[%s]", formatArray(toList((byte[]) arg)));
        } else if (arg instanceof Object[]) {
            return String.format("[%s]", formatArray((Object[]) arg));
        } else if (arg instanceof String[]) {
            return String.format("[%s]", formatArray((String[]) arg));
        } else {
            return arg.toString();
        }
    }

    public String formatArgs(Object ... args) {
        return formatArray(args);
    }

    public String formatArray(Collection collection) {
        return formatArray(collection.toArray());
    }
    
    public String formatArray(Object[] args) {
        if (args == null) {
            if (displayable) return "";
            return "null[]";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (builder.length() > 0) {
                builder.append(delimiter);
            }
            String string = format(arg);
            if (string.contains(delimiter)) {
                builder.append("{");
                builder.append(string);
                builder.append("}");
            } else if (string.trim().isEmpty()) {
                builder.append("(empty)");
            } else {
                builder.append(string);
            }
        }
        return builder.toString();
    }

    public String formatQuote(Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
                builder.append(quote);
                builder.append(Types.formatPrint(arg));
                builder.append(quote);
            }
        }
        return builder.toString();
    }
    
    public static String formatVerbose(Object[] args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            if (arg == null) {
                builder.append("null");
            } else {
                String string = Types.formatPrint(arg);
                if (Strings.isEmpty(string)) {
                    string = "empty";
                }
                if (arg.getClass() != String.class && !arg.getClass().isPrimitive()) {
                    builder.append("(");
                    builder.append(arg.getClass().getSimpleName());
                    builder.append(") ");
                }
                builder.append(string);
            }
        }
        return builder.toString();
    }

    public List toList(byte[] array) {
        List list = new ArrayList();
        for (byte element : array) {
            list.add(element);
        }
        return list;
    }
    
}
