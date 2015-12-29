package com.github.atdixon.vroom.coercion;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import java.lang.reflect.Type;
import java.util.Map;

public final class ToPMap implements Coercion<PMap> {

    @Override
    public PMap coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value instanceof PMap) {
            return (PMap) value;
        }
        if (value instanceof Map) {
            return HashTreePMap.from((Map) value);
        }
        throw new FastCannotCoerceException(type, value);
    }

}
