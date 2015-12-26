package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToBoolean implements Coercion<Boolean> {

    @Override
    public Boolean coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
