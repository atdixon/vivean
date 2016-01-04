package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.Calendar;

public final class ToSqlTime implements Coercion<java.sql.Time> {

    @Override
    public Time coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof Time) {
            return (Time) value;
        }
        if (value instanceof java.util.Date) {
            return new Time(((java.util.Date) value).getTime());
        }
        if (value instanceof Calendar) {
            return new Time(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Number) {
            return new Time(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return Time.valueOf((String) value);
            } catch (IllegalArgumentException e) {
                try {
                    return new Time(Long.parseLong((String) value));
                } catch (NumberFormatException e2) {
                    throw new FastCannotCoerceException(type, value, e);
                }
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
