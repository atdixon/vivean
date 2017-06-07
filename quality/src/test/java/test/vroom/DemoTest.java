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

import com.github.atdixon.vroom.TS;
import com.github.atdixon.vroom.VMap;
import com.github.atdixon.vroom.Vget;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@Test
public class DemoTest {

    public void testDemo() {
        VMap movie = VMap.create(new HashMap<String, Object>() {{
            put("title", "Super Movie");
            put("producer",
                asList("Bob Smith", "Jenny Baker", "Karen Lye"));
            put("release-year", 2015);
            put("user-ratings", new double[] {1.0, 2.0, 5.0});
            put("sub-object",
                asList(new HashMap<String, Object>() {{
                           put("tag", "red");
                       }},
                    new HashMap<String, Object>() {{
                        put("tag", "green");
                    }}));
        }});

        // access required knowledge...
        assertEquals(movie.one("release-year", int.class), (Integer) 2015);

        // if release-year isn't present, that will throw exception.
        // more robustly, you can ask if knowledge is there first...
        if (movie.knows("release-year", int.class))  {
            assertEquals(movie.one("release-year", int.class), (Integer) 2015);
        }

        // ...however, this is more succinct (and more performant):
        movie.one("release-year", int.class, releaseYear ->
            assertEquals(releaseYear, (Integer) 2015));

        // or offer a default for missing attributes...
        assertEquals(movie.oneOr("dvd-release-year", int.class, 2016),
            (Integer) 2016);

        // or use null semantics
        if (movie.oneOr("dvd-release-year", Integer.class, null) != null) {
            fail();
        }

        // for collections, sometimes you can only handle one...
        assertEquals(movie.oneOr("producer", String.class, null),
            "Bob Smith");

        // but you can upgrade your code to handle more later...
        assertEquals(movie.many("producer", String.class),
            asList("Bob Smith", "Jenny Baker", "Karen Lye"));

        assertEquals(movie.many("director", String.class),
            Collections.emptyList());

        // multiple container (coll, array) types are supported...
        assertEquals(movie.many("user-ratings", Float.class),
            asList(1.0f, 2.0f, 5.0f));
        assertEquals(movie.many("user-ratings", Double.class),
            asList(1.0d, 2.0d, 5.0d));

        // access non-primitive lists
        final Map<String, Object> sub = movie.oneOr("sub-object", TS.Map(), null);
        assertEquals(sub,
            new HashMap<String, Object>() {{
                put("tag", "red"); }});
        final List<Map<String, Object>> subs = movie.many("sub-object", TS.Map());
        assertEquals(subs,
            asList(
            new HashMap<String, Object>() {{
                put("tag", "red"); }},
            new HashMap<String, Object>() {{
                put("tag", "green"); }}));

        // as Set of Maps...
        final Set<Map<String, Object>> subSet = movie.one("sub-object", TS.Set(TS.Map()));
        assertEquals(subSet,
            new HashSet<Map<String, Object>>(asList(
                new HashMap<String, Object>() {{
                    put("tag", "red");
                }},
                new HashMap<String, Object>() {{
                    put("tag", "green");
                }})));

        // toMap support
        assertEquals(movie.toMap().get("title"), "Super Movie");

        assertEquals(movie.oneOr("sub-object", VMap.class, null)
                .toMap(),
            VMap.create(new HashMap<String, Object>() {{
                put("tag", "red");
            }}).toMap());
        assertEquals(movie.many("sub-object", VMap.class)
            .stream()
            .map(VMap::toMap)
            .collect(Collectors.toList()),
            asList(
                VMap.create(new HashMap<String, Object>() {{
                    put("tag", "red");
                }}).toMap(),
                VMap.create(new HashMap<String, Object>() {{
                    put("tag", "green");
                }}).toMap()));

        // Vget
        final Map<String, Object> map = new HashMap<String, Object>() {{
            put("foo", new HashMap<String, Object>() {{
                put("bar", 1);
                put("bar.cat", "true");
            }});
        }};
        assertEquals(Vget.oneInt(map, "foo.bar"), 1);
        assertEquals(Vget.oneBoolean(map, "foo.bar.cat"), true);
    }

}
