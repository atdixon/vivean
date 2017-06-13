package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;

public final class ToCharacter implements Coercion<Character> {

    @Override
    public Character coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (value instanceof Character) {
            return (Character) value;
        } else if (value instanceof String && 1 == ((String) value).length()) {
            return ((String) value).charAt(0);
        }
        throw new FastCannotCoerceException(type, value);
    }

}
