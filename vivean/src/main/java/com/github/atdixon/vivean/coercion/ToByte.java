package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;

public final class ToByte implements Coercion<Byte> {

    @Override
    public Byte coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (value instanceof Byte) {
            return (Byte) value;
        } else if (value instanceof Number) {
            return ((Number) value).byteValue();
        }
        if (value instanceof String) {
            try {
                return Byte.valueOf((String) value);
            } catch (NumberFormatException e) {/*ok*/}
        }
        throw new FastCannotCoerceException(type, value);
    }

}
