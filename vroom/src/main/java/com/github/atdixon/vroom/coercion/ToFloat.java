package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToFloat implements Coercion<Float> {

    @Override
    public Float coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            try {
                return Float.valueOf((String) value);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new FastCannotCoerceException(type, value);
    }

}
