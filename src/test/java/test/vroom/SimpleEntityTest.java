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
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test
public class SimpleEntityTest {

    public void testSimple() {
        Entity e = Entity.adapt(new HashMap<String, Object>() {{
            put("title", "heat");
            put("year", 2005);
            put("director", "michael mann");
        }});

        assertTrue(e.has("year", int.class));
        assertFalse(e.has("release-date", int.class));

        assertEquals(e.requireInt("year"), 2005);
        assertEquals((int) e.orNull("year", int.class), 2005);
        assertTrue(e.getOne("year", int.class, 0) == 2005);

        assertEquals(e.orNull("release-date", int.class), null);

        try {
            e.requireBoolean("isMissing");
            fail();
        } catch (Exception ex) { /* ok */ }
    }

}
