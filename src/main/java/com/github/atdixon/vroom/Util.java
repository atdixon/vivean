package com.github.atdixon.vroom;

import java.util.Collection;

final class Util {

    private Util() {}

    static <T> T notNull(T value) {
        if (value == null)
            throw new IllegalStateException();
        return value;
    }

    static <T> T notNull(T value, String message) {
        if (value == null)
            throw new IllegalStateException(message);
        return value;
    }

    static void insist(boolean condition) {
        if (!condition)
            throw new IllegalStateException();
    }

    static void insist(boolean condition, String message) {
        if (!condition)
            throw new IllegalStateException(message);
    }

    static boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

}
