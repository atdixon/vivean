package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.CanToMap;
import org.pcollections.PMap;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
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

    private final Map<String, ?> map;

    private VMap(Map<String, ?> map) {
        this.map = new HashMap<>(map);
    }

    // -- producers --

    public VMap assoc(String key, VMap val) {
        return assoc(key, val.toMap());
    }

    @SuppressWarnings("unchecked")
    public VMap assoc(String key, Object val) {
        final Object useVal = Shrink.shrinkToNull(val);
        if (useVal == null) {
            if (map.get(key) == null) {
                return this;
            } else {
                return new VMap(V.one(map, PMap.class).get().minus(key));
            }
        } else {
            if (useVal.equals(map.get(key))) {
                return this;
            } else {
                return new VMap(V.one(map, PMap.class).get().plus(key, useVal));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public VMap without(String key) {
        return new VMap(V.one(map, PMap.class).get().minus(key));
    }

    @Override @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(map);
    }

    public Map<String, Object> toShrunkMap() {
        return Collections.unmodifiableMap(Shrink.shrink(map));
    }

    // -- core reads --

    @Nonnull
    public <T> Optional<T> one(String key, Class<T> as) {
        return V.one(V.get(map, key), as);
    }

    @Nonnull
    public <T> Optional<T> one(String key, TypeSupplier<T> as) {
        return V.one(V.get(map, key), as);
    }

    @Nonnull
    public <T> List<T> many(String key, Class<T> as) {
        return V.many(V.get(map, key), as);
    }

    @Nonnull
    public <T> List<T> many(String key, TypeSupplier<T> as) {
        return V.many(V.get(map, key), as);
    }

    // -- nice --

    @Override
    public String toString() {
        return map.toString();
    }

}
