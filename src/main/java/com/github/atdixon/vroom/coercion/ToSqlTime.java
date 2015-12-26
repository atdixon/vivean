package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.sql.Time;

public final class ToSqlTime implements Coercion<java.sql.Time> {

    @Override
    public Time coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
