package com.github.atdixon.vivean.coercion;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public interface Coercion<T> {

    @Nullable T coerce(Type type, Object value) throws FastCannotCoerceException;

}
