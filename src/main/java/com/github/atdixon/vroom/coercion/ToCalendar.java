package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.util.Calendar;

public final class ToCalendar implements Coercion<Calendar> {

    @Override
    public Calendar coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
