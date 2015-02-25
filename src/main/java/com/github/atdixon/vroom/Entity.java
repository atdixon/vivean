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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.github.atdixon.vroom.Util.notNull;

@SuppressWarnings("unchecked")
public final class Entity {

    public static Entity empty() {
        return new Entity(new HashMap<String, Object>());
    }

    public static Entity adapt(Map<String, Object> map) {
        return new Entity(map);
    }

    public static <T> T adapt(Map<String, Object> entity, Class<T> type, Class<?>... types) {
        return ProxyFactory.adapt(new Entity(entity), type, types);
    }

    private final Map<String, Object> asMap;

    private Entity(Map<String, Object> asMap) {
        this.asMap = notNull(asMap);
    }

    public Map<String, Object> asMap() {
        return asMap;
    }

    public boolean has(String key, Class<?> asType) {
        return getOne(key, asType, null) != null;
    }

    @Nullable
    public <T> T orNull(String key, Class<T> type) {
        return getOne(key, type, null);
    }

    public <T> Optional<T> orOptional(String key, Class<T> type) {
        return Optional.fromNullable(getOne(key, type, null));
    }

    public String getString(String key) throws CannotCoerceException {
        return getOne(key, String.class);
    }

    public boolean getBoolean(String key) throws CannotCoerceException {
        return getOne(key, boolean.class);
    }

    public float getFloat(String key) throws CannotCoerceException {
        return getOne(key, float.class);
    }

    public double getDouble(String key) throws CannotCoerceException {
        return getOne(key, double.class);
    }

    public int getInt(String key) throws CannotCoerceException {
        return getOne(key, int.class);
    }

    public Map<String, Object> getMap(String key) throws CannotCoerceException {
        return getOne(key, Map.class);
    }

    public Entity getEntity(String key) throws CannotCoerceException {
        return getOne(key, Entity.class);
    }

    public <T> T getOne(String key, Class<T> type) {
        return notNull(getOne(key, type, null));
    }

    public <T> T getOne(String key, Class<T> type, T def) {
        Util.insist(!Util.isCollectionType(type));
        Object val = get(key);
        if (val instanceof Collection) {
            val = ((Collection) val).isEmpty()
                ? null : ((Collection) val).iterator().next();
        }
        return Coercions.to(val, type, def);
    }

    public List<Entity> getEntities(String key) {
        return getMany(key, Entity.class);
    }

    public <T> List<T> getMany(String key, Class<T> type) {
        Util.insist(!Util.isCollectionType(type));
        final List<T> answer = new ArrayList<T>();
        final Object value = get(key);
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

    /** Get and also support dot.expressions. Note that any literal key with
     * a dot in it is preferred for answer before 'navigating' dot. */
    private Object get(String key) {
        final String[] parts = key.split("\\.");
        Map<String, Object> curr = asMap;
        for (int i = 0; i < parts.length - 1; ++i) {
            if (curr.containsKey(join(parts, i, '.'))) { // prefer keys containing dots if they exist
                return curr.get(join(parts, i, '.'));
            } else if (curr.get(parts[i]) instanceof Map) {
                curr = (Map<String, Object>) curr.get(parts[i]);
            } else if (curr.get(parts[i]) instanceof Iterable) {
                final Iterable asIterable = (Iterable) curr.get(parts[i]);
                final Iterator asIterator = asIterable.iterator();
                if (asIterator.hasNext()) {
                    final Object maybeSingleton = asIterator.next();
                    if (!asIterator.hasNext() && maybeSingleton instanceof Map) {
                        curr = (Map<String, Object>) maybeSingleton;
                    } else {
                        // not a singleton collection or singleton but not a map
                        return null;
                    }
                }
            } else {
                return null; // no such path.
            }
        }
        return curr.get(parts[parts.length - 1]);
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

    private static String join(String[] arr, int begin, char sep) {
        final StringBuilder buf = new StringBuilder();
        for (int i = begin; i < arr.length; ++i) {
            if (i != begin)
                buf.append(sep);
            buf.append(arr[i]);
        }
        return buf.toString();
    }

}
