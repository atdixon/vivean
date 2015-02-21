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
