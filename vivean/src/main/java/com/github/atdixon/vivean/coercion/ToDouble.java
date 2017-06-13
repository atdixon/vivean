package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;

public final class ToDouble implements Coercion<Double> {

    @Override
    public Double coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new FastCannotCoerceException(type, value);
    }

}
