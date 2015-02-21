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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/** Validate interfaces that clients wish to proxy over a map/entity. */
final class InterfaceValidation {

    private InterfaceValidation() {}

    static void validateInterfaces(Class<?>... types) {
        for (Class<?> type : types) {
            validateOne(type);
        }
    }

    private static void validateOne(Class<?> type) {
        Util.insist(type.isInterface());
        for (Method m : type.getMethods()) {
            Util.insist(Methods.isGetter(m), String.format("expected getter method: %s", m.getName()));
            validateGetter(m);
        }
    }

    /** Precondition: isGetter(m) */
    private static void validateGetter(Method m) {
        if (Methods.isCollection(m)) {
            Util.insist(Methods.isList(m),
                "a getter must return List Collection");
            Util.insist(!Methods.isNullable(m),
                "illegal Nullable on getter returning Collection");
            Util.insist(!Methods.isDefaulted(m),
                "illegal com.github.atdixon.vroom.DefaultValue on getter returning Collection");
            Util.insist(isSupportedCollectionReturnType(m.getGenericReturnType()),
                "illegal parameterized type for collection returned from getter");
        } else {
            Util.insist(!Methods.isNullable(m) || !Methods.isPrimitive(m),
                "illegal Nullable on primitive getter");
            Util.insist(xor(Methods.isNullable(m), Methods.isOptional(m), isRequired(m), Methods.isDefaulted(m)),
                "getter can have @Nullable, @com.github.atdixon.vroom.DefaultValue, throw IllegalStateException, or return Optional, but" +
                    " not any combination of these");
            if (Methods.isOptional(m)) {
                Util.insist(isSupportedOptionalType(m.getGenericReturnType()),
                    "illegal type parameter on Optional");
            } else {
                Util.insist(isSupportedReturnType(m.getReturnType()),
                    String.format("illegal return type: %s", m.getReturnType()));
                if (Methods.isDefaulted(m)) {
                    final String defaultValue = Methods.defaultValue(m);
                    Util.notNull(Coercions.to(defaultValue, m.getReturnType(), null),
                        "com.github.atdixon.vroom.DefaultValue doesn't match return type");
                }
            }
        }
    }

    /** Precondition: type == Optional.class */
    private static boolean isSupportedOptionalType(Type type) {
        return isSupportedReturnType(Methods.typeOfOptional(type));
    }

    private static boolean isRequired(Method m) {
        for (Class et : m.getExceptionTypes()) {
            if (et == MissingRequiredValueException.class)
                return true;
        }
        return false;
    }

    private static boolean isSupportedCollectionReturnType(Type type) {
        return isSupportedReturnType(Methods.typeOfCollection(type));
    }

    private static boolean isSupportedReturnType(Type type) {
        return ((Class) type).isInterface()
            || type == Boolean.class || type == boolean.class
            || type == Double.class || type == double.class
            || type == Float.class || type == float.class
            || type == Integer.class || type == int.class
            || type == Map.class
            || type == String.class;
    }

    private static boolean xor(boolean... vs) {
        boolean hasTrue = false;
        for (boolean v : vs) {
            if (v && hasTrue) return false;
            hasTrue |= v;
        }
        return hasTrue;
    }

}
