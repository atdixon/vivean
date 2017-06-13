package com.github.atdixon.vivean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Vg {

    private Vg() {}

    // -- String --

    public static Optional<String> oneString(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), String.class);
    }

    public static List<String> manyString(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), String.class);
    }

    // -- Boolean --

    public static Optional<Boolean> oneBoolean(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), boolean.class);
    }

    public static List<Boolean> manyBoolean(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), boolean.class);
    }

    // -- Byte --

    public static Optional<Byte> oneByte(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), byte.class);
    }

    public static List<Byte> manyByte(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), byte.class);
    }

    // -- Short --

    public static Optional<Short> oneShort(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), short.class);
    }

    public static List<Short> manyShort(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), short.class);
    }

    // -- Integer --

    public static Optional<Integer> oneInt(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), int.class);
    }

    public static List<Integer> manyInt(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), int.class);
    }

    // -- Long --

    public static Optional<Long> oneLong(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), long.class);
    }

    public static List<Long> manyLong(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), long.class);
    }

    // -- Float --

    public static Optional<Float> oneFloat(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), float.class);
    }

    public static List<Float> manyFloat(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), float.class);
    }

    // -- Double --

    public static Optional<Double> oneDouble(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), double.class);
    }

    public static List<Double> manyDouble(Map<String, ?> map, String key) {
        return V.many(V.get(map, key), double.class);
    }

}
