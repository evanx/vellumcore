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
package vellum.data;

import java.util.*;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class Maps {

   static Logger logger = LoggerFactory.getLogger(Maps.class);

   public static <K, V extends Comparable> V getMinimumValue(Map<K, V> map) {
      return getMinimumValueEntry(map).getValue();
   }

   public static <K, V extends Comparable> Entry<K, V> getMinimumValueEntry(Map<K, V> map) {
      Entry<K, V> entry = null;
      for (Entry<K, V> element : map.entrySet()) {
         if (entry == null || element.getValue().compareTo(entry.getValue()) < 0) {
            entry = element;
         }
      }
      return entry;
   }

   public static <K, V extends Comparable> V getMaximumValue(Map<K, V> map) {
      return getMaximumValueEntry(map).getValue();
   }

   public static <K, V extends Comparable> Entry<K, V> getMaximumValueEntry(Map<K, V> map) {
      Entry<K, V> entry = null;
      for (Entry<K, V> element : map.entrySet()) {
         if (entry == null || element.getValue().compareTo(entry.getValue()) > 0) {
            entry = element;
         }
      }
      return entry;
   }

   public static <K, V extends Comparable> LinkedList<K> descendingValueKeys(Map<K, V> map) {
      return keyLinkedList(descendingValueEntrySet(map));
   }

   public static <K, V extends Comparable> NavigableSet<Entry<K, V>>
           ascendingValueEntrySet(Map<K, V> map) {
      TreeSet set = new TreeSet(new Comparator<Entry<K, V>>() {

         @Override
         public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            return o1.getValue().compareTo(o2.getValue());
         }
      });
      set.addAll(map.entrySet());
      return set;
   }

   public static <K, V extends Comparable> LinkedList<K> ascendingValueKeys(Map<K, V> map) {
      return keyLinkedList(ascendingValueEntrySet(map));
   }

   public static <K, V extends Comparable> NavigableSet<Entry<K, V>>
           descendingValueEntrySet(Map<K, V> map) {
      TreeSet set = new TreeSet(new Comparator<Entry<K, V>>() {

         @Override
         public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            return o2.getValue().compareTo(o1.getValue());
         }
      });
      set.addAll(map.entrySet());
      return set;
   }

   public static <K, V> LinkedList<K> keyLinkedList(NavigableSet<Entry<K, V>> entrySet) {
      LinkedList<K> keyList = new LinkedList();
      for (Map.Entry<K, V> entry : entrySet) {
         keyList.add(entry.getKey());
      }
      return keyList;
   }

   public static <K, V> Map<K, V> newMap(K key, V value) {
      Map map = new HashMap();
      map.put(key, value);
      return map;
   }

   public static <K, V> Map.Entry<K, V> newEntry(K key, V value) {
      return new MapEntry(key, value);
   }

   public static <K, V> Map<K, V> newMap(MapEntry... entries) {
      Map map = new HashMap();
      for (MapEntry entry : entries) {
         map.put(entry.getKey(), entry.getValue());
      }
      return map;
   }

   public static boolean containsKeys(Map map, Object... keys) {
      for (Object key : keys) {
         if (!map.containsKey(key)) {
            logger.warn("containsKeys {}", key);
            return false;
         }
      }
      return true;
   }

   public static void removeAllKeys(Map map, Collection removeKeys) {
      for (Object key : removeKeys) {
         map.remove(key);
      }
   }

   public static void retainAllKeys(Map map, Collection retainKeys) {
      Collection removeKeys = new ArrayList(map.keySet());
      removeKeys.removeAll(retainKeys);
      removeAllKeys(map, removeKeys);
   }
}

class EntryAscendingComparator<K, V extends Comparable> implements Comparator<Entry<K, V>> {

   @Override
   public int compare(Entry o1, Entry o2) {
      return ((Comparable) o1.getValue()).compareTo(o2.getValue());
   }
}

class EntryDescendingComparator<K, V extends Comparable> implements Comparator<Entry<K, V>> {

   @Override
   public int compare(Entry o1, Entry o2) {
      return ((Comparable) o2.getValue()).compareTo(o1.getValue());
   }
}
