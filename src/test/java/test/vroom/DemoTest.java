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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@Test
public class DemoTest {

    /** See {@link test.vroom.ExtensiveEntityTest} for static typed
     * use cases / interface proxies. */
    public void testDemo() {
        Entity movie = Entity.adapt(new HashMap<String, Object>() {{
            put("title", "Super Movie");
            put("producer",
                asList("Bob Smith", "Jenny Baker", "Karen Lye"));
            put("release-year", 2015);
            put("sub-object",
                asList(new HashMap<String, Object>() {{
                           put("tag", "red");
                       }},
                    new HashMap<String, Object>() {{
                        put("tag", "green");
                    }}));
        }});

        // access required knowledge...
        assertEquals(movie.getInt("release-year"), 2015);

        // if release-year isn't present, that will throw exception.
        // more robustly, you can ask if knowledge is there first...
        if (movie.has("release-year", int.class))  {
            assertEquals(movie.getInt("release-year"), 2015);
        }

        // or offer a default for missing attributes...
        assertEquals(movie.getOne("dvd-release-year", int.class, 2016),
            (Integer) 2016);

        // or use null semantics
        if (movie.orNull("dvd-release-year", Integer.class) != null) {
            fail();
        }

        // for collections, sometimes you can only handle one...
        assertEquals(movie.getOne("producer", String.class, null),
            "Bob Smith");

        // but you can upgrade your code to handle more later...
        assertEquals(movie.getMany("producer", String.class),
            asList("Bob Smith", "Jenny Baker", "Karen Lye"));

        assertEquals(movie.getMany("director", String.class),
            Collections.emptyList());

        assertEquals(movie.getOne("sub-object", Map.class, null),
            new HashMap<String, Object>() {{
                put("tag", "red"); }});
        assertEquals(movie.getMany("sub-object", Map.class),
            asList(
            new HashMap<String, Object>() {{
                put("tag", "red"); }},
            new HashMap<String, Object>() {{
                put("tag", "green"); }}));

        assertEquals(movie.getOne("sub-object", Entity.class, null),
            Entity.adapt(new HashMap<String, Object>() {{
                put("tag", "red");
            }}));
        assertEquals(movie.getMany("sub-object", Entity.class),
            asList(
                Entity.adapt(new HashMap<String, Object>() {{
                    put("tag", "red");
                }}),
                Entity.adapt(new HashMap<String, Object>() {{
                    put("tag", "green");
                }})));
    }

}
