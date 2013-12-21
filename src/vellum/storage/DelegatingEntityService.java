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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class DelegatingEntityService<E extends AbstractIdEntity> implements EntityService<E> {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingEntityService.class);
    CachingEntityService<E> cache;
    EntityService<E> delegate;
            
    public DelegatingEntityService(CachingEntityService<E> cache, EntityService<E> delegate) {
        this.cache = cache;
        this.delegate = delegate;
    }

    @Override
    public void add(E entity) throws StorageException {
        assert(entity.getId() == null);
        assert(!cache.containsKey(entity.getKey()));
        delegate.add(entity);
        cache.put(entity);
        assert(cache.contains(entity));
    }

    @Override
    public void replace(E entity) throws StorageException {
        delegate.replace(entity);
        cache.put(entity);
    }

    @Override
    public boolean containsKey(Comparable key) throws StorageException {
        if (cache.containsKey(key)) {
            assert(delegate.containsKey(key));
            return true;
        }
        return delegate.containsKey(key);
    }

    @Override
    public void remove(Comparable key) throws StorageException {
        cache.clear();
        delegate.remove(key);
    }

    @Override
    public E find(Comparable key) throws StorageException {
        E entity = cache.find(key);
        if (entity != null) {
            assert(delegate.find(key) != null);
            return entity;
        }
        entity = delegate.find(key);
        if (entity != null) {
            cache.add(entity);
        }
        return entity;
    }

    @Override
    public E retrieve(Comparable key) throws StorageException {        
        E entity = find(key);
        if (entity == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, key);
        }
        return entity;
    }

    @Override
    public Collection<E> list() throws StorageException {
        return cache.putAll(delegate.list());
    }

    @Override
    public Collection<E> list(Comparable key) throws StorageException {
        return cache.putAll(delegate.list(key));
    }
}
