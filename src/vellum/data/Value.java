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

/**
 *
 * @author evan.summers
 */
public class Value<T> {

   T value;
   
   public Value() {      
   }

   public Value(T value) {
      this.value = value;
   }
      
   public T get() {
      return value;      
   }
   
   public boolean ok() {
      return value != null;
   }

   public static <T> Value<T> of(T value) {
      return new Value(value);
   }

   @Override
   public String toString() {
      if (value == null) {
         return "$null";
      } else {
         String string = value.toString();
         if (string.isEmpty()) {
            return "$empty";
         } else {
            return string;            
         }
      }
   }
      
   public final static Value none = new Value();
}
