package com.github.atdixon.vroom2;

import com.google.common.collect.Lists;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Arrays.asList;

@Deprecated
public class __Driver {

    static final class Qua {}
    static abstract class Qui {}
    abstract class Quo {}
    interface Que {}

    static class FooTypeReference extends TypeReference<Integer> {}
    static class BooTypeReference extends FooTypeReference {}

    static class WahWah<T> {
        public class Woh<U> {
            U foo() {
                throw new UnsupportedOperationException();
            }
        }
        public Woh<Integer> anIntegerWoh() {
            return new Woh<Integer>() {};
        }
    }

    static <T> T coerce(Object value, TypeReference<T> as) {
        throw new RuntimeException();
    }

    public static void dmain(String[] args) {
        List foo =(List) asList(1, 2);
        System.out.println(foo.stream()
            .filter(Objects::nonNull)
            .findFirst());


        System.out.println(Modifier.isAbstract(Qua.class.getModifiers()));
        System.out.println(Modifier.isAbstract(Qui.class.getModifiers()));
        System.out.println(Modifier.isAbstract(Quo.class.getModifiers()));
        System.out.println(Modifier.isAbstract(Que.class.getModifiers()));

    }

    public static void main(String[] args) {
        VMap m = VMap.create();
        System.out.println(m.many("age", Integer.class));
        System.out.println(m.one("age", new TypeReference<List<Integer>>() {}));
        m = m.assoc("age", asList(1, 2));
        System.out.println(m.many("age", Integer.class));
        System.out.println(m.one("age", new TypeReference<List<Integer>>() {}));
        System.out.println(m.one("age", Integer.class));
        System.out.println(m.many("age", int.class));
    }

    public static void xmain(String[] args) {

//        List<String> answer = coerce(new String[]{}, TypeReferences.list(String.class));
//        List<String> a2 = coerce(new String[]{}, new TypeReference<List<String>>() {});

        // getting an array type for conversion
        System.out.println(new Integer[0].getClass().getComponentType());

        TypeReference<?> cat = new TypeReference<Map<String, Object>>() {};
        TypeReference<?> dog = new TypeReference<List<String>[]>() {};
        TypeReference<?> ear = new TypeReference<WahWah.Woh>() {};

        Object iwoh = new WahWah<String>()
            .anIntegerWoh();

        _dump("cat", cat);
        _dump("dog", dog);
        _dump("ear", ear);
        _dump("woh", iwoh.getClass().getGenericSuperclass());



        VMap m = VMap.create();

//        Optional<Map<String, Object>> foo
//            = m.one("foo", new TypeReference<Map<String, Object>>() {});

//        final Optional<Integer> foo =
//            m.one("age", OptionalTypeReference.of(Integer.class));
//        System.out.println(foo);
//
//
//        m.one("peer", new TypeReference<Map<String, Object>>() {});
    }

    private static void _dump(String memo, TypeReference<?> tr) {
        final Type type = tr.getType();
        _dump(memo, type);
    }

    private static void _dump(String memo, Object o) {
        final Type sc = o.getClass().getGenericSuperclass();
        if (sc instanceof ParameterizedType) {
            final Type type = ((ParameterizedType) sc).getActualTypeArguments()[0];
            _dump(memo, type);
        } else {
            System.out.println(memo);
            System.out.println("===");
            System.out.println("class: " + o.getClass() + " <<" + o.getClass().getClass().getClass() + ">>");
            System.out.println("genericSuperclass: " + sc + " <" + sc.getClass() + ">");
        }
    }

    private static void _dump(String memo, Type type) {
        System.out.println(memo);
        System.out.println("---");
        System.out.println(type + " <" + type.getClass() + ">");
        if (type instanceof ParameterizedType) {
            final ParameterizedType as = (ParameterizedType) type;
            System.out.println("rawType: " + as.getRawType() + " <" + as.getRawType().getClass() + ">");
            System.out.println("ownerType: " + as.getOwnerType() + " <" + (as.getOwnerType() == null ? "null" : as.getOwnerType().getClass()) + ">");
            final List<String> actualTypeArguments = Lists.newArrayList();
            asList(as.getActualTypeArguments()).forEach(t ->
                actualTypeArguments.add(t + " <" + t.getClass() + ">"));
            System.out.println("actualTypeArguments: "
                + actualTypeArguments);
        }
    }

}
