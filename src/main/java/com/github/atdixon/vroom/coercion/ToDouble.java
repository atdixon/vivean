package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToDouble implements Coercion<Double> {

    @Override
    public Double coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
