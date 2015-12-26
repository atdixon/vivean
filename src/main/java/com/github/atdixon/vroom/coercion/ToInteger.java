package com.github.atdixon.vroom.coercion;

import com.github.atdixon.vroom2.vutil;

import java.lang.reflect.Type;

public final class ToInteger implements Coercion<Integer> {

    @Override
    public Integer coerce(Type type, Object value) throws CannotCoerceException {
        vutil.insist(type == int.class || type == Integer.class);
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.valueOf((String) value);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new CannotCoerceException(type, value);
    }

}
