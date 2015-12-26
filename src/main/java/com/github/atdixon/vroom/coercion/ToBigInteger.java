package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.math.BigInteger;

public final class ToBigInteger implements Coercion<BigInteger> {

    @Override
    public BigInteger coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
