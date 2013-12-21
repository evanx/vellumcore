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
package vellum.storage;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class CachingEntityService<E extends AbstractIdEntity> implements EntityService<E> {

    private static final Logger logger = LoggerFactory.getLogger(CachingEntityService.class);
    protected final Map<Comparable, E> keyMap = new TreeMap();
    private final Map<Long, E> idMap = new TreeMap();
    private final Queue<E> evictQueue = new LinkedList();
    int capacity;
    EntityMatcher matcher;
    long seq = 1;
    
    public CachingEntityService(int capacity, EntityMatcher matcher) {
        this.capacity = capacity;
        this.matcher = matcher;
    }

    public synchronized void clear() {
        keyMap.clear();
        idMap.clear();
        evictQueue.clear();                
    }

    public synchronized void put(E entity) {
        idMap.put(entity.getId(), entity);
        keyMap.put(entity.getKey(), entity);
        evict();
    }

    public synchronized Collection<E> putAll(Collection<E> entities) {
        for (E entity : entities) {
            put(entity);
        }
        return entities;
    }
    
    private void evict() {
        while (evictQueue.size() > capacity) {
            E evictEntity = evictQueue.poll();
            if (evictEntity != null) {
                remove(evictEntity);
            }
        }
    }
    
    private void remove(E entity) {
        assert(entity.getId() != null);
        idMap.remove(entity.getId());
        keyMap.remove(entity.getKey());
    }

    @Override
    public synchronized void add(E entity) throws StorageException {
        assert(entity.getId() == null);
        entity.setId(seq++);
        put(entity);
    }

    @Override
    public synchronized void replace(E entity) throws StorageException {
        assert(entity.getId() != null);
        put(entity);
    }

    @Override
    public synchronized boolean containsKey(Comparable key) throws StorageException {
        if (key instanceof Long) {
            return idMap.containsKey((Long) key);
        }
        return keyMap.containsKey(key);
    }

    public synchronized boolean contains(E entity) throws StorageException {
        assert(idMap.containsKey(entity.getId()) == keyMap.containsKey(entity.getKey()));
        return idMap.containsKey(entity.getId());
    }

    
    @Override
    public synchronized void remove(Comparable key) throws StorageException {
        clear();
    }

    @Override
    public synchronized E find(Comparable key) throws StorageException {
        if (key instanceof Long) {
            return idMap.get((Long) key);
        }
        return keyMap.get(key);
    }

    public synchronized E findId(Long id) throws StorageException {
        return idMap.get(id);
    }
    
    @Override
    public synchronized E retrieve(Comparable key) throws StorageException {
        E entity = find(key);
        if (entity == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, key);
        }
        return entity;
    }

    @Override
    public synchronized Collection<E> list() throws StorageException {
        return keyMap.values();
    }
    
    @Override
    public synchronized Collection<E> list(Comparable key) throws StorageException {
        return matcher.matches(list(), key);
    }
    
}
