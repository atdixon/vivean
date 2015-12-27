package com.github.atdixon.vroom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/*package*/ final class ParameterizedTypeImpl implements ParameterizedType {

    private final Type ownerType;
    private final Class rawType;
    private final Type[] argumentsList;

    ParameterizedTypeImpl(@Nullable Type ownerType, @Nonnull Class rawType, Type... typeArguments) {
        this.ownerType = ownerType;
        this.rawType = rawType;
        this.argumentsList = typeArguments;
    }

    @Override public Type getOwnerType() {
        return ownerType;
    }

    @Override public Type getRawType() {
        return rawType;
    }

    @Override public Type[] getActualTypeArguments() {
        return argumentsList;
    }

    @Override public int hashCode() {
        return (ownerType == null ? 0 : ownerType.hashCode())
            ^ Arrays.hashCode(argumentsList) ^ rawType.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (!(obj instanceof ParameterizedType)) {
            return false;
        }
        final ParameterizedType other = (ParameterizedType) obj;
        return getRawType().equals(other.getRawType())
            && Objects.equals(getOwnerType(), other.getOwnerType())
            && Arrays.equals(getActualTypeArguments(), other.getActualTypeArguments());
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        if (ownerType != null) {
            buf.append((ownerType instanceof Class)
                ? ((Class<?>) ownerType).getName()
                : ownerType.toString()).append('.');
        }
        buf.append(rawType.getName());
        if (argumentsList.length > 0) {
            buf.append('<');
            for (Type arg : argumentsList) {
                buf.append((arg instanceof Class)
                    ? ((Class<?>) arg).getName()
                    : arg.toString());
                buf.append(',');
            }
            buf.deleteCharAt(buf.length() - 1);
            buf.append('>');
        }
        return buf.toString();
    }

}
