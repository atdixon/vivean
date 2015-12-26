package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToShort implements Coercion<Short> {

    @Override
    public Short coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
