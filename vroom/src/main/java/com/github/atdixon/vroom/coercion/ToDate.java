package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

public final class ToDate implements Coercion<Date> {

    @Override
    public Date coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof java.util.Date) {
            return (java.util.Date) value;
        }
        if (value instanceof Calendar) {
            return new java.util.Date(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Number) {
            return new java.util.Date(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return new java.util.Date(Long.parseLong((String) value));
            } catch (NumberFormatException e) {
                throw new FastCannotCoerceException(type, value, e);
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
