package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Calendar;

public final class ToSqlTimestamp implements Coercion<java.sql.Timestamp> {

    @Override
    public Timestamp coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) value).getTime());
        }
        if (value instanceof Calendar) {
            return new Timestamp(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Number) {
            return new Timestamp(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return Timestamp.valueOf((String) value);
            } catch (IllegalArgumentException e) {
                try {
                    return new Timestamp(Long.parseLong((String) value));
                } catch (NumberFormatException e2) {
                    throw new FastCannotCoerceException(type, value, e);
                }
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
