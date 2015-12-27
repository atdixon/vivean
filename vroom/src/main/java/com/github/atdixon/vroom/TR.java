package com.github.atdixon.vroom;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Type factories. */
public final class TR {
    private TR() {}

    public static <T> TypeSupplier<Optional<T>> Optional(Class<T> type) {
        return () -> new ParameterizedTypeImpl(null, Optional.class, type);
    }

    public static <T> TypeSupplier<List<T>> List(Class<T> type) {
        return () -> new ParameterizedTypeImpl(null, List.class, type);
    }

    public static <T> TypeSupplier<List<T>> Set(Class<T> type) {
        return () -> new ParameterizedTypeImpl(null, Set.class, type);
    }

    public static TypeSupplier<Map<String, Object>> Map() {
        return () -> new ParameterizedTypeImpl(null, Map.class, String.class, Object.class);
    }

}
