package com.github.atdixon.vroom;

import clojure.lang.IPersistentMap;
import com.github.atdixon.vroom.coercion.CoercionKilt;
import com.github.atdixon.vroom.coercion.FastCannotCoerceException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class V {

    private V() {}

    public static <T> boolean knows(Object value, Class<T> as) {
        return null != one(value, as, (T) null);
    }

    public static <T> boolean knows(Object value, TypeReference<T> as) {
        return null != one(value, as.getType(), (T) null);
    }

    @Nonnull
    public static <T> T one(Object value, Class<T> as) throws CannotCoerceException {
        return one(value, (Type) as);
    }

    @Nonnull
    public static <T> T one(Object value, TypeReference<T> as) throws CannotCoerceException {
        return one(value, as.getType());
    }

    /** Nullable. */
    public static <T> T one(Object value, Class<T> as, @Nullable T default_) {
        return one(value, (Type) as, default_);
    }

    /** Nullable. */
    public static <T> T one(Object value, TypeReference<T> as, @Nullable T default_) {
        return one(value, as.getType(), default_);
    }

    public static <T> void one(Object value, Class<T> as, Consumer<? super T> consumer) {
        final T one = one(value, as, (T) null);
        if (one != null)
            consumer.accept(one);
    }

    public static <T> void one(Object value, TypeReference<T> as, Consumer<? super T> consumer) {
        final T one = one(value, as, (T) null);
        if (one != null)
            consumer.accept(one);
    }

    @Nonnull
    public static <T> List<T> many(Object value, Class<T> as) {
        return one(value, createParameterizedType(List.class, as));
    }

    @Nonnull
    public static <T> List<T> many(Object value, TypeReference<T> as) {
        return one(value, createParameterizedType(List.class, as.getType()));
    }

    private static <T> T one(Object value, Type as) throws CannotCoerceException {
        try {
            final T coerced = CoercionKilt.coerce(as, value);
            if (coerced == null)
                throw new CannotCoerceException(as, value);
            return coerced;
        } catch (FastCannotCoerceException e) {
            throw new CannotCoerceException(e);
        }
    }

    private static <T> T one(Object value, Type as, T default_) {
        try {
            final T coerced = CoercionKilt.coerce(as, value);
            return coerced != null ? coerced : default_;
        } catch (FastCannotCoerceException e) {
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

    public static Object get(Map<String, Object> map, String key) {
        return get(adapt(map), key);
    }

    /*package*/ static Object get(IPersistentMap/*<String,Object>*/ map, String key) {
        return get(adapt(map), key);
    }

    /** Get and also support dot.expressions. Note that any literal key with
     * a dot in it is preferred for answer before 'navigating' dot. */
    private static Object get(MapLike map, String key) {
        final String[] parts = key.split("\\.");
        MapLike curr = map; // invariant: curr = next, if next is a map.
        Object next = null; // invariant: next found value
        int i = 0; // inv: curr index into parts
        for (;;) {
            // prefer keys containing dots if they exist
            // note: we're not optimized for long dot paths (we're n^2); if this
            // becomes important (for long dot path access use cases, we can pre
            // index map.)
            for (int j = parts.length; j > i; --j) {
                final String path = join(parts, i, j, '.');
                next = curr.get(path);
                if (curr.containsKey(path)) {
                    i = j;
                    break; // keep next and break from j loop.
                }
            }
            if (i == parts.length) {
                return next;
            }
            // assert: if no next -> next == null
            if (knows(next, Map.class)) {
                curr = adapt(one(next, new TypeReference<Map<String, Object>>() {}));
            } else {
                return null; // no such paths
            }
        }
    }

    private static String join(String[] arr, int begin, int end, char sep) {
        if (end > arr.length)
            throw new IllegalStateException();
        final StringBuilder buf = new StringBuilder();
        for (int i = begin; i < end; ++i) {
            if (i != begin)
                buf.append(sep);
            buf.append(arr[i]);
        }
        return buf.toString();
    }

    private static MapLike adapt(final IPersistentMap/*<String,Object>*/ map) {
        return new MapLike() {
            @Override public Object get(String key) {
                return map.valAt(key);
            }
            @Override public boolean containsKey(String key) {
                return map.containsKey(key);
            }};
    }

    private static MapLike adapt(final Map<String, Object> map) {
        return new MapLike() {
            @Override public Object get(String key) {
                return map.get(key);
            }
            @Override public boolean containsKey(String key) {
                return map.containsKey(key);
            }};
    }

    /** Internal. */
    private interface MapLike {
        Object get(String key);
        boolean containsKey(String key);
    }

}
