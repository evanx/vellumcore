/*
 Source https://github.com/evanx by @evanxsummers

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
package vellum.entity;

import java.util.Objects;

/**
 *
 * @author evan.summers
 */
public abstract class LongIdEntity implements Comparable<LongIdEntity> {

    public abstract Long getId();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LongIdEntity) {
            LongIdEntity other = (LongIdEntity) obj;
            return Objects.equals(getId(), other.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        }
        return getId().hashCode();
    }

    @Override
    public int compareTo(LongIdEntity other) {
        if (other.getId() == null || getId() == null) {
           throw new IllegalArgumentException("null entity id");
        }
        return getId().compareTo(other.getId());
    }

    @Override
    public String toString() {
        if (getId() == null) {
            return getClass().getSimpleName();
        }
        return getId().toString();
    }
}
