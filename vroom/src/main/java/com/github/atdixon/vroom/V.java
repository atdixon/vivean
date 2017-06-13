package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.CoercionRegistry;
import com.github.atdixon.vroom.coercion.FastCannotCoerceException;
import com.github.atdixon.vroom.coercion.Kilt;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.atdixon.vroom.coercion.Containers.isContainer;

public final class V {

    static {
        // use static initializer in V to register VMap coercion b/c V class is guaranteed
        // to be loaded before coercions are attempted through V system.
        CoercionRegistry.register((type, value) -> {
            if (value.getClass() == VMap.class)
                return value;
            if (value instanceof Map)
                //noinspection unchecked
                return VMap.create((Map<String, ?>) value);
            throw new FastCannotCoerceException(type, value); }, VMap.class);
    }

    private V() {}

    @Nonnull
    public static <T> Optional<T> one(Object value, Class<T> as) {
        if (isContainer(as))
            throw new IllegalStateException("use many()");
        return internalOne(value, as);
    }

    @Nonnull
    public static <T> Optional<T> one(Object value, TypeSupplier<T> as) {
        if (isContainer(as.get()))
            throw new IllegalStateException("use many()");
        return internalOne(value, as.get());
    }

    @Nonnull
    public static <T> List<T> many(Object value, Class<T> as) {
        return many(value, () -> as);
    }

    @Nonnull @SuppressWarnings("unchecked")
    public static <T> List<T> many(Object value, TypeSupplier<T> as) {
        return (List<T>) internalOne(value,
            new ParameterizedTypeImpl(null, List.class, as.get())).get();
    }

    private static <T> Optional<T> internalOne(Object value, Type as) {
        try {
            final T coerced = Kilt.coerce(as, value);
            return coerced != null ? Optional.of(coerced) : Optional.empty();
        } catch (FastCannotCoerceException e) {
            return Optional.empty();
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
            if (one(next, Map.class).isPresent()) {
                curr = one(next, new TypeReference<Map<String, ?>>() {}).get();
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
