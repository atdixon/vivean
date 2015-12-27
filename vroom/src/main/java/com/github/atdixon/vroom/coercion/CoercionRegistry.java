package com.github.atdixon.vroom.coercion;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.atdixon.vroom.coercion.Util.rawTypeOf;

public final class CoercionRegistry {

    private static final Map<Class, Coercion> registry = new HashMap<>();

    static {
        register(new ToObject(), Object.class);

        register(new ToMap(), Map.class);
        register(new ToHashMap(), HashMap.class);
        register(new ToLinkedHashMap(), LinkedHashMap.class);

        register(new ToByte(), Byte.class, byte.class);
        register(new ToShort(), Short.class, short.class);
        register(new ToInteger(), Integer.class, int.class);
        register(new ToLong(), Long.class, long.class);
        register(new ToFloat(), Float.class, float.class);
        register(new ToDouble(), Double.class, double.class);
        register(new ToBoolean(), Boolean.class, boolean.class);
        register(new ToCharacter(), Character.class, char.class);

        register(new ToString(), String.class);

        register(new ToDate(), java.util.Date.class);
        register(new ToSqlDate(), java.sql.Date.class);
        register(new ToSqlTime(), java.sql.Time.class);
        register(new ToSqlTimestamp(), java.sql.Timestamp.class);
        register(new ToCalendar(), GregorianCalendar.class, Calendar.class);

        register(new ToUrl(), URL.class);
        register(new ToUri(), URI.class);
    }

    private CoercionRegistry() {}

    public static void register(Coercion coercion, Class... types) {
        for (Class type : types)
            registry.put(type, coercion);
    }

    public static void deregister(Class... types) {
        for (Class type : types)
            registry.remove(type);
    }

    public static Coercion require(Class type) {
        return Objects.requireNonNull(registry.get(type), () -> "no coercion for " + type.getName());
    }

    @SuppressWarnings("unchecked") @Nullable
    public static <T> T coerce(Type type, @Nullable Object value) throws FastCannotCoerceException {
        if (value == null) {
            return null;
        } else if (type instanceof Class && value.getClass().isAssignableFrom((Class) type)) {
            return (T) value;
        } else { // a coercion is required
            try {
                return (T) require(rawTypeOf(type))
                    .coerce(type, value);
            } catch (Exception e) {
                throw (e instanceof FastCannotCoerceException)
                    ? (FastCannotCoerceException) e
                    : new FastCannotCoerceException(type, value, e);
            }
        }
    }

}
