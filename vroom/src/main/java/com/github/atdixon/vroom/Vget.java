package com.github.atdixon.vroom;

import java.util.Map;
import java.util.function.Consumer;

public final class Vget {

    private Vget() {}

    public static String oneString(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), String.class);
    }

    public static String oneStringOr(Map<String, ?> map, String key, String default_) {
        return V.oneOr(V.get(map, key), String.class, default_);
    }

    public static void oneString(Map<String, ?> map, String key, Consumer<String> c) {
        V.one(V.get(map, key), String.class, c);
    }

    public static boolean oneBoolean(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), boolean.class);
    }

    public static boolean oneBooleanOr(Map<String, ?> map, String key, boolean default_) {
        return V.oneOr(V.get(map, key), boolean.class, default_);
    }

    public static void oneBoolean(Map<String, ?> map, String key, Consumer<Boolean> c) {
        V.one(V.get(map, key), boolean.class, c);
    }

    public static byte oneByte(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), byte.class);
    }

    public static byte oneByteOr(Map<String, ?> map, String key, byte default_) {
        return V.oneOr(V.get(map, key), byte.class, default_);
    }

    public static void oneByte(Map<String, ?> map, String key, Consumer<Byte> c) {
        V.one(V.get(map, key), byte.class, c);
    }

    public static short oneShort(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), short.class);
    }

    public static short oneShortOr(Map<String, ?> map, String key, short default_) {
        return V.oneOr(V.get(map, key), short.class, default_);
    }

    public static void oneShort(Map<String, ?> map, String key, Consumer<Short> c) {
        V.one(V.get(map, key), short.class, c);
    }

    public static int oneInt(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), int.class);
    }

    public static int oneIntOr(Map<String, ?> map, String key, int default_) {
        return V.oneOr(V.get(map, key), int.class, default_);
    }

    public static void oneInt(Map<String, ?> map, String key, Consumer<Integer> c) {
        V.one(V.get(map, key), int.class, c);
    }

    public static long oneLong(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), long.class);
    }

    public static long oneLongOr(Map<String, ?> map, String key, long default_) {
        return V.oneOr(V.get(map, key), long.class, default_);
    }

    public static void oneLong(Map<String, ?> map, String key, Consumer<Long> c) {
        V.one(V.get(map, key), long.class, c);
    }

    public static float oneFloat(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), float.class);
    }

    public static float oneFloatOr(Map<String, ?> map, String key, float default_) {
        return V.oneOr(V.get(map, key), float.class, default_);
    }

    public static void oneFloat(Map<String, ?> map, String key, Consumer<Float> c) {
        V.one(V.get(map, key), float.class, c);
    }

    public static double oneDouble(Map<String, ?> map, String key) {
        return V.one(V.get(map, key), double.class);
    }

    public static double oneDoubleOr(Map<String, ?> map, String key, double default_) {
        return V.oneOr(V.get(map, key), double.class, default_);
    }

    public static void oneDouble(Map<String, ?> map, String key, Consumer<Double> c) {
        V.one(V.get(map, key), double.class, c);
    }

}
