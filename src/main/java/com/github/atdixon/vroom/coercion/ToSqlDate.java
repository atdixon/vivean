package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Type;
import java.sql.Date;

public final class ToSqlDate implements Coercion<java.sql.Date> {

    @Override
    public Date coerce(Type type, Object value) throws CannotCoerceException {
        throw new UnsupportedOperationException("not yet");
    }

}
