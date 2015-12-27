package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.Calendar;

public final class ToSqlDate implements Coercion<java.sql.Date> {

    @Override
    public Date coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof java.util.Date) {
            return new Date(((java.util.Date) value).getTime());
        }
        if (value instanceof Calendar) {
            return new Date(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return Date.valueOf((String) value);
            } catch (IllegalArgumentException e) {
                try {
                    return new Date(Long.parseLong((String) value));
                } catch (NumberFormatException e2) {
                    throw new FastCannotCoerceException(type, value, e);
                }
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
