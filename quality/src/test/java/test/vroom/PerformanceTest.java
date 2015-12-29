package test.vroom;

import com.github.atdixon.vroom.V;
import com.github.atdixon.vroom.VMap;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Test(groups = "perf", enabled = false)
public final class PerformanceTest {

    public static void main(String[] args) {

        final Map<String, Object> m = new HashMap<String, Object>() {{
            put("person.age", "1");
        }};

        final VMap v = VMap.create(m);

        System.out.println(time(10000000, i -> {
            v.one("person.age", int.class);
        }));

        System.out.println(time(10000000, i -> {
            V.one(V.get(m, "person.age"), int.class);
        }));

        System.out.println(time(10000000, i -> {
            V.get(m, "person.age");
        }));

        System.out.println(time(10000000, i -> {
            if (m.containsKey("person.age"))
                Integer.parseInt((String) m.get("person.age"));
        }));
    }

    private static long time(long n, Consumer<Long> behavior) {
        final long warmup = n / 10000;
        long start = System.nanoTime();
        for (long i = 0; i < n + warmup; ++i) {
            if (i == warmup)
                start = System.nanoTime();
            behavior.accept(i);
        }
        return (System.nanoTime() - start) / (long) 1e6;
    }

}
