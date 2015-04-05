/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.atdixon.vroom;

import com.google.common.base.Optional;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.atdixon.vroom.Util.notNull;

@SuppressWarnings("unchecked")
public final class VMap {

    public static VMap empty() {
        return new VMap(new HashMap<String, Object>());
    }

    public static VMap of(Map<String, Object> map) {
        return new VMap(map);
    }

    private final Map<String, Object> map;

    private VMap(Map<String, Object> map) {
        this.map = notNull(map);
    }

    public Map<String, Object> asMap() {
        return map;
    }

    public boolean knows(String key, Class<?> asType) {
        return this.one(key, asType, null) != null;
    }

    @Nullable
    public <T> T oneOrNull(String key, Class<T> type) {
        return this.one(key, type, null);
    }

    public <T> Optional<T> oneOptional(String key, Class<T> type) {
        return Optional.fromNullable(this.one(key, type, null));
    }

    public String oneString(String key) throws CannotCoerceException {
        return one(key, String.class);
    }

    public boolean oneBoolean(String key) throws CannotCoerceException {
        return one(key, boolean.class);
    }

    public float oneFloat(String key) throws CannotCoerceException {
        return one(key, float.class);
    }

    public double oneDouble(String key) throws CannotCoerceException {
        return one(key, double.class);
    }

    public int oneInt(String key) throws CannotCoerceException {
        return one(key, int.class);
    }

    public long oneLong(String key) throws CannotCoerceException {
        return one(key, long.class);
    }

    public Map<String, Object> oneMap(String key) throws CannotCoerceException {
        return one(key, Map.class);
    }

    public VMap oneVMap(String key) throws CannotCoerceException {
        return one(key, VMap.class);
    }

    public List<VMap> getEntities(String key) {
        return many(key, VMap.class);
    }

    public <T> T one(String key, Class<T> t) {
        return V.one(map, key, t);
    }

    public <T> T one(String key, Class<T> t, T def) {
        return V.one(map, key, t, def);
    }

    public <T> List<T> many(String key, Class<T> t) {
        return V.many(map, key, t);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == VMap.class
            && map.equals(((VMap) obj).map);
    }

    @Override
    public String toString() {
        return "VMap(" + map.toString() + ")";
    }

}
