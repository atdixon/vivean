package com.github.atdixon.vroom.coercion;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public interface Coercion<T> {

    @Nullable T coerce(Type type, Object value) throws CannotCoerceException;

}
