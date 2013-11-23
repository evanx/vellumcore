/*
 * Source https://code.google.com/p/vellum by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
       
 */
package vellum.datatype;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author evan.summers
 */
public class QueueMap<K, V> extends HashMap<K, V> {
    int capacity;
    Queue<K> keyQueue = new LinkedList();

    public QueueMap(int capacity) {
        this.capacity = capacity;
    }

    public V put(K key, V value) {
        while (size() >= capacity) {
            super.remove(keyQueue.remove());
        }
        keyQueue.add(key);
        return super.put(key, value);
    }

}
