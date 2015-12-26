package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToLong implements Coercion<Long> {

    @Override
    public Long coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
