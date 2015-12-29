package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.CoercionRegistry;
import com.github.atdixon.vroom.coercion.Containers;
import org.pcollections.PMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Omit null values. Unwrap singleton values from their collections. */
public final class Shrink {

    private Shrink() {}

    @Nonnull
    public static Map<String, Object> shrink(@Nonnull Map<String, ?> map) {
        return internalShrink(map, new HashSet<>());
    }

    @Nullable @SuppressWarnings("unchecked")
    public static Object shrinkToNull(Object value) {
        return internalShrinkToNull(value, new HashSet<>());
    }

    @Nonnull @SuppressWarnings("unchecked")
    private static Map<String, Object> internalShrink(@Nonnull Map<String, ?> map, Set<Object> seen) {
        final PMap<String, Object> asPMap = CoercionRegistry.coerce(PMap.class, map);
        PMap<String, Object> answer = asPMap;
        for (Map.Entry<String, ?> e : asPMap.entrySet()) {
            Object s = internalShrinkToNull(e.getValue(), seen);
            if (s != null)
                answer = answer.plus(e.getKey(), s);
        }
        return answer;
    }

    @Nullable @SuppressWarnings("unchecked")
    private static Object internalShrinkToNull(Object value, Set<Object> seen) {
        if (value == null) {
            return null;
        } else if (value instanceof Map) {
            if (!seen.add(value))
                throw new RuntimeException("recursive data structure");
            final Map<String, Object> shrunk = internalShrink((Map<String, ?>) value, seen);
            return shrunk.isEmpty() ? null : shrunk;
        } else if (Containers.isContainer(value)) {
            if (!seen.add(value))
                throw new RuntimeException("recursive data structure");
            final List<Object> ss = new LinkedList<>();
            Containers.forEach(value, item -> {
                Object s = internalShrinkToNull(item, seen);
                if (s != null)
                    ss.add(s);
            });
            if (ss.isEmpty())
                return null;
            else if (1 == ss.size())
                return ss.get(0);
            else if (Containers.size(value) == ss.size())
                return value;
            else
                return ss;
        } else {
            return value;
        }
    }

}
