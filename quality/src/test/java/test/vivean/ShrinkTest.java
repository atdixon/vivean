package test.vivean;

import com.github.atdixon.vivean.Shrink;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Test
public final class ShrinkTest {

    @SuppressWarnings("unchecked")
    public void testShrink() {
        assertEquals(Shrink.shrink(Collections.emptyMap()), Collections.emptyMap());
        assertNull(Shrink.shrinkToNull(Collections.emptyMap()));

        final Map<String, Object> empty = new HashMap<String, Object>() {{
            put("one", Collections.emptyList());
            put("two", Collections.emptySet());
            put("three", new HashMap<String, Object>() {{
                put("alpha", new Object[0]);
                put("beta", Optional.empty());
            }});
        }};

        assertEquals(Shrink.shrink(empty), Collections.emptyMap());
        assertNull(Shrink.shrinkToNull(empty));

        final Object[] gammaGamma = {"gamma", "gamma"};
        final Map<String, Object> sparse = new HashMap<String, Object>() {{
            put("one", Collections.singletonList(1));
            put("two", Arrays.asList(2, "2"));
            put("three", new Object[] { 3 });
            put("four", new HashMap<String, Object>() {{
                put("alpha", new Object[] { "alpha" });
                put("beta", new Object[] { "beta" });
                put("gamma", gammaGamma);
            }});}};

        final Map<String, Object> shrunkSparse = new HashMap<String, Object>() {{
            put("one", 1);
            put("two", Arrays.asList(2, "2"));
            put("three", 3);
            put("four", new HashMap<String, Object>() {{
                put("alpha", "alpha");
                put("beta", "beta");
                put("gamma", gammaGamma);
            }});
        }};
        assertEquals(Shrink.shrink(sparse), shrunkSparse);
        assertEquals((Map<String, Object>) Shrink.shrinkToNull(sparse), shrunkSparse);
    }

}
