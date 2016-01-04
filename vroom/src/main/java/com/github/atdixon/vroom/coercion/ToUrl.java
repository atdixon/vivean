package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

public final class ToUrl implements Coercion<URL> {

    @Override
    public URL coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (value instanceof URL) {
            return (URL) value;
        } else if (value instanceof String) {
            try {
                new URL((String) value);
            } catch (MalformedURLException e) {
                throw new FastCannotCoerceException(type, value, e);
            }
        }
        throw new FastCannotCoerceException(type, value);
    }

}
