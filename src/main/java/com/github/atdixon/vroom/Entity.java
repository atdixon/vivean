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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Entity {

    public static Entity adapt(Map<String, Object> map) {
        return new Entity(map);
    }

    public static <T> T adapt(Map<String, Object> entity, Class<T> type, Class<?>... types) {
        return ProxyFactory.adapt(new Entity(entity), type, types);
    }

    private final Map<String, Object> asMap;

    private Entity(Map<String, Object> asMap) {
        this.asMap = Util.notNull(asMap);
    }

    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(asMap);
    }

    public boolean has(String key, Class<?> asType) {
        return getOne(key, asType, null) != null;
    }

    public <T> T orNull(String key, Class<T> type) {
        return getOne(key, type, null);
    }

    public String requireString(String key) throws CannotCoerceException {
        return Coercions.toString(asMap.get(key));
    }

    public boolean requireBoolean(String key) throws CannotCoerceException {
        return Coercions.toBoolean(asMap.get(key));
    }

    public float requireFloat(String key) throws CannotCoerceException {
        return Coercions.toFloat(asMap.get(key));
    }

    public double requireDouble(String key) throws CannotCoerceException {
        return Coercions.toDouble(asMap.get(key));
    }

    public int requireInt(String key) throws CannotCoerceException {
        return Coercions.toInteger(asMap.get(key));
    }

    @SuppressWarnings("unchecked")
    public <T> T getOne(String key, Class<T> type, T def) {
        Util.insist(!Util.isCollectionType(type));
        Object val = asMap.get(key);
        if (val instanceof Collection) {
            val = ((Collection) val).isEmpty()
                ? null : ((Collection) val).iterator().next();
        }
        return Coercions.to(val, type, def);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getMany(String key, Class<T> type) {
        Util.insist(!Util.isCollectionType(type));
        final List<T> answer = new ArrayList<T>();
        final Object value = asMap.get(key);
        if (value == null) {
            return answer;
        } else if (type == Boolean.class) {
            if (value instanceof Collection) {
                for (Object i : (Collection) value) {
                    addNotNull(answer, (T) Coercions.toBooleanOr(i, null));
                }
            } else {
                addNotNull(answer, (T) Coercions.toBooleanOr(value, null));
            }
        } else if (type == Integer.class) {
            if (value instanceof Collection) {
                for (Object i : (Collection) value) {
                    addNotNull(answer, (T) Coercions.toIntegerOr(i, null));
                }
            } else {
                addNotNull(answer, (T) Coercions.toIntegerOr(value, null));
            }
        } else if (type == Float.class) {
            if (value instanceof Collection) {
                for (Object i : (Collection) value) {
                    addNotNull(answer, (T) Coercions.toFloatOr(i, null));
                }
            } else {
                addNotNull(answer, (T) Coercions.toFloatOr(value, null));
            }
        } else if (type == Double.class) {
            if (value instanceof Collection) {
                for (Object i : (Collection) value) {
                    addNotNull(answer, (T) Coercions.toDoubleOr(i, null));
                }
            } else {
                addNotNull(answer, (T) Coercions.toDoubleOr(value, null));
            }
        } else if (type == String.class) {
            if (value instanceof Collection) {
                for (Object i : (Collection) value) {
                    addNotNull(answer, (T) Coercions.toStringOr(i, null));
                }
            } else {
                addNotNull(answer, (T) Coercions.toStringOr(value, null));
            }
        } else if (type == Map.class || type == Entity.class || type.isInterface()) {
            if (value instanceof Collection) {
                for (Object i : (Collection) value) {
                    addNotNull(answer, Coercions.to(i, type, null));
                }
            } else {
                addNotNull(answer, Coercions.to(value, type, null));
            }
        } else {
            throw new UnsupportedOperationException(); // unsupported type
        }
        return answer;
    }

    @Override
    public int hashCode() {
        return asMap.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Entity.class
            && asMap.equals(((Entity) obj).asMap);
    }

    @Override
    public String toString() {
        return "Entity(" + asMap.toString() + ")";
    }

    private static <T> void addNotNull(List<T> list, T value) {
        if (value != null) {
            list.add(value);
        }
    }

}
