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

import java.util.Collection;
import java.util.Date;
import vellum.util.Lists;
import vellum.util.Strings;
/**
 *
 * @author evan.summers
 */
public class TypeFormats {

    public static TypeFormats formatter = new TypeFormats(false);
    public static TypeFormats verboseFormatter = new TypeFormats(true);
    public static TypeFormats displayFormatter = new TypeFormats(true);
    
    static {
        verboseFormatter.verbose = true;
        displayFormatter.displayable = true;
    }
    
    boolean displayable = false;
    boolean verbose = false;
    
    TypeFormats(boolean displayable) {
        this.displayable = displayable;
    }
    
    public String format(Object arg) {
        if (arg == null) {
            if (displayable) return "";
            return "null";
        } else if (arg instanceof Class) {
            return ((Class) arg).getSimpleName();
        } else if (arg instanceof Date) {
            return DefaultDateFormats.timeMillisFormat.format((Date) arg);
        } else if (Strings.isEmpty(arg.toString())) {
            return "empty";
        } else if (arg instanceof byte[]) {
            return String.format("[%s]", Lists.format(Lists.asList((byte[]) arg)));
        } else if (arg instanceof Object[]) {
            return String.format("[%s]", Lists.format((Object[]) arg));
        } else if (arg instanceof String[]) {
            return String.format("[%s]", Lists.format((String[]) arg));
        } else if (arg instanceof Collection) {
            return String.format("[%s]", Lists.format((Collection) arg));
        } else {
            return arg.toString();
        }
    }

}
