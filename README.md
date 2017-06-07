Work immutably, robustly and in a knowledge-oriented way with Entity maps in Java.
See [DemoTest](https://github.com/atdixon/vroom/blob/master/quality/src/test/java/test/vroom/DemoTest.java)
for example usages.

Available in Maven Central. Use:

    <dependency>
        <groupId>com.github.atdixon</groupId>
        <artifactId>vroom</artifactId>
        <version>0.3.12</version>
    </dependency>

##### Benchmark
    
To run [jmh](http://openjdk.java.net/projects/code-tools/jmh/) benchmarks comparing VMap 
with correlate java.util.Map operations, use:

    $ mvn clean install -Pbenchmark

