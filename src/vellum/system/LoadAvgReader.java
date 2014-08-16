/*
 Source https://code.google.com/p/vellum by @evanxsummers

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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evanx
 */
public final class LoadAvgReader {

   static Logger logger = LoggerFactory.getLogger(LoadAvgReader.class);

   long lastTimestamp ;
   long period;
   double cachedLoadAvg;
   double highThreshold;
   double lowThreshold;
   static File procFile = new File("/proc/loadavg");

   public LoadAvgReader(long period, double lowThreshold, double highThreshold) throws IOException {
      this.lowThreshold = lowThreshold;
      this.highThreshold = highThreshold;
      if (!procFile.exists()) {
         throw new IOException("missing: " + procFile.getAbsolutePath());
      }
   }

   private void readLoadAvg() {
      try {
         try (BufferedReader reader = new BufferedReader(new FileReader(procFile))) {
            cachedLoadAvg = Float.parseFloat(reader.readLine().split("\\s")[0]);
         }
      } catch (IOException e) {
         logger.warn("read load failed", e);
      }
   }

   public double getLoadAvg() {
      long timestamp = System.currentTimeMillis();
      if (lastTimestamp == 0 || lastTimestamp + period > timestamp) {
         lastTimestamp = timestamp;
         readLoadAvg();
      }
      return cachedLoadAvg;
   }

   public boolean isHighLoad() {
      return getLoadAvg() > highThreshold;
   }

   public boolean isLowLoad() {
      return getLoadAvg() < lowThreshold;
   }

   public boolean isMediumLoad() {
      return !isHighLoad() && !isLowLoad();
   }
}
