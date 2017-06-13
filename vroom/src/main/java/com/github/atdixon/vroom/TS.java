package com.github.atdixon.vroom;

import java.util.Map;

/** Type factories. */
public final class TS {

    private TS() {}

    public static TypeSupplier<Map<String, Object>> Map() {
        return Map(Object.class);
    }

    public static <V> TypeSupplier<Map<String, V>> Map(Class<V> valType) {
        return () -> new ParameterizedTypeImpl(null, Map.class, String.class, valType);
    }

}
