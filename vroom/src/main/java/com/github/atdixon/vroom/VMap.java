package com.github.atdixon.vroom;

import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;
import com.github.atdixon.vroom.coercion.Coercion;
import com.github.atdixon.vroom.coercion.CoercionRegistry;
import com.github.atdixon.vroom.coercion.FastCannotCoerceException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class VMap {

    static {
        CoercionRegistry.register(new Coercion() {
            @SuppressWarnings("unchecked") @Nullable @Override
            public Object coerce(Type type, Object value) throws FastCannotCoerceException {
                if (value.getClass() == VMap.class)
                    return value;
                if (value instanceof Map)
                    return VMap.create((Map<String, Object>) value);
                throw new FastCannotCoerceException(type, value); }}, VMap.class);
    }

    // static factory methods

    public static VMap create() {
        return new VMap(Collections.emptyMap());
    }

    public static VMap create(Map<String, ?> map) {
        return new VMap(map);
    }

    // state

    private final Map<String, ?> map;

    private VMap(Map<String, ?> map) {
        this.map = map;
    }

    // producers

    @SuppressWarnings("unchecked")
    public VMap assoc(String key, Object val) {
        final IPersistentMap r = (map instanceof IPersistentMap)
            ? (IPersistentMap) map : PersistentHashMap.create(map);
        if (val == null) {
            if (r.valAt(key) == null) {
                return this;
            } else {
                return new VMap((Map<String, ?>) r.without(key));
            }
        } else {
            if (val.equals(r.valAt(key))) {
                return this;
            } else {
                return new VMap((Map<String, ?>) r.assoc(key, val));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public VMap without(String key) {
        final IPersistentMap r = (map instanceof IPersistentMap)
            ? (IPersistentMap) map : PersistentHashMap.create(map);
        return new VMap((Map<String, ?>) r.without(key));
    }

    // ...

    /** Answer immutable/unmodifiable {@link Map}. todo shrink */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(map);
    }

    // core reads

    public <T> boolean knows(String key, Class<T> as) {
        return V.knows(V.get(map, key), as);
    }

    public <T> boolean knows(String key, TypeSupplier<T> as) {
        return V.knows(V.get(map, key), as);
    }

    public <T> void one(String key, Class<T> as, Consumer<? super T> consumer) {
        V.one(V.get(map, key), as, consumer);
    }

    public <T> void one(String key, TypeSupplier<T> as, Consumer<? super T> consumer) {
        V.one(V.get(map, key), as, consumer);
    }

    @Nonnull
    public <T> T one(String key, Class<T> as) throws NotKnownException {
        try {
            return V.one(V.get(map, key), as);
        } catch (CannotCoerceException e) {
            throw new NotKnownException(key, e);
        }
    }

    @Nonnull
    public <T> T one(String key, TypeSupplier<T> as) throws NotKnownException {
        try {
            return V.one(V.get(map, key), as);
        } catch (CannotCoerceException e) {
            throw new NotKnownException(key, e);
        }
    }

    /** Nullable. */
    public <T> T oneOr(String key, Class<T> as, @Nullable T default_) {
        return V.oneOr(V.get(map, key), as, default_);
    }

    /** Nullable. */
    public <T> T oneOr(String key, TypeSupplier<T> as, @Nullable T default_) {
        return V.oneOr(V.get(map, key), as, default_);
    }

    @Nonnull
    public <T> List<T> many(String key, Class<T> as) {
        return V.many(V.get(map, key), as);
    }

    @Nonnull
    public <T> List<T> many(String key, TypeSupplier<T> as) {
        return V.many(V.get(map, key), as);
    }

    // niceties

    @Override
    public String toString() {
        return map.toString();
    }

}
