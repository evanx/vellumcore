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

import java.util.concurrent.TimeUnit;

/**
 *
 * @author evan.summers
 */
public class Seconds {
    
    public static long toMillis(long seconds) {
        return seconds*1000;
    }
    public static long fromMillis(long millis) {
        return millis/1000;
    }
    
    public static long toMinutes(long seconds) {
        return seconds/60;
    }

    public static long fromMinutes(long minutes) {
        return minutes*60;
    }

    public static long toHours(long seconds) {
        return seconds/60/60;
    }

    public static long fromHours(long hours) {
        return hours*60*60;
    }
    
    public static long toDays(long seconds) {
        return seconds/60/60/24;
    }
    
    public static long fromDays(long days) {
        return TimeUnit.DAYS.toSeconds(days);
    }
}
