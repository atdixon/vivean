package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.Coercion;
import com.github.atdixon.vroom.coercion.CoercionRegistry;
import com.github.atdixon.vroom.coercion.FastCannotCoerceException;
import com.github.atdixon.vroom.coercion.Kilt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class V {

    static {
        // use static initializer in V to register VMap coercion b/c V class is guaranteed
        // to be loaded before coercions are attempted through V system.
        CoercionRegistry.register(new Coercion() {
            @SuppressWarnings("unchecked") @Nullable @Override
            public Object coerce(Type type, Object value) throws FastCannotCoerceException {
                if (value.getClass() == VMap.class)
                    return value;
                if (value instanceof Map)
                    return VMap.create((Map<String, Object>) value);
                throw new FastCannotCoerceException(type, value); }}, VMap.class);
    }

    private V() {}

    public static <T> boolean knows(Object value, Class<T> as) {
        return null != oneOr(value, as, null);
    }

    public static <T> boolean knows(Object value, TypeSupplier<T> as) {
        return null != oneOr(value, as.get(), null);
    }

    @Nonnull
    public static <T> T one(Object value, Class<T> as) throws CannotCoerceException {
        return (value != null && as.isAssignableFrom(value.getClass()))
            ? as.cast(value) : one(value, (Type) as);
    }

    @Nonnull
    public static <T> T one(Object value, TypeSupplier<T> as) throws CannotCoerceException {
        return one(value, as.get());
    }

    /** Nullable. */
    public static <T> T oneOr(Object value, Class<T> as, @Nullable T default_) {
        return oneOr(value, (Type) as, default_);
    }

    /** Nullable. */
    public static <T> T oneOr(Object value, TypeSupplier<T> as, @Nullable T default_) {
        return oneOr(value, as.get(), default_);
    }

    public static <T> void one(Object value, Class<T> as, Consumer<? super T> consumer) {
        final T one = oneOr(value, as, null);
        if (one != null)
            consumer.accept(one);
    }

    public static <T> void one(Object value, TypeSupplier<T> as, Consumer<? super T> consumer) {
        final T one = oneOr(value, as, null);
        if (one != null)
            consumer.accept(one);
    }

    @Nonnull
    public static <T> List<T> many(Object value, Class<T> as) {
        return many(value, () -> as);
    }

    @Nonnull @SuppressWarnings("unchecked")
    public static <T> List<T> many(Object value, TypeSupplier<T> as) {
        return one(value, new ParameterizedTypeImpl(null, List.class, as.get()));
    }

    @Nonnull
    private static <T> T one(Object value, Type as) throws CannotCoerceException {
        try {
            final T coerced = Kilt.coerce(as, value);
            if (coerced == null)
                throw new CannotCoerceException(as, value);
            return coerced;
        } catch (FastCannotCoerceException e) {
            throw new CannotCoerceException(e);
        }
    }

    /** Nullable. */
    private static <T> T oneOr(Object value, Type as, @Nullable T default_) {
        try {
            final T coerced = Kilt.coerce(as, value);
            return coerced != null ? coerced : default_;
        } catch (FastCannotCoerceException e) {
            return default_;
        }
    }

    public static Object get(Map<String, ?> map, String key) {
        return map.containsKey(key) ? /*optimization*/map.get(key) : getPath(map, key);
    }

    /** Get and also support dot.expressions. Note that any literal key with
     * a dot in it is preferred for answer before 'navigating' dot. */
    private static Object getPath(Map map, String key) {
        final String[] parts = key.split("\\.");
        Map curr = map; // invariant: curr = next, if next is a map.
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
                curr = one(next, new TypeReference<Map<String, Object>>() {});
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
}
