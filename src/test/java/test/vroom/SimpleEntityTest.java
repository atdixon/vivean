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

import com.github.atdixon.vroom.Entity;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test
public class SimpleEntityTest {

    @SuppressWarnings("unchecked")
    public void testSimple() {
        Entity e = Entity.adapt(ImmutableMap.<String, Object>builder()
            .put("name", "my entity")
            .put("age", Integer.MAX_VALUE)
            .put("ttl", Integer.MIN_VALUE)
            .put("creator", getClass().getSimpleName())
            .put("details.key1", "val1")
            .put("details", asList(ImmutableMap.of(
                    "key2", "val2",
                    "key3", asList("val3"),
                    "key4", asList("val4a", "val4b"),
                    "key5", asList(),
                    "key.6", "val6"
                ))).build());

        assertTrue(e.has("age", int.class));
        assertFalse(e.has("ttl", boolean.class)); // not as boolean

        assertEquals(e.getInt("age"), Integer.MAX_VALUE);
        assertEquals((int) e.getOne("age", int.class), Integer.MAX_VALUE);
        assertEquals((int) e.orNull("age", int.class), Integer.MAX_VALUE);
        assertEquals((int) e.orOptional("age", int.class).orNull(), Integer.MAX_VALUE);
        assertTrue(e.getOne("age", int.class, 0) == Integer.MAX_VALUE);

        assertEquals(e.orNull("no-such-key", int.class), null);

        assertEquals(e.getOne("details.key1", String.class), "val1");
        assertEquals(e.getOne("details.key2", String.class), "val2");
        assertEquals(e.getOne("details.key3", String.class), "val3");
        assertEquals(e.getOne("details.key4", String.class), "val4a");
        assertEquals(e.getOne("details.key5", String.class, null), null);
        assertEquals(e.getOne("details.key.6", String.class), "val6");

        assertNull(e.orNull("details.missing.rating", int.class));
        try {
            assertEquals((int) e.getOne("details.missing.rating", int.class), 4);
            fail();
        } catch (Exception ex) { /* ok */ }

        try {
            e.getBoolean("isMissing");
            fail();
        } catch (Exception ex) { /* ok */ }
    }

}
