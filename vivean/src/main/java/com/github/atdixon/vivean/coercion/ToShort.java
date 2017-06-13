package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;

public final class ToShort implements Coercion<Short> {

    @Override
    public Short coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (value instanceof Short) {
            return (Short) value;
        } else if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        if (value instanceof String) {
            try {
                return Short.valueOf((String) value);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new FastCannotCoerceException(type, value);
    }

}
