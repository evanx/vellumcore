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

import java.text.ParseException;
import java.util.Date;
import vellum.data.SafeDateFormat;

/**
 *
 * @author evan.summers
 */
public class CalendarFormats {

    public static final String millisTimestampPattern = "yyyy-MM-dd HH:mm:ss,SSS";
    public static final SafeDateFormat dateFormat = new SafeDateFormat("yyyy-MM-dd");
    public static final SafeDateFormat numericTimestampMinuteFormat = new SafeDateFormat("yyyyMMddHHmm");
    public static final SafeDateFormat timestampFormat = new SafeDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    public static final SafeDateFormat timestampZoneFormat = new SafeDateFormat("yyyy-MM-dd HH:mm:ss,SSS Z");
    public static final SafeDateFormat millisTimestampFormat = 
            new SafeDateFormat(millisTimestampPattern);
    public static final SafeDateFormat timeFormat = new SafeDateFormat("HH:mm:ss");
    public static final SafeDateFormat shortTimeFormat = new SafeDateFormat("HH:mm");
    
    public static Date parse(SafeDateFormat dateFormat, String string) throws ParseException {
        return dateFormat.parse(string);
    }

    public static Date parseTimestampMillis(String string) throws ParseException {
        SafeDateFormat format = millisTimestampFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return parse(format, string);
    }

    public static Date parseTimestamp(String string) throws ParseException {
        SafeDateFormat format = timestampFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return parse(format, string);
    }

    public static Date parseDate(String string) throws ParseException {
        if (string.length() > timestampFormat.getPattern().length()) {
            string = string.substring(0, timestampFormat.getPattern().length());
        }
        if (string.length() == timestampFormat.getPattern().length()) {
            int index = string.indexOf(".");
            if (index == string.length() - 4) {
                string = string.substring(0, index) + "," + string.substring(index + 1);
            }
            return parse(timestampFormat, string);
        }
        return parse(dateFormat, string);
    }
    
}
