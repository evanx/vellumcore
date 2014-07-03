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
package vellum.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import vellum.util.Strings;
import vellum.util.Args;

/**
 * Utility methods related to exceptions.
 *
 * @author evan.summers
 */
public class Exceptions {

   public static Throwable getThrowable(Object[] args) {
      if (args.length > 0 && args[0] instanceof Throwable) {
         return (Throwable) args[0];
      }
      return null;
   }

   public static String getMessage(Object[] args) {
      return Args.format(args);
   }

   public static String getMessage(Throwable e) {
      if (e.getMessage() == null) {
         return e.getClass().getSimpleName();
      } else {
         return String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage());
      }
   }

   public static RuntimeException newRuntimeException(Object... args) {
      if (args.length == 1) {
         Throwable e = getThrowable(args);
         if (e instanceof RuntimeException) {
            return (RuntimeException) e;
         }
      }
      return new ArgsRuntimeException(args);
   }

   public static String printStackTrace(Throwable exception) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      try {
         exception.printStackTrace(ps);
         return baos.toString(Strings.ENCODING);
      } catch (UnsupportedEncodingException e) {
         throw Exceptions.newRuntimeException(e);
      }
   }

   public static void warn(Exception e) {
      e.printStackTrace(System.err);
   }

}
