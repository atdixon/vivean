package com.github.atdixon.vroom2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * todo
 */
@Deprecated /** eval*/
public final class TypeReferences {

    private TypeReferences() {}

    public static <T> TypeReference<Optional<T>> optional(Class<T> type) {
        return new TypeReference<Optional<T>>() {};
    }

    public static <T> TypeReference<List<T>> list(Class<T> type) {
        return new TypeReference<List<T>>() {};
    }

    public static TypeReference<Map<String, Object>> map() {
        return new TypeReference<Map<String, Object>>() {};
    }

}
