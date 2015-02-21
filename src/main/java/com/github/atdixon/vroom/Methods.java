/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.atdixon.vroom;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

final class Methods {

    private Methods() {}

    static boolean isGetter(Method m) {
        return m.getReturnType() != null && m.getParameterTypes().length == 0;
    }

    /** Is the provided method (assume it has a return value) @Nullable? */
    static boolean isNullable(Method m) {
        return null != m.getAnnotation(Nullable.class);
    }

    static boolean isPrimitive(Method m) {
        return m.getReturnType().isPrimitive();
    }

    static boolean isCollection(Method m) {
        return isCollection(m.getReturnType());
    }

    static boolean isCollection(Type type) {
        return Collection.class.isAssignableFrom((Class<?>) type);
    }

    static boolean isOptional(Method m) {
        return m.getReturnType() == Optional.class;
    }

    /** Precondition: isOptional(type) */
    static Type typeOfOptional(Type type) {
        final ParameterizedType asImpl = (ParameterizedType) type;
        return asImpl.getActualTypeArguments()[0];
    }

    static boolean isDefaulted(Method m) {
        return null != m.getAnnotation(DefaultValue.class);
    }

    /** Precondition: isDefaulted(m) */
    static String defaultValue(Method m) {
        return m.getAnnotation(DefaultValue.class).value();
    }

    static boolean isList(Method m) {
        return List.class.isAssignableFrom(m.getReturnType());
    }

    static String toAttributeName(Method m) {
        final Attribute attr = m.getAnnotation(Attribute.class);
        return attr != null ? attr.name() : uncapitalize(removeGetterStart(m));
    }

    /** Remove "get" or "is" from the front of the name of a getter if needed and return the result. */
    private static String removeGetterStart(Method m) {
        if (m.getName().startsWith("get")  && Character.isUpperCase(m.getName().charAt(3))) {
            return m.getName().substring(3);
        }
        if (m.getName().startsWith("is") && Character.isUpperCase(m.getName().charAt(2)) &&
                (m.getReturnType() == boolean.class || m.getReturnType() == Boolean.class)) {
            return m.getName().substring(2);
        }
        return m.getName();
    }

    /** pre: isCollection(type) */
    static Type typeOfCollection(Type type) {
        final ParameterizedType asImpl = (ParameterizedType) type;
        return asImpl.getActualTypeArguments()[0];
    }

    private static String uncapitalize(String v) {
        return Strings.isNullOrEmpty(v) ? v : (Character.toLowerCase(v.charAt(0)) + v.substring(1));
    }

}
