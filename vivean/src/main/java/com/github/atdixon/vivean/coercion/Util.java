package com.github.atdixon.vivean.coercion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class Util {

    private Util() {}

    static Class rawTypeOf(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        } else {
            throw new IllegalArgumentException();
        }
    }

}
