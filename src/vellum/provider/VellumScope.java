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
package vellum.provider;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class VellumScope {
    private static final Logger logger = LoggerFactory.getLogger(VellumScope.class);
    Map<Class, Object> map = new HashMap();
    
    public VellumScope() {
    }

    public void put(Object instance) {
        Class type = instance.getClass();
        if (map.containsKey(type)) {
            logger.warn("put {}", type);
        }
        map.put(type, instance);
    }
    
    public <T> T get(Class<T> type) {
        try {
            T instance = (T) map.get(type);
            if (instance == null) {
                logger.warn("get {}", type);
                instance = type.newInstance();
                map.put(type, instance);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(type.getName(), e);
        }
    }
}
