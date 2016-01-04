package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToString implements Coercion<String> {

    @Override
    public String coerce(Type type, Object value) throws FastCannotCoerceException {
        return (value == null)
        ? null :
            ((value instanceof String) ? (String) value : value.toString());
    }

}
