package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.CanToMap;
import org.pcollections.PMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class VMap implements CanToMap<String, Object> {

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
                return new VMap(V.one(map, PMap.class).minus(key));
            }
        } else {
            if (useVal.equals(map.get(key))) {
                return this;
            } else {
                return new VMap(V.one(map, PMap.class).plus(key, useVal));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public VMap without(String key) {
        return new VMap(V.one(map, PMap.class).minus(key));
    }

    @Override @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(map);
    }

    public Map<String, Object> toShrunkMap() {
        return Collections.unmodifiableMap(Shrink.shrink(map));
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
