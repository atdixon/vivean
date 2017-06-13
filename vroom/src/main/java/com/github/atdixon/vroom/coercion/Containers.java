package com.github.atdixon.vroom.coercion;

import java.lang.reflect.Array;
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
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.atdixon.vroom.coercion.Util.rawTypeOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

public final class Containers {

    private static Map<Class, Object> empties = new HashMap<>();
    static {
        empties.put(Optional.class, Optional.empty());
        empties.put(Object[].class, new Object[0]);
        empties.put(List.class, emptyList());
        empties.put(Set.class, emptySet());
        empties.put(ArrayList.class, unmodifiableList(new ArrayList()));
        empties.put(LinkedList.class, unmodifiableList(new LinkedList()));
        empties.put(HashSet.class, unmodifiableSet(new HashSet()));
        empties.put(LinkedHashSet.class, unmodifiableSet(new LinkedHashSet()));
    }

    private Containers() {}

    public static boolean isContainer(Type type) {
        return isContainer(rawTypeOf(type));
    }

    public static boolean isContainer(Class type) {
        return Iterable.class.isAssignableFrom(type)
            || type.isArray()
            || Optional.class.equals(type);
    }

    public static boolean isContainerValue(Object value) {
        return value != null && isContainer(value.getClass());
    }

    public static Type containerType(Type type) {
        if (type instanceof ParameterizedType) {
            final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArguments.length == 1) {
                return typeArguments[0];
            }
        }
        final Class raw = rawTypeOf(type);
        if (raw.isArray()) {
            return raw.getComponentType();
        }
        return Object.class;
    }

    public static void forEach(Object container, Consumer<Object> c) {
        forEach(container, v -> { c.accept(v); return true; });
    }

    /** Iterate until f answer false. */
    public static void forEach(Object container, Function<Object, Boolean> f) {
        if (container instanceof Iterable) {
            for (Object value : ((Iterable) container)) {
                if (!f.apply(value))
                    return;
            }
            return;
        }
        if (container.getClass().isArray()) {
            final int length = Array.getLength(container);
            for (int i = 0; i < length; i++) {
                if (!f.apply(Array.get(container, i)))
                    return;
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

    public static boolean isEmptyContainer(Object value) {
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

    public static int size(Object value) {
        if (value instanceof Iterable) {
            if (value instanceof Collection) {
                return ((Collection<?>) value).size();
            } else {
                final int[] size = new int[1];
                ((Iterable<?>) value).forEach(v -> ++size[0]);
                return size[0];
            }
        }
        if (value instanceof Object[]) {
            return ((Object[]) value).length;
        }
        if (value instanceof Optional) {
            return ((Optional) value).isPresent() ? 1 : 0;
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    public static Object instantiateContainer(Type type, List values) {
        final Class raw = rawTypeOf(type);
        try {
            if (values.isEmpty() && empties.containsKey(raw)) {
                return empties.get(raw);
            }
            if (raw.isArray()) {
                final Object answer = Array.newInstance(raw.getComponentType(), values.size());
                for (int i = 0; i < values.size(); ++i)
                    Array.set(answer, i, values.get(i));
                return answer;
            }
            if (Collection.class.isAssignableFrom(raw)) {
                final Collection answer;
                if (!Modifier.isAbstract(raw.getModifiers())) {
                    answer = (Collection) raw.newInstance();
                    answer.addAll(values);
                } else if (List.class.equals(raw)) {
                    answer = new ArrayList(values);
                } else if (Set.class.equals(raw)) {
                    answer = new LinkedHashSet(values);
                } else if (Collection.class.equals(raw)) {
                    answer = new ArrayList(values);
                } else {
                    throw new IllegalArgumentException();
                }
                return answer;
            }
            if (Optional.class.equals(raw)) {
                return values.isEmpty() ? Optional.empty() : Optional.of(values.iterator().next());
            }
            throw new IllegalArgumentException();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
