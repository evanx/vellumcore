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

import vellum.util.Args;

/**
 * 
 * @author evan.summers
 */
public class StorageException extends Exception {
    StorageExceptionType exceptionType;
    Class entityType;
    Comparable key;
            
    public StorageException(StorageExceptionType exceptionType) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
    }
    
    public StorageException(StorageExceptionType exceptionType, Comparable key) {
        this(exceptionType);
        this.key = key;
    }

    public StorageException(Exception cause, StorageExceptionType exceptionType) {
        super(exceptionType.name(), cause);
        this.exceptionType = exceptionType;
    }
    
    public StorageException(Exception cause, StorageExceptionType exceptionType, 
            Class entityType, Comparable key) {
        this(cause, exceptionType);
        this.key = key;
        this.entityType = entityType;
    }

    @Override
    public String getMessage() {
        return Args.format(exceptionType, entityType, key, getCause().getMessage());
    }
        
    public StorageExceptionType getExceptionType() {
        return exceptionType;
    }

    public Comparable getKey() {
        return key;
    }                

    public Class getEntityType() {
        return entityType;
    }        
}
