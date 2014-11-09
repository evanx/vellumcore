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
package vellum.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.util.Streams;

/**
 *
 * @author evan.summers
 */
public class Systems {

   public static Logger logger = LoggerFactory.getLogger(Systems.class);

   public static final String osName = System.getProperty("os.name");
   public static final String userDir = System.getProperty("user.dir");
   public static final String homeDir = System.getProperty("user.home");

   public static boolean isLinux() {
      return osName.toLowerCase().startsWith("linux");
   }

   public static void sleep(long duration, TimeUnit timeUnit) {
      sleep(timeUnit.toMillis(duration));
   }

   public static void sleep(long millis) {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException e) {
         logger.warn("sleep", e);
      }
   }

   public static String getHostName() {
      String hostName = System.getProperty("hostName");
      if (hostName == null) {
         try {
            hostName = InetAddress.getLocalHost().getHostName();
         } catch (UnknownHostException e) {
            return e.getMessage();
         }
      }
      int index = hostName.indexOf(".");
      if (index > 0) {
         hostName = hostName.substring(0, index);
      }
      return hostName;
   }

   public static String getPath(String fileName) {
      if (fileName == null) {
         throw new RuntimeException(fileName);
      }
      if (Character.isLetter(fileName.charAt(0))) {
         return homeDir + "/" + fileName;
      }
      return fileName;
   }

   public static boolean isExcessiveLoadAvg(double threshold) throws IOException {
      return getLoadAvg() > threshold;
   }

   public static float getLoadAvg() throws IOException {
      if (!isLinux()) {
         return 0;
      } else {
         return Float.parseFloat(Streams.readString("/proc/loadavg").split("\\s")[0]);
      }
   }

   public static Iterable<String> execList(String command) throws IOException, InterruptedException {
      return Systems.execList(command, 99);
   }

   public static Process execProcess(String command) throws IOException, InterruptedException {
      Process process = Runtime.getRuntime().exec(command);
      process.waitFor();
      return process;
   }
   
   public static Iterable<String> execList(String command, int lineLimit) throws IOException, InterruptedException {
      Process process = Runtime.getRuntime().exec(command);
      process.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      List<String> list = new ArrayList();
      while (true) {
         String line = reader.readLine();
         logger.debug("exec line: {}", line);
         if (line == null || list.size() == lineLimit) {
            return list;
         }
         list.add(line);
      }
   }

   public static String execFirstLine(String command) throws IOException, InterruptedException {
      Process process = Runtime.getRuntime().exec(command);
      process.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      return reader.readLine();
   }

   public static String execString(String command) throws IOException, InterruptedException {
      Process process = Runtime.getRuntime().exec(command);
      process.waitFor();
      return Streams.readString(process.getInputStream());
   }
}
