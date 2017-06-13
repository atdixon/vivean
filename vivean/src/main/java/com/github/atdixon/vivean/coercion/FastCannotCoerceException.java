package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;

public final class FastCannotCoerceException extends RuntimeException {

    private final Type type;
    private final Object value;

    public FastCannotCoerceException(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public FastCannotCoerceException(Type type, Object value, Throwable cause) {
        super(cause);
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
