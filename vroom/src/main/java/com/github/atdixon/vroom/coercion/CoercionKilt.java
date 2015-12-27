package com.github.atdixon.vroom.coercion;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.github.atdixon.vroom.coercion.Containers.containerType;
import static com.github.atdixon.vroom.coercion.Containers.forEach;
import static com.github.atdixon.vroom.coercion.Containers.instantiateContainer;
import static com.github.atdixon.vroom.coercion.Containers.isContainer;
import static com.github.atdixon.vroom.coercion.Containers.isEmptyContainer;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * todo explain kilt
 * . nulls become empty collections
 * . shrinkage
 * . etc.
 */
@SuppressWarnings("unchecked")
public final class CoercionKilt {

    public static <T> T coerce(Type type, @Nullable Object value) {
        if (isContainer(type)) {
            if (isContainer(value)) {
                final Type containerType = containerType(type);
                final List values = new ArrayList();
                forEach(value, v -> {
                    final Object coerced = coerce(containerType, v);
                    if (coerced != null && !isEmptyContainer(coerced)) {
                        values.add(coerced);
                    }
                    return true; });
                return (T) instantiateContainer(type, values);
            } else {
                final Object coerced = coerce(containerType(type), value);
                if (coerced == null || isEmptyContainer(coerced)) {
                    return (T) instantiateContainer(type, emptyList());
                } else {
                    return (T) instantiateContainer(type, singletonList(coerced));
                }
            }
        } else {
            if (isContainer(value)) {
                final Object[] answer = new Object[1];
                forEach(value, v -> {
                    final Object coerced = coerce(type, v);
                    if (coerced != null && !isEmptyContainer(coerced)) {
                        answer[0] = coerced;
                        return false;
                    }
                    return true; });
                return (T) answer[0];
            } else {
                try {
                    return CoercionRegistry.coerce(type, value);
                } catch (FastCannotCoerceException e) {
                    return null;
                }
            }
        }
    }

}
