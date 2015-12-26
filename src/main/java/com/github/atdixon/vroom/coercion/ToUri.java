package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.net.URI;

public final class ToUri implements Coercion<URI> {

    @Override
    public URI coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
