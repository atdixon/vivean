package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToFloat implements Coercion<Float> {

    @Override
    public Float coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
