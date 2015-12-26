package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToByte implements Coercion<Byte> {

    @Override
    public Byte coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
