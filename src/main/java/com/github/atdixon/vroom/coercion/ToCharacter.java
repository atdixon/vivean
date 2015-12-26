package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;

public final class ToCharacter implements Coercion<Character> {

    @Override
    public Character coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
