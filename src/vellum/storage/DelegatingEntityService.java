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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.type.Enabled;
import vellum.type.Labelled;

/**
 *
 * @author evan.summers
 */
public class DelegatingEntityService<E extends AutoIdEntity> implements EntityService<E> {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingEntityService.class);
    final CachingEntityService<E> cache;
    final EntityService<E> delegate;

    public DelegatingEntityService(CachingEntityService<E> cache, EntityService<E> delegate) {
        this.cache = cache;
        this.delegate = delegate;
    }

    @Override
    public void persist(E entity) throws StorageException {
        assert (entity.getId() == null);
        synchronized (cache) {
            assert (!cache.retrievable(entity.getId()));
            delegate.persist(entity);
            cache.put(entity);
            assert (cache.contains(entity));
        }
    }

    @Override
    public void update(E entity) throws StorageException {
        delegate.update(entity);
        cache.put(entity);
    }

    @Override
    public boolean retrievable(Comparable key) throws StorageException {
        if (cache.retrievable(key)) {
            assert (delegate.retrievable(key));
            return true;
        }
        return delegate.retrievable(key);
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
            assert delegate.find(key) != null;
            return entity;
        }
        entity = delegate.find(key);
        if (entity != null) {
            cache.put(entity);
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
        return cache.list();
    }

    @Override
    public Collection<E> list(Comparable key) throws StorageException {
        Collection<E> entities = delegate.list(key);
        LinkedList list = new LinkedList();
        synchronized (cache) {
            for (E entity : entities) {
                E cachedEntity = cache.find(entity.getId());
                if (cachedEntity != null) {
                    assertEquals(cachedEntity, entity);
                    list.add(cachedEntity);
                    logger.trace("cached {}", entity);
                } else {
                    cache.put(entity);
                    list.add(entity);
                }
            }
            Collection<E> cachedList = cache.list(key);
            if (entities.size() != cachedList.size()) {
                logger.warn("list assertion {} {}", entities.size(), key);
                new Throwable().printStackTrace(System.err);
            }
        }
        return list;
    }

    private void assertEquals(E cachedEntity, E entity) {
        assert (cachedEntity.getId().equals(entity.getId()));
        assert (cachedEntity.getId().equals(entity.getId()));
        assert (cachedEntity.toString().equals(entity.toString()));
        assert (cachedEntity.hashCode() == entity.hashCode());
        assert (cachedEntity.compareTo(entity) == 0);
        if (entity instanceof Labelled) {
            assertEquals((Labelled) cachedEntity, (Labelled) entity);
        }
        if (entity instanceof Enabled) {
            assertEquals((Enabled) cachedEntity, (Enabled) entity);
        }
        for (Comparable key : cache.getMatcher().getKeys(cachedEntity)) {
            assert (cache.getMatcher().matches(key, entity));
        }
    }

    private void assertEquals(Enabled cachedEntity, Enabled entity) {
        assert (cachedEntity.isEnabled() == entity.isEnabled());
    }

    private void assertEquals(Labelled cachedEntity, Labelled entity) {
        assert (cachedEntity.getLabel().equals(entity.getLabel()));
    }
}
