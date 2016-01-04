package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class ToLinkedHashMap implements Coercion<LinkedHashMap> {

    @Override
    public LinkedHashMap coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value instanceof LinkedHashMap) {
            return (LinkedHashMap) value;
        }
        if (value instanceof Map) {
            return new LinkedHashMap((Map) value);
        }
        throw new FastCannotCoerceException(type, value);
    }

}
