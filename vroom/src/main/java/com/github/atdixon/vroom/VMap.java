package com.github.atdixon.vroom;

import clojure.lang.IPersistentMap;
import clojure.lang.MapEntry;
import clojure.lang.PersistentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class VMap {

    // static factory methods

    public static VMap create() {
        return new VMap(PersistentHashMap.create());
    }

    public static VMap create(Map<String, Object> map) {
        return new VMap(PersistentHashMap.create(map));
    }

    // state

    private final IPersistentMap/*<String,Object>*/ map;

    private VMap(IPersistentMap/*<String,Object>*/ map) {
        this.map = map;
    }

    // producers

    public VMap assoc(String key, Object val) {
        if (val == null) {
            if (map.valAt(key) == null) {
                return this;
            } else {
                return new VMap(map.without(key));
            }
        } else {
            if (val.equals(map.valAt(key))) {
                return this;
            } else {
                return new VMap(map.assoc(key, val));
            }
        }
    }

    public VMap without(String key) {
        return new VMap(map.without(key));
    }

    // ...

    /** Answer immutable/unmodifiable {@link Map}. todo shrink */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(StreamSupport.stream((Spliterator<MapEntry>) map.spliterator(), false)
            .collect(Collectors.toMap(e -> (String) e.key(), e -> (Object) e.val())));
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

    @Nonnull
    public <T> T one(String key, Class<T> as) throws NotKnownException {
        return V.one(V.get(map, key), as);
    }

    @Nonnull
    public <T> T one(String key, TypeSupplier<T> as) throws NotKnownException {
        return V.one(V.get(map, key), as);
    }

    /** Nullable. */
    public <T> T one(String key, Class<T> as, @Nullable T default_) {
        return V.one(V.get(map, key), as, default_);
    }

    /** Nullable. */
    public <T> T one(String key, TypeSupplier<T> as, @Nullable T default_) {
        return V.one(V.get(map, key), as, default_);
    }

    @Nonnull
    public <T> List<T> many(String key, Class<T> as) {
        return V.many(V.get(map, key), as);
    }

    @Nonnull
    public <T> List<T> many(String key, TypeSupplier<T> as) {
        return V.many(V.get(map, key), as);
    }

    // pretties

    // niceties

    @Override
    public String toString() {
        return map.toString();
    }

}
