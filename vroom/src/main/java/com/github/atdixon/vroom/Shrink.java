package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.Containers;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** Omit null values. Unwrap singleton values from their collections. */
public final class Shrink {

    private Shrink() {}

    @Nonnull
    public static Map<String, Object> shrink(@Nonnull Map<String, ?> map) {
        PMap<String, Object> answer = HashTreePMap.from(Collections.emptyMap());
        for (Map.Entry<String, ?> e : map.entrySet()) {
            Object s = shrinkToNull(e.getValue());
            if (s != null)
                answer = answer.plus(e.getKey(), s);
        }
        return answer;
    }

    @Nullable @SuppressWarnings("unchecked")
    public static Object shrinkToNull(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Map) {
            final Map<String, Object> shrunk = shrink((Map<String, ?>) value);
            return shrunk.isEmpty() ? null : shrunk;
        } else if (Containers.isContainerValue(value)) {
            final List<Object> ss = new LinkedList<>();
            Containers.forEach(value, item -> {
                Object s = shrinkToNull(item);
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
