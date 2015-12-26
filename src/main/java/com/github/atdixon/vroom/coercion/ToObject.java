package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToObject implements Coercion<Object> {

    @Override
    public Object coerce(Type type, Object value) throws FastCannotCoerceException {
        return value;
    }

}
