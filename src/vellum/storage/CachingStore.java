/*
 Source https://code.google.com/p/vellum by @evanxsummers

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
package vellum.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.SynchronousQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public abstract class CachingStore<E extends AbstractIdEntity> implements EntityStore<E> {

    private static final Logger logger = LoggerFactory.getLogger(CachingStore.class);
    private final Map<Comparable, E> keyMap = Collections.synchronizedMap(new TreeMap());
    private final Map<Long, E> idMap = Collections.synchronizedMap(new TreeMap());
    private final SynchronousQueue<E> evictQueue = new SynchronousQueue();
    EntityStore<E> delegate;
    int capacity;

    public CachingStore(int capacity, EntityStore delegate) {
        this.capacity = capacity;
        this.delegate = delegate;
    }

    public synchronized void clear() {
        keyMap.clear();
        idMap.clear();
        evictQueue.clear();                
    }

    private synchronized void remove(E entity) {
        idMap.remove(entity.getId());
        keyMap.remove(entity.getKey());
    }
    
    private synchronized void put(E entity) {
        while (evictQueue.size() > capacity) {
            E evictEntity = evictQueue.poll();
            if (evictEntity != null) {
                remove(evictEntity);
            }
        }
        idMap.put(entity.getId(), entity);
        keyMap.put(entity.getKey(), entity);
    }

    @Override
    public void insert(E entity) throws StorageException {
        delegate.insert(entity);
        put(entity);
    }

    @Override
    public void update(E entity) throws StorageException {
        delegate.update(entity);
    }

    @Override
    public boolean containsKey(Comparable key) throws StorageException {
        return delegate.containsKey(key);
    }

    @Override
    public void delete(Comparable key) throws StorageException {
        delegate.delete(key);
        clear();
    }

    @Override
    public E select(Comparable key) throws StorageException {
        return delegate.select(key);
    }

    @Override
    public E find(Comparable key) throws StorageException {
        if (key instanceof Long) {
            E entity = idMap.get((Long) key);
            if (entity != null) {
                return entity;
            }
        } else {
            E entity = keyMap.get(key);
            if (entity != null) {
                return entity;
            }
        }
        E entity = select(key);
        if (entity == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, key);
        }
        put(entity);
        return entity;
    }

    @Override
    public Collection<E> list() throws StorageException {
        return delegate.list();
    }
}
