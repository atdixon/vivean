package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.util.Date;

public final class ToDate implements Coercion<Date> {

    @Override
    public Date coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
