package com.github.atdixon.vroom;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @see <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html}">http://gafter.blogspot.com/2006/12/super-type-tokens.html</a>
 */
public abstract class TypeReference<T> {

    private final Type type;

    protected TypeReference() {
        final Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("missing type parameter");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }

}
