package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

public final class ToCalendar implements Coercion<Calendar> {

    @Override
    public Calendar coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return (Calendar) value;
        }
        if (value instanceof Date) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) value);
            return calendar;
        }
        if (value instanceof Number) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Number) value).longValue());
            return calendar;
        }
        if (value instanceof String) {
            try {
                final Date parsed = new Date(Long.parseLong((String) value));
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsed);
                return calendar;
            } catch (NumberFormatException e) {
                throw new FastCannotCoerceException(type, value, e);
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
