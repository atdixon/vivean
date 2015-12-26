package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.net.URL;

public final class ToUrl implements Coercion<URL> {

    @Override
    public URL coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
