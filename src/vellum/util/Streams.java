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
import vellum.exception.Exceptions;
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

    public static BufferedReader newBufferedGzip(String fileName) {
        try {
            File file = newFile(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(new FileInputStream(file))));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReader(String fileName) {
        if (fileName.endsWith(".gz")) {
            return newBufferedGzip(fileName);
        }
        try {
            File file = newFile(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReaderTail(String fileName, long length) {
        try {
            String command = String.format("tail -%d %s", length, fileName);
            InputStream inputStream = exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReaderTailFollow(String fileName, long length) {
        try {
            String command = String.format("tail -f %s", fileName);
            InputStream inputStream = exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReaderEnd(String fileName, long length) {
        try {
            File file = newFile(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            if (file.length() > length) {
                reader.skip(file.length() - length);
            }
            reader.readLine();
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static File newFile(String fileName) {
        if (true) {
            return new File(fileName);
        }
        if (fileName.startsWith("/")) {
            return new File(fileName);
        } else {
            return new File(userHomeDir, fileName);
        }
    }

    public static String loadResourceString(Class parent, String resourceName) {
        try {
            return readString(getResourceAsStream(parent, resourceName));
        } catch (IOException e) {
            throw Exceptions.newRuntimeException(e, parent, resourceName);
        }
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

    public static byte[] readBytes(String filePath) {
        return readBytes(new File(filePath));
    }
    
    public static byte[] readBytes(File file) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            FileInputStream stream = new FileInputStream(file);
            while (true) {
                int b = stream.read();
                if (b < 0) {
                    return outputStream.toByteArray();
                }
                outputStream.write(b);
            }
        } catch (IOException e) {
            throw Exceptions.newRuntimeException(e);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Socket closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void renameTo(String srcFileName, String destFileName) {
        logger.debug("replaceFile {} {}", srcFileName, destFileName);
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        srcFile.renameTo(destFile);
    }

    public static List<String> readLineList(InputStream stream, int capacity) {
        List<String> lineList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return lineList;
                }
                lineList.add(line);
                if (capacity > 0 && lineList.size() > capacity) {
                    throw new SizeRuntimeException(lineList.size());
                }
            } catch (Exception e) {
                throw Exceptions.newRuntimeException(e);
            }
        }
    }

    public static void read(InputStream stream, StringBuilder builder) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return;
                }
                if (builder != null) {
                    builder.append(line);
                    builder.append("\n");
                }
            } catch (Exception e) {
                throw Exceptions.newRuntimeException(e);
            }
        }
    }

    public static String readString(InputStream stream, long capacity) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return builder.toString();
                }
                builder.append(line);
                builder.append("\n");
                if (capacity > 0 && builder.length() > capacity) {
                    throw new SizeRuntimeException(builder.length());
                }
            } catch (Exception e) {
                throw Exceptions.newRuntimeException(e);
            }
        }
    }

    public static PrintWriter newPrintWriter(OutputStream outputStream) {
        return new PrintWriter(outputStream);
    }

    public static String baseName(String fileName) {
        int index = fileName.lastIndexOf(fileSeparator);
        if (index >= 0) {
            return fileName.substring(index + 1);
        }
        return fileName;
    }

    public static String removeFileNameExtension(File file) {
        return removeExtension(file.getName());

    }

    public static String removeExtension(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                fileName = fileName.substring(0, index);
                index = fileName.lastIndexOf('/');
                if (index >= 0) {
                    fileName = fileName.substring(index + 1);
                }
            }
        }
        return fileName;
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
    
    public static byte[] readContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.connect();
        int length = connection.getContentLength();
        byte[] content = readBytes(new BufferedInputStream(connection.getInputStream()));
        if (content.length < length) {
            throw new IOException(String.format("Read only %d of %d for %s", content.length, length, urlString));
        }
        return content;
    }

    public static void write(byte[] content, File file) 
            throws FileNotFoundException, IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content);
        outputStream.close();
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

    public static String getContentType(String path) {
        if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (path.endsWith(".html")) {
            return "text/html";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "text/javascript";
        } else if (path.endsWith(".txt")) {
            return "text/plain";
        } else if (path.endsWith(".json")) {
            return "text/json";
        } else if (path.endsWith(".html")) {
            return "text/html";
        } else if (path.endsWith(".ico")) {
            return "image/x-icon";
        } else {
            logger.warn(path);
            return "text/html";
        }
    }        
}
