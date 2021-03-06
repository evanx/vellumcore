/*
 Source https://github.com/evanx by @evanxsummers

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
package vellum.util;

import vellum.data.ComparableTuple;

/**
 *
 * @author evan.summers
 */
public class Comparables {

    public static boolean matches(Comparable[] array, Comparable[] other) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && other[i] != null &&
                    !array[i].equals(other[i])) {
                return false;
            }
        }
        return true;
    }
    
    public static int compareTo(Comparable[] array, Comparable[] other) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null && other[i] == null) {
                continue;
            }
            if (array[i] == null) {
                return -1;
            }
            if (other[i] == null) {
                return 1;
            }
            int result = array[i].compareTo(other[i]);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    public static int hashCode(Comparable[] array) {
        int hash = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                hash ^= array[i].hashCode();
            }
        }
        return hash;
    }
        
    public static int compareTo(Comparable comparable, Comparable other) {
        if (comparable == other) return 0;
        if (other == null) return 1;
        if (comparable == null) return -1;
        return comparable.compareTo(other);
    }
    
    public static boolean equals(Comparable comparable, Comparable other) {
        return compareTo(comparable, other) == 0;
    }

    public static ComparableTuple tuple(Comparable ... values) {
        return new ComparableTuple(values);        
    }
    
    public static String toString(Comparable comparable) {
        if (comparable == null) return "null";
        return comparable.toString();
    }
}
