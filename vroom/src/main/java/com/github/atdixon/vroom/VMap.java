package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.CanToMap;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class VMap implements CanToMap<String, Object> {

    // -- factory --

    public static VMap create() {
        return new VMap(Collections.emptyMap());
    }

    public static VMap create(Map<String, ?> map) {
        return new VMap(map);
    }

    // -- state --

    private final PMap<String, Object> pmap;

    private VMap(Map<String, ?> map) {
        this.pmap = map instanceof PMap ? (PMap) map : HashTreePMap.from(map);
    }

    // -- producers --

    @SuppressWarnings("unchecked")
    public VMap assoc(String key, Object val) {
        final Object useVal = Shrink.shrinkToNull(val);
        return useVal == null
            ? new VMap(pmap.minus(key))
            : new VMap(pmap.plus(key, useVal));
    }

    @SuppressWarnings("unchecked")
    public VMap without(String key) {
        return new VMap(pmap.minus(key));
    }

    @Override @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        return pmap; // unmodifiable.
    }

    // -- core reads --

    @Nonnull
    public <T> Optional<T> one(String key, Class<T> as) {
        return V.one(V.get(pmap, key), as);
    }

    @Nonnull
    public <T> Optional<T> one(String key, TypeSupplier<T> as) {
        return V.one(V.get(pmap, key), as);
    }

    @Nonnull
    public <T> List<T> many(String key, Class<T> as) {
        return V.many(V.get(pmap, key), as);
    }

    @Nonnull
    public <T> List<T> many(String key, TypeSupplier<T> as) {
        return V.many(V.get(pmap, key), as);
    }

    // -- nice --

    @Override
    public String toString() {
        return pmap.toString();
    }

}
