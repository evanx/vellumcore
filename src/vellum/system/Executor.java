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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.util.Strings;

/**
 *
 * @author evan
 */
public class Executor<T> {

    private final static Logger logger = LoggerFactory.getLogger(Executor.class);
    private int exitCode; 
    private String result;
    private String error;

    public String exec(String command, String ... envp) throws Exception {
        return exec(command, null, envp);
    }

    public String exec(String command, byte[] data, Map map) throws Exception {
        List env = new ArrayList();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            if (value != null) {
                env.add(key.toString() + "=" + value.toString());
            }
        }
        return exec(command, data, Strings.toArray(env));
    }
    
    public String exec(String command, byte[] data, String[] envp) throws Exception {
        Process process = Runtime.getRuntime().exec(command, envp);
        logger.debug("process started {}", command);
        if (data != null) {
            process.getOutputStream().write(data);
            process.getOutputStream().close();
        }
        Future<String> resultFuture = Executors.newSingleThreadExecutor().submit(
                new ExecReader(process.getInputStream()));
        Future<String> errorFuture = Executors.newSingleThreadExecutor().submit(
                new ExecReader(process.getErrorStream()));
        result = resultFuture.get();
        error = errorFuture.get();
        exitCode = process.waitFor();
        logger.trace("process completed {}: {}", exitCode, command);
        logger.trace("process output {}: {}", result.length(), result);
        return result;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
    
    public int getExitCode() {
        return exitCode;
    }    
}
