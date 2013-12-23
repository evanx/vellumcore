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
    Object[] parameters;
            
    public StorageException(StorageExceptionType exceptionType) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
    }
    
    public StorageException(StorageExceptionType exceptionType, Object... parameters) {
        this(exceptionType);
        this.parameters = parameters;
    }

    public StorageException(Exception cause, StorageExceptionType exceptionType) {
        super(exceptionType.name(), cause);
        this.exceptionType = exceptionType;
    }
    
    public StorageException(Exception cause, StorageExceptionType exceptionType, Object... parameters) {
        this(cause, exceptionType);
        this.parameters = parameters;
    }

    @Override
    public String getMessage() {
        return Args.format(exceptionType, getCause(), Args.format(parameters));
    }
        
    public StorageExceptionType getExceptionType() {
        return exceptionType;
    }
}
