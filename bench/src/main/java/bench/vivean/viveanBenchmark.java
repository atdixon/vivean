package bench.vivean;

import com.github.atdixon.vivean.VMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class viveanBenchmark {

    @State(Scope.Thread)
    public static class ThreadState {
        volatile VMap vmap;
        volatile Map<String, Object> map;
        volatile int counter = 0;

        @Setup public void setup() {
            ++counter;
            vmap = VMap.create(new HashMap<String, Object>() {{
                put("key", emptyList());
                put("key/i", 100);
                put("key/i*", IntStream.range(0, 10_000).boxed().collect(toList()));
            }});
            map = new HashMap<String, Object>() {{
                put("key", emptyList());
                put("key/i", 100);
                put("key/i*", IntStream.range(0, 10_000).boxed().collect(toList()));
            }};
        }
    }

    // -- vmap ops --

    @Benchmark
    public void one_i(ThreadState state) {
        Integer v = state.vmap.one("key/i", int.class).get();
    }

    @Benchmark
    public void many_i(ThreadState state) {
        List<Integer> v = state.vmap.many("key/i*", int.class)
            .stream()
            .map(i -> i + 1)
            .collect(toList());
    }

    @Benchmark
    public void assoc(ThreadState state) {
        state.vmap.assoc("key", emptyList());
    }

    @Benchmark
    public void without(ThreadState state) {
        state.vmap.without("key");
    }

    // -- correlate map ops --

    @Benchmark
    public void get_i(ThreadState state) {
        Integer v = (Integer) state.map.get("key/i");
    }

    @Benchmark @SuppressWarnings("unchecked")
    public void get_ilist(ThreadState state) {
        List<Integer> v = ((List<Integer>) state.map.get("key/i*"))
            .stream()
            .map(i -> i + 1)
            .collect(toList());
    }

    @Benchmark
    public void put(ThreadState state) {
        state.map.put("key", emptyList());
    }

    @Benchmark
    public void remove(ThreadState state) {
        state.map.remove("key");
    }

}
