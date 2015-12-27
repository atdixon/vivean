package com.github.atdixon.vroom;

import com.github.atdixon.vroom.coercion.FastCannotCoerceException;

import java.lang.reflect.Type;

public final class CannotCoerceException extends RuntimeException {

    private final Type type;
    private final Object value;

    public CannotCoerceException(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public CannotCoerceException(FastCannotCoerceException e) {
        super(e);
        this.type = e.type();
        this.value = e.value();
    }

    public Type type() {
        return type;
    }

    public Object value() {
        return value;
    }

}
