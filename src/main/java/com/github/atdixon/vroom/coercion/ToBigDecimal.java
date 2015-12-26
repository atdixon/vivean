package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public final class ToBigDecimal implements Coercion<BigDecimal> {

    @Override
    public BigDecimal coerce(Type type, Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(value.toString().trim());
        } catch (NumberFormatException e) {
            throw new FastCannotCoerceException(type, value, e);
        }
    }

}
