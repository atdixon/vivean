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

import java.util.Collection;

final class Util {

    private Util() {}

    static <T> T notNull(T value) {
        if (value == null)
            throw new IllegalStateException();
        return value;
    }

    static <T> T notNull(T value, String message) {
        if (value == null)
            throw new IllegalStateException(message);
        return value;
    }

    static void insist(boolean condition) {
        if (!condition)
            throw new IllegalStateException();
    }

    static void insist(boolean condition, String message) {
        if (!condition)
            throw new IllegalStateException(message);
    }

    static boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type)
            || type.isArray();
    }

}
