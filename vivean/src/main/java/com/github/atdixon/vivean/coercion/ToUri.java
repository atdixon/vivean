package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;
import java.net.URI;

public final class ToUri implements Coercion<URI> {

    @Override
    public URI coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (value instanceof URI) {
            return (URI) value;
        } else if (value instanceof String) {
            try {
                URI.create((String) value);
            } catch (IllegalArgumentException e) {
                throw new FastCannotCoerceException(type, value, e);
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
