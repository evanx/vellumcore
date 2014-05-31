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
package vellum.util;

import java.io.Console;
import org.apache.log4j.Logger;

/**
 *
 * @author evan.summers
 */
public class MockableConsoleAdapter implements MockableConsole {
    static Logger logger = Logger.getLogger(MockableConsoleAdapter.class);
    Console console; 
    
    public MockableConsoleAdapter(Console console) {
        this.console = console;
    }
    
    @Override
    public char[] readPassword(String prompt, Object ... args) {
        if (console == null) {
            logger.warn("No console available: " + String.format(prompt, args));
            return new char[0];
        }
        return console.readPassword(prompt, args);
    }    
    
    @Override
    public void println(String message) {
        if (console == null) {
            logger.warn("No console available: " + message);
        } else {
            console.writer().println(message);
        }
    }
}