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
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class TemporaryStorage<E extends AbstractEntity> implements Storage<E> {

    Logger logger = LoggerFactory.getLogger(TemporaryStorage.class);
    Map<Comparable, E> keyMap = new TreeMap();
    Map<Long, E> idMap = new TreeMap();
    long idSequence = 1;
    
    @Override
    public void insert(E entity) throws StorageException {
        logger.info("insert {} {}", entity.getKey(), !keyMap.containsKey(entity.getKey()));
        if (keyMap.put(entity.getKey(), entity) != null) {
            throw new StorageException(StorageExceptionType.ALREADY_EXISTS, entity.getKey());
        }
        if (entity instanceof AbstractIdEntity) {
            AbstractIdEntity idEntity = (AbstractIdEntity) entity;
            idEntity.setId(idSequence++);
            if (idMap.put(idEntity.getId(), entity) != null) {
                throw new StorageException(StorageExceptionType.ALREADY_EXISTS, idEntity.getId());
            }
        }
    }

    @Override
    public void update(E entity) throws StorageException {
        logger.info("update {} {}", entity.getKey(), keyMap.containsKey(entity.getKey()));
        if (keyMap.put(entity.getKey(), entity) == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, entity.getKey());            
        }
    }

    @Override
    public boolean containsKey(Comparable key) {
        logger.debug("containsKey {}", key, keyMap.containsKey(key));
        return keyMap.containsKey(key);
    }
    
    @Override
    public void delete(Comparable key) throws StorageException {
        logger.info("delete {} {}", key, keyMap.containsKey(key));
        if (keyMap.remove(key) != null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, key);           
        }
    }

    @Override
    public E select(Comparable key) {
        logger.info("select {} {}", key, keyMap.containsKey(key));
        return keyMap.get(key);
    }

    @Override
    public E find(Comparable key) throws StorageException {
        E entity = select(key);
        if (entity == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, key);           
        }
        return entity;
    }

    @Override
    public E findId(Long id) throws StorageException {
        E entity = idMap.get(id);
        if (entity == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, id);           
        }
        return entity;
    }
    
    @Override
    public Collection<E> selectCollection(Enum queryType, Object ... parameters) {
        if (parameters.length == 0) {
            
        }
        return keyMap.values();
    }
}
