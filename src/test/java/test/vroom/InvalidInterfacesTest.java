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
package test.vroom;

import com.github.atdixon.vroom.DefaultValue;
import com.github.atdixon.vroom.Entity;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;

import static org.testng.Assert.fail;

@Test
public class InvalidInterfacesTest {

    private static final Class[] invalids = new Class[] {
        NilAndDefault.class,
        BadCollectType.class,
        IncompatDefaultVal.class,
        NullPrim.class
    };

    public void testBadInterfaces() {
        for (Class invalid : invalids) {
            assertInvalidInterface(invalid);
        }
    }

    private static void assertInvalidInterface(Class<?> iface) {
        try {
            Entity.adapt(new HashMap<String, Object>() {{
                             put("foo", "bar"); }}, iface);
            fail(": " + iface);
        } catch (Exception e) { /* success */ }
    }


    /** Incompat. default value. */
    public static interface IncompatDefaultVal {
        @DefaultValue("5") boolean isGood();
    }

    /** Bad collect type. */
    public static interface BadCollectType {
        Set<Integer> getNumbers();
    }

    /** Nullable w/ default. */
    public static interface NilAndDefault {
        @Nullable @DefaultValue("5") Integer getNumber();
    }

    /** Null primitive. */
    public static interface NullPrim {
        @Nullable int getNumber();
    }
}
