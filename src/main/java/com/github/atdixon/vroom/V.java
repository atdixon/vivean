package com.github.atdixon.vroom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.Map;

import static com.github.atdixon.vroom.Util.insist;
import static com.github.atdixon.vroom.Util.notNull;

public final class V {

    private V() {}

    // map lookups

    public static <T> T one(Map<String, Object> map, String key, Class<T> t) {
        return one(get(map, key), t);
    }

    public static <T> T one(Map<String, Object> map, String key, Class<T> t, T def) {
        return one(get(map, key), t, def);
    }

    public static <T> List<T> many(Map<String, Object> map, String key, Class<T> t) {
        return many(get(map, key), t);
    }

    // value conversions

    /** Can value be viewed as at least one/non-empty t? */
    public static <T> boolean is(Object o, Class<T> t) {
        return null != one(o, t, null);
    }

    public static <T> T one(Object o, Class<T> t) {
        insist(!Util.isCollectionType(t));
        return notNull(one(o, t, null));
    }

    public static <T> T one(Object o, Class<T> t, T def) {
        insist(!Util.isCollectionType(t));
        if (o instanceof Iterable) {
            o = Iterables.getFirst((Iterable) o, null);
        } else if (o instanceof Object[]) {
            o = ((Object[]) o).length > 0 ? ((Object[]) o)[0] : null;
        }
        return Coercions.to(o, t, def);
    }

    public static <T> List<T> many(Object o, Class<T> t) {
        insist(!Util.isCollectionType(t));
        final ImmutableList.Builder<T> answer = ImmutableList.builder();
        if (o instanceof Iterable) {
            for (Object i : (Iterable) o) {
                addNotNullOrEmpty(answer, Coercions.to(i, t, null));
            }
        } else if (o instanceof Object[]) {
            for (Object i : (Object[]) o) {
                addNotNullOrEmpty(answer, Coercions.to(i, t, null));
            }
        } else if (o != null) {
            addNotNullOrEmpty(answer, Coercions.to(o, t, null));
        }
        return answer.build();
    }

    // factories

    public static <T> T proxy(Map<String, Object> m, Class<T> t, Class<?>... ts) {
        return ProxyFactory.adapt(m, t, ts);
    }

    // internal

    /** Get and also support dot.expressions. Note that any literal key with
     * a dot in it is preferred for answer before 'navigating' dot. */
    @SuppressWarnings("unchecked")
    private static Object get(Map<String, Object> map, String key) {
        final String[] parts = key.split("\\.");
        Map<String, Object> curr = map; // invariant: curr = next, if next is a map.
        Object next = null; // invariant: next found value
        int i = 0; // inv: curr index into parts
        for (;;) {
            // prefer keys containing dots if they exist
            // note: we're not optimized for long dot paths (we're n^2); if this
            // becomes important (for long dot path access use cases, we can pre
            // index map.)
            for (int j = parts.length; j > i; --j) {
                next = curr.get(join(parts, i, j, '.'));
                if (curr.containsKey(join(parts, i, j, '.'))) {
                    i = j;
                    break; // keep next and break from j loop.
                }
            }
            if (i == parts.length) {
                return next;
            }
            // assert: if no next -> next == null
            if (is(next, Map.class)) {
                curr = (Map<String, Object>) one(next, Map.class);
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

    private static <T> void addNotNullOrEmpty(ImmutableList.Builder<T> list, T value) {
        if (value instanceof Map && !((Map) value).isEmpty() || value != null) {
            list.add(value);
        }
    }

}
