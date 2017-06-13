package com.github.atdixon.vivean.coercion;

import java.lang.reflect.Type;
import java.math.BigInteger;

public final class ToBigInteger implements Coercion<BigInteger> {

    @Override
    public BigInteger coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        try {
            return new BigInteger(value.toString());
        } catch (NumberFormatException e) {
            throw new FastCannotCoerceException(type, value, e);
        }
    }

}
