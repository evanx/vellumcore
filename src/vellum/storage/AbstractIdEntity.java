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

import vellum.util.Comparables;

/**
 *
 * @author evan.summers
 */
public abstract class AbstractIdEntity extends AbstractEntity {

    public abstract Long getId();
    public abstract void setId(Long id);
    
    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }
    
    @Override
    public int compareTo(AbstractEntity o) {
        if (o instanceof AbstractIdEntity) {
            AbstractIdEntity other = (AbstractIdEntity) o;
            return Comparables.compareTo(getId(), other.getId());
        }
        return super.compareTo(o);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AbstractIdEntity) {
            AbstractIdEntity other = (AbstractIdEntity) object;
            return Comparables.equals(getId(), other.getId());
        }
        return false;
    }
    
}
