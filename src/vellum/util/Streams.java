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
package vellum.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import vellum.exception.ArgsRuntimeException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.exception.SizeRuntimeException;

/**
 *
 * @author evan.summers
 */
public class Streams {

   private final static Logger logger = LoggerFactory.getLogger(Streams.class);

   public static final String fileSeparator = System.getProperty("file.separator");
   public static final String userHomeDir = System.getProperty("user.home");

   public static BufferedReader newBufferedReader(InputStream inputStream) {
      return new BufferedReader(new InputStreamReader(inputStream));
   }

   public static BufferedReader newBufferedGzip(String fileName) throws FileNotFoundException, IOException {
      return newBufferedGzip(new File(fileName));
   }

   public static BufferedReader newBufferedGzip(File file) throws FileNotFoundException, IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
              new GZIPInputStream(new FileInputStream(file))));
      return reader;
   }

   public static BufferedReader newBufferedGzipInputStream(File file) throws FileNotFoundException, IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
              new GZIPInputStream(new FileInputStream(file))));
      return reader;
   }

   public static String loadResourceString(Class parent, String resourceName) throws IOException {
      return readString(getResourceAsStream(parent, resourceName));
   }

   public static byte[] readResourceBytes(Class parent, String resourceName) throws IOException {
      return readBytes(getResourceAsStream(parent, resourceName));
   }

   public static String readResourceString(Class parent, String resourceName) throws IOException {
      return readString(getResourceAsStream(parent, resourceName));
   }

   protected static InputStream getResourceAsStream(Class type, String resourceName) {
      InputStream stream = type.getResourceAsStream(resourceName);
      if (stream == null) {
         throw new ArgsRuntimeException(type, resourceName);
      }
      return stream;
   }

   public static byte[] readGzipBytes(File file) throws FileNotFoundException, IOException {
      try (InputStream stream = new GZIPInputStream(new FileInputStream(file))) {
         return readBytes(stream);
      }
   }

   public static byte[] readBytes(String filePath) throws IOException {
      return readBytes(new File(filePath));
   }

   public static byte[] readBytes(File file) throws FileNotFoundException, IOException {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      FileInputStream stream = new FileInputStream(file);
      while (true) {
         int b = stream.read();
         if (b < 0) {
            return outputStream.toByteArray();
         }
         outputStream.write(b);
      }
   }

   public static byte[] readBytes(InputStream stream) throws IOException {
      try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
         while (true) {
            int b = stream.read();
            if (b < 0) {
               return outputStream.toByteArray();
            }
            outputStream.write(b);
         }
      }
   }

   public static StringBuilder readStringBuilder(InputStream stream) throws IOException {
      return new StringBuilder(readString(stream));
   }

   public static String readString(InputStream stream) throws IOException {
      return new String(readBytes(stream));
   }

   public static String readString(File file) throws IOException {
      return new String(readBytes(new FileInputStream(file)));
   }

   public static String readString(String fileName) throws IOException {
      return new String(readBytes(new FileInputStream(fileName)));
   }

   public static char[] readChars(InputStream stream) throws IOException {
      return Bytes.toCharArray(readBytes(stream));
   }

   public static InputStream exec(String command) throws IOException {
      logger.debug("exec {}", command);
      Process process = Runtime.getRuntime().exec(command);
      return process.getInputStream();
   }

   public static void process(LineProcessor processor, InputStream stream) throws Exception {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
         while (true) {
            String line = reader.readLine();
            if (line == null) {
               break;
            }
            processor.processLine(line);
         }
      }
   }

   public static void close(ServerSocket closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void close(Socket closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void close(Closeable closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static void renameTo(String srcFileName, String destFileName) {
      logger.debug("replaceFile {} {}", srcFileName, destFileName);
      File srcFile = new File(srcFileName);
      File destFile = new File(destFileName);
      srcFile.renameTo(destFile);
   }

   public static List<String> readLineList(InputStream stream, int capacity) throws IOException {
      List<String> lineList = new ArrayList();
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      while (true) {
         String line = reader.readLine();
         if (line == null) {
            return lineList;
         }
         lineList.add(line);
         if (capacity > 0 && lineList.size() > capacity) {
            throw new SizeRuntimeException(lineList.size());
         }
      }
   }

   public static void read(InputStream stream, StringBuilder builder) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      while (true) {
         String line = reader.readLine();
         if (line == null) {
            return;
         }
         builder.append(line);
         builder.append("\n");
      }
   }

   public static String readString(InputStream stream, long capacity) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      StringBuilder builder = new StringBuilder();
      while (true) {
         String line = reader.readLine();
         if (line == null) {
            return builder.toString();
         }
         builder.append(line);
         builder.append("\n");
         if (capacity > 0 && builder.length() > capacity) {
            throw new SizeRuntimeException(builder.length());
         }
      }
   }

   public static void transmit(InputStream inputStream, File file)
           throws IOException {
      transmit(inputStream, new FileOutputStream(file));
   }

   public static void transmit(InputStream inputStream, OutputStream outputStream)
           throws IOException {
      while (true) {
         int b = inputStream.read();
         if (b < 0) {
            return;
         }
         outputStream.write(b);
      }
   }

   public static void println(OutputStream outputStream, Object data) {
      new PrintWriter(outputStream).println(data);
   }

   public static String parseFileName(String urlString) {
      int index = urlString.lastIndexOf('/');
      if (index >= 0) {
         return urlString.substring(index + 1);
      } else {
         return urlString;
      }
   }

   public static int connectTimeout = 15000;
   public static int readTimeout = 20000;

   public static URLConnection connect(String urlString) throws IOException {
      return connect(new URL(urlString));
   }
   
   public static URLConnection connect(URL url) throws IOException {
      URLConnection connection = url.openConnection();
      connection.setDoOutput(false);
      connection.setDoInput(true);
      connection.setConnectTimeout(connectTimeout);
      connection.setReadTimeout(readTimeout);
      connection.connect();
      return connection;
   }

   public static int connectHeadCode(URL url) throws IOException {
      return connectHead(url).getResponseCode();
   }
   
   public static HttpURLConnection connectHead(URL url) throws IOException {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("HEAD");
      connection.setDoOutput(false);
      connection.setDoInput(false);
      connection.setConnectTimeout(connectTimeout);
      connection.setReadTimeout(readTimeout);
      connection.connect();
      return connection;
   }
   
   public static byte[] readContent(String urlString) throws IOException {
      URLConnection connection = connect(urlString);
      int length = connection.getContentLength();
      byte[] content = readBytes(new BufferedInputStream(connection.getInputStream()));
      if (content.length < length) {
         throw new IOException(String.format("Read only %d of %d for %s", content.length, length, urlString));
      }
      return content;
   }

   public static void write(byte[] content, File file)
           throws FileNotFoundException, IOException {
      try (FileOutputStream outputStream = new FileOutputStream(file)) {
         outputStream.write(content);
      }
   }

   public static void postHttp(byte[] content, URL url) throws IOException {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      try {
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "plain/text");
         connection.setDoOutput(true);
         connection.setDoInput(false);
         InputStream inputStream = new ByteArrayInputStream(content);
         transmit(inputStream, connection.getOutputStream());
      } finally {
         connection.disconnect();
      }
   }
}
