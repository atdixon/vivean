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
import com.google.common.reflect.AbstractInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

final class ProxyFactory {

    private ProxyFactory() {}

    @SuppressWarnings("unchecked")
    /*pkg*/ static <T> T adapt(Entity entity, Class<T> type, Class<?>... types) {
        Util.notNull(entity);
        final Class<?>[] allTypes = new Class<?>[types.length + 1];
        allTypes[0] = type;
        System.arraycopy(types, 0, allTypes, 1, types.length);
        InterfaceValidation.validateInterfaces(allTypes);
        return (T) Proxy.newProxyInstance(
            type.getClassLoader(),
            allTypes,
            new InternalInvocationHandler(entity));
    }

    /** Invocationhandler. */
    static final class InternalInvocationHandler extends AbstractInvocationHandler {

        private final Entity entity;

        /*package*/ InternalInvocationHandler(Entity entity) {
            this.entity = entity;
        }

        @Override
        protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
            if (Methods.isGetter(method)) {
                final String attribute = Methods.toAttributeName(method);
                if (Methods.isCollection(method)) {
                    return entity.getMany(attribute, (Class<?>) Methods.typeOfCollection(method.getGenericReturnType()));
                }
                final Object one;
                if (Methods.isOptional(method)) {
                    one = entity.getOne(attribute,
                        (Class<?>) Methods.typeOfOptional(method.getGenericReturnType()), /*default=*/null);
                } else {
                    one = entity.getOne(attribute, method.getReturnType(), /*default=*/null);
                }
                if (one == null && !Methods.isNullable(method) && !Methods.isOptional(method) && !Methods.isDefaulted(method)) {
                    throw new MissingRequiredValueException(attribute);
                }
                if (one == null && Methods.isDefaulted(method)) {
                    return Util.notNull(Coercions.to(Methods.defaultValue(method), method.getReturnType(), null));
                }
                return Methods.isOptional(method) ? Optional.fromNullable(one) : one;
            } else {
                throw new UnsupportedOperationException("can't handle method: " + method.getName());
            }
        }

    }
}
