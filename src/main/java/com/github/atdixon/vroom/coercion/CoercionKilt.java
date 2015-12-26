package com.github.atdixon.vroom.coercion;

import com.github.atdixon.vroom2.vutil;

import javax.annotation.Nullable;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

/**
 * todo explain kilt
 * . nulls become empty collections
 * . shrinkage
 * . etc.
 */
@SuppressWarnings("unchecked")
public final class CoercionKilt {

    private static Map<Class, Object> empties = new HashMap<>();
    static {
        empties.put(Object[].class, new Object[0]);
        empties.put(List.class, emptyList());
        empties.put(Set.class, emptySet());
        empties.put(ArrayList.class, unmodifiableList(new ArrayList()));
        empties.put(LinkedList.class, unmodifiableList(new LinkedList()));
        empties.put(HashSet.class, unmodifiableSet(new HashSet()));
        empties.put(LinkedHashSet.class, unmodifiableSet(new LinkedHashSet()));
    }

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
                final Type containerType = containerType(type);
                final Object[] answer = new Object[1];
                forEach(value, v -> {
                    final Object coerced = coerce(containerType, v);
                    if (coerced != null && !isEmptyContainer(coerced)) {
                        answer[0] = coerced;
                        return false;
                    }
                    return true; });
                return (T) answer[0];
            } else {
                try {
                    return CoercionRegistry.coerce(type, value);
                } catch (CannotCoerceException e) {
                    return null;
                }
            }
        }
    }

    private static Type containerType(Type type) {
        if (type instanceof ParameterizedType) {
            final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArguments.length == 1) {
                return typeArguments[0];
            }
        }
        final Class raw = vutil.rawTypeOf(type);
        if (raw.isArray()) {
            return raw.getComponentType();
        }
        return Object.class;
    }

    private static boolean isContainer(Type type) {
        return isContainer(vutil.rawTypeOf(type));
    }

    private static boolean isContainer(Class type) {
        return Iterable.class.isAssignableFrom(type)
            || type.isArray()
            || Optional.class.equals(type);
    }

    private static boolean isContainer(Object value) { // todo: test array, collection, iterable, etc
        return value != null && isContainer(value.getClass());
    }

    /** Iterate until f answer false. */
    private static void forEach(Object container, Function<Object, Boolean> f) {
        if (container instanceof Iterable) {
            for (Object value : ((Iterable) container)) {
                if (!f.apply(value))
                    return;
            }
            return;
        }
        if (container instanceof Object[]) {
            for (Object value : (Object[]) container) {
                if (!f.apply(value)) {
                    return;
                }
            }
            return;
        }
        if (container instanceof Optional) {
            if (((Optional) container).isPresent())
                f.apply(((Optional) container).get());
            return;
        }
        throw new IllegalArgumentException();
    }

    private static Object instantiateContainer(Type type, List values) {
        final Class raw = vutil.rawTypeOf(type);
        try {
            if (values.isEmpty() && empties.containsKey(raw)) {
                return empties.get(raw);
            }
            if (Object[].class.equals(raw)) {
                return values.toArray(new Object[values.size()]);
            }
            if (Collection.class.isAssignableFrom(raw)) {
                final Collection answer;
                if (!Modifier.isAbstract(raw.getModifiers())) {
                    answer = (Collection) raw.newInstance();
                } else if (List.class.equals(raw)) {
                    answer = new ArrayList();
                } else if (Set.class.equals(raw)) {
                    answer = new LinkedHashSet();
                } else {
                    throw new IllegalArgumentException();
                }
                answer.addAll(values);
                return answer;
            }
            throw new IllegalArgumentException();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isEmptyContainer(Object value) {
        if (value instanceof Iterable) {
            return !((Iterable) value).iterator().hasNext();
        }
        if (value instanceof Object[]) {
            return ((Object[]) value).length == 0;
        }
        if (value instanceof Optional) {
            return !((Optional) value).isPresent();
        }
        return false;
    }

}
