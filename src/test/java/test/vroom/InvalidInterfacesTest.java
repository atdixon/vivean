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
