package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.util.GregorianCalendar;

public final class ToGregorianCalendar implements Coercion<GregorianCalendar> {

    @Override
    public GregorianCalendar coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
