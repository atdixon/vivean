package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToBoolean implements Coercion<Boolean> {

    @Override
    public Boolean coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            if ("true".equals(value)) {
                return Boolean.TRUE;
            } else if ("false".equals(value)) {
                return Boolean.FALSE;
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
