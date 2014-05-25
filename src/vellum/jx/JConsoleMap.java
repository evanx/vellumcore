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
package vellum.jx;

import com.google.gson.JsonPrimitive;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.exception.ArgsRuntimeException;
import vellum.util.MockableConsole;

/**
 *
 * @author evan.summers
 */
public class JConsoleMap extends JMap {
    final static Logger logger = LoggerFactory.getLogger(JConsoleMap.class);
    
    final MockableConsole console;
    
    public JConsoleMap(MockableConsole console) {
        this.console = console;
    }

    public JConsoleMap(MockableConsole console, JMap properties) {
        this(console);
        putAll(properties);
    }
    
    public JConsoleMap(MockableConsole console, Properties properties) {
        this(console, JMaps.map(properties));
    }

    public JConsoleMap(MockableConsole console, String section, Properties properties) {
        this(console);
        for (Object key : properties.keySet()) {
            String name = key.toString();
            if (name.startsWith(section)) {
                if (name.charAt(section.length()) == '.') {
                    name = name.substring(section.length() + 1);
                    put(name, properties.get(key));
                    logger.info("property {} {}", name, properties.get(key));
                }
            }
        }
    }

    public MockableConsole getConsole() {
        return console;
    }
        
    public char[] getPassword(String key, char[] defaultValue) {
        Object object = super.get(key);
        if (object == null) {
            return defaultValue;
        } else if (object instanceof char[]) {
            return (char[]) object;
        } else if (object instanceof String) {
            return object.toString().toCharArray();
        } else if (object instanceof JsonPrimitive) {
            return object.toString().toCharArray();
        } else {
            throw new ArgsRuntimeException("Invalid password property type", key, object.getClass());
        }
    }
    
    public char[] getPassword(String key) {
        Object object = super.get(key);
        if (object == null) {
            return console.readPassword(key);
        } else if (object instanceof char[]) {
            return (char[]) object;
        } else if (object instanceof String) {
            return object.toString().toCharArray();
        } else if (object instanceof JsonPrimitive) {
            return object.toString().toCharArray();
        } else {
            throw new RuntimeException("Invalid password property type: " + key);
        }
    }        
    
    public Class getClass(String key) throws ClassNotFoundException {
        Object object = get(key);
        if (object instanceof Class) {
            return (Class) object;
        } else if (object instanceof String) {
            return Class.forName((String) key);
        }
        throw new RuntimeException("Invalid class property type: " + key);
    }
    
    @Override
    public String toString() {
        return toJson();
    }    
}
