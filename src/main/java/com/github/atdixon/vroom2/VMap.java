package com.github.atdixon.vroom2;

import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;
import com.github.atdixon.vroom.coercion.CannotCoerceException;
import com.github.atdixon.vroom.coercion.CoercionKilt;
import com.github.atdixon.vroom.coercion.CoercionRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class VMap {

    // todo -- caching strategy? pluggable w/ default soft/weak impl? what does integer do?
    // todo --              ** OR ** soethingl ike a proxy wrapper that caches method answers??????
    // todo             ...cache coercions but clear cache on 'mutate'/producer??
    // todo -- immutable factories or builders like guava ??

    // static factory methods

    public static VMap create() {
        return new VMap(PersistentHashMap.create());
    }

    public static VMap create(Map<String, Object> map) {
        return new VMap(PersistentHashMap.create(map));
    }

    // state

    private final IPersistentMap map;

    private VMap(IPersistentMap map) {
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

    // core reads

    public <T> boolean knows(String key, Class<T> as) {
        return null != one(key, as, (T) null);
    }

    public <T> void one(String key, Class<T> as, Consumer<? super T> consumer) {
        final T one = one(key, as, (T) null);
        if (one != null)
            consumer.accept(one);
    }

    @Nonnull
    public <T> T one(String key, Class<T> as) throws NotKnownException {
        return one(key, (Type) as);
    }

    @Nonnull
    public <T> T one(String key, TypeReference<T> as) throws NotKnownException {
        return one(key, as.getType());
    }

    @Nullable
    public <T> T one(String key, Class<T> as, @Nullable T default_) {
        return one(key, (Type) as, default_);
    }

    @Nullable
    public <T> T one(String key, TypeReference<T> as, @Nullable T default_) {
        return one(key, as.getType(), default_);
    }

    @Nonnull
    public <T> List<T> many(String key, Class<T> as) {
        return one(key, createParameterizedType(List.class, as));
    }

    @Nonnull
    public <T> List<T> many(String key, TypeReference<T> as) {
        return one(key, createParameterizedType(List.class, as.getType()));
    }

    // pretties

    // niceties

    @Override
    public String toString() {
        return String.format("VMap(%s)", map.toString()); // todo better
    }

    // internal

    @Nonnull
    private <T> T one(String key, Type as) throws NotKnownException {
        final Object value = map.valAt(key); // todo V.get
        try {
            final T coerced = CoercionKilt.coerce(as, value);
            if (coerced == null)
                throw new NotKnownException(key);
            return coerced;
        } catch (CannotCoerceException e) {
            throw new NotKnownException(key);
        }
    }

    @Nullable
    private <T> T one(String key, Type as, @Nullable T default_) {
        final Object value = map.valAt(key); // todo V.get
        try {
            final T coerced = CoercionRegistry.coerce(as, value);
            return coerced != null ? coerced : default_;
        } catch (CannotCoerceException e) {
            return default_;
        }
    }

    private static ParameterizedType createParameterizedType(final Type rawType, final Type... actualTypeArguments) {
        return new ParameterizedType() {
            @Override public Type[] getActualTypeArguments() {
                return actualTypeArguments;
            }
            @Override public Type getRawType() {
                return rawType;
            }
            @Override public Type getOwnerType() {
                return null; }};
    }

}
