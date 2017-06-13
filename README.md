Work immutably, robustly and in a knowledge-oriented way with Entity maps in Java.
See [DemoTest](https://github.com/atdixon/vivean/blob/master/quality/src/test/java/test/vivean/DemoTest.java)
for example usages.

Available in Maven Central. Use:

    <dependency>
        <groupId>com.github.atdixon</groupId>
        <artifactId>vivean</artifactId>
        <version>0.4.0-beta</version>
    </dependency>

##### Benchmark
    
To run [jmh](http://openjdk.java.net/projects/code-tools/jmh/) benchmarks comparing VMap 
with correlate java.util.Map operations, use:

    $ mvn clean install -Pbenchmark

