package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.util.Map;

public final class ToMap implements Coercion<Map> {

    @Override
    public Map coerce(Type type, Object value) throws CannotCoerceException {
        if (value instanceof Map) {
            return (Map) value;
        }
        throw new CannotCoerceException(type, value);
    }

}
