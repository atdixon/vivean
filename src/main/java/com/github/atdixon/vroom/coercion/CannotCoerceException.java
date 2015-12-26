package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class CannotCoerceException extends RuntimeException {

    private final Type type;
    private final Object value;

    public CannotCoerceException(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type type() {
        return type;
    }

    public Object value() {
        return value;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
