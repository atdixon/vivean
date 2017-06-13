package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;

public final class ToInteger implements Coercion<Integer> {

    @Override
    public Integer coerce(Type type, Object value) throws FastCannotCoerceException {
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
        throw new FastCannotCoerceException(type, value);
    }

}
