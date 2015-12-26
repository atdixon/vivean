package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public final class ToBigDecimal implements Coercion<BigDecimal> {

    @Override
    public BigDecimal coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
