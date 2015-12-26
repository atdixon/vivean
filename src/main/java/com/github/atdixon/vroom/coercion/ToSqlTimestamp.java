package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public final class ToSqlTimestamp implements Coercion<java.sql.Timestamp> {

    @Override
    public Timestamp coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
