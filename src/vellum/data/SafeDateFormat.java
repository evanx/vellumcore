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
package vellum.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author evan.summers
 */
public class SafeDateFormat {

   SimpleDateFormat dateFormat;
   String pattern;

   public SafeDateFormat(String pattern) {
      this.pattern = pattern;
      dateFormat = new SimpleDateFormat(pattern);
   }

   public String getPattern() {
      return pattern;
   }

   public synchronized String formatNow() {
       return format(new Date());           
   }
   
   public synchronized String format(Date date) {
      if (date == null) {
         return "";
      }
      return dateFormat.format(date);
   }

   public synchronized String format(TimeZone timeZone, long timestamp) {
      if (timestamp == 0) {
         return "";
      }
      Calendar calendar = Calendar.getInstance(timeZone);
      calendar.setTimeInMillis(timestamp);
      return dateFormat.format(calendar.getTime());
   }
   
   public synchronized Date parse(String string) throws ParseException{
       return parse(string, null);
       
   }
   
   public synchronized Date parse(String string, Date defaultValue) throws ParseException {
      if (string == null || string.isEmpty()) {
         return defaultValue;
      }
      if (string.length() > pattern.length()) {
          string = string.substring(0, pattern.length());
      }
      return dateFormat.parse(string);
   }
}
