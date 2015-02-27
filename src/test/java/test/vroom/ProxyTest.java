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

import com.github.atdixon.vroom.Attribute;
import com.github.atdixon.vroom.DefaultValue;
import com.github.atdixon.vroom.V;
import com.github.atdixon.vroom.VMap;
import com.github.atdixon.vroom.MissingRequiredValueException;
import com.google.common.base.Optional;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

@Test
public class ProxyTest {

    public void textExtensiveScenarios() {
        // test different scenarios
        Movie m1 = V.proxy(new HashMap<String, Object>() {{
                               put("title", "Dr. Strangelove");
                               put("subtitle", "How I Learned to Stop Worrying and Love the Bomb");
                               put("releaseYear", 1964);
                               put("awesome", true);
                               put("sequel", Collections.emptyMap());
                           }},
            Movie.class, InferredTitle.class);

        Movie m2 = V.proxy(new HashMap<String, Object>() {{
                               put("title", "Dr. Strangelove");
                               put("subtitle", "How I Learned to Stop Worrying and Love the Bomb");
                               put("releaseYear", 1964);
                               put("sequel", new HashMap<String, Object>() {{
                                   put("releaseYear", 4);
                               }});
                           }},
            Movie.class, InferredTitle.class);

        Movie m3 = V.proxy(new HashMap<String, Object>() {{
                               put("title", asList("Dr. Strangelove",
                                   "How I Learned to Stop Worrying and Love the Bomb"));
                               // put a bad type, but one that can be coerced
                               put("releaseYear", 1964.00);
                           }},
            Movie.class, InferredTitle.class);

        Movie m4 = V.proxy(new HashMap<String, Object>() {{
                               // put a value that cannot successfully be coerced
                               put("releaseYear", "oops");
                           }},
            Movie.class);

        // movie1
        assertEquals(m1.myTitle(), "Dr. Strangelove");
        assertEquals(m1.subtitle(), "How I Learned to Stop Worrying and Love the Bomb");
        assertEquals(m1.releaseYear(), 1964);
        assertEquals(m1.isAwesome(), true);
        assertEquals(m1.sequelAsOptional().orNull(), null);
        assertEquals(m1.sequelAsList(), Collections.emptyList());
        assertEquals(m1.getSequel(), null);

        // movie2
        final Movie next = m2.getSequel();
        final Optional<Movie> nextAsOptional = m2.sequelAsOptional();
        final List<Movie> nextAsList = m2.sequelAsList();

        assertNotNull(next);
        assertNotNull(nextAsOptional.orNull());
        assertEquals(nextAsList.size(), 1);

        // movie3
        assertEquals(m3.myTitle(), "Dr. Strangelove"); // one of the titles is picked
        try {
            m3.subtitle();
            fail();
        } catch (MissingRequiredValueException e) { /* ok */ }

        assertNull(m3.getSequel());
        assertEquals(m3.releaseYear(), 1964);

        // movie4
        assertEquals(m4.releaseYear(), 1900/*default-value*/);
    }

    /**
     * Explicit name attr.
     */
    public static interface ExplicitTitle {
        @Nullable
        @Attribute(name = "title")
        String myTitle();
    }

    /**
     * Inferred name.
     */
    public static interface InferredTitle {
        @Nullable
        String getTitle();
    }

    /**
     * Entity interface.
     */
    public static interface Movie extends ExplicitTitle {
        @DefaultValue("true")
        boolean isAwesome();

        String subtitle() throws MissingRequiredValueException;

        @DefaultValue("1900")
        int releaseYear();

        @Nullable
        Movie getSequel();

        @Attribute(name = "sequel")
        List<Movie> sequelAsList();

        @Attribute(name = "sequel")
        Optional<Movie> sequelAsOptional();
    }
}
