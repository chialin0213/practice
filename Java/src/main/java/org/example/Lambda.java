package org.example;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lambda {
    private static final boolean supplierConsumerTest = false ;
    private static final boolean predicateTest = false ;
    private static final boolean functionTest = true ;

    public static void main(String[] args) {
        if(supplierConsumerTest) supplierConsumerTest();
        if(predicateTest) predicateTest();
        if(functionTest) functionTest();
    }

    /**
     *
     * Java中如果一個接口中有且只有一個抽象方法，那麼這個接口就是函數式接口
     *
     * @FunctionalInterface
     * public interface Supplier<T> {
     *     T get();
     * }
     *
     *@FunctionalInterface
     * public interface Consumer<T> {
     *      void accept(T t)
     * }
     */
    public static void supplierConsumerTest(){
        Supplier<String> hello = () -> "Hello World!";
        System.out.println("hello="+hello.get());

        Country country = new Country();
        Supplier<String> s = country::getPMName;
        Consumer<String> c = country::printMessage;
        c.accept(s.get());

        Supplier<Integer> sint = () -> new Random().nextInt(23);
        System.out.println(sint.get());
    }

    /**
     * @FunctionalInterface
     * public interface Predicate<T> {
     *      boolean test(T t);
     * }
     */
    public static void predicateTest(){
        Stream<Integer> iStream = Stream.of(1, 3, 4, 5, 6, 7, 55, 432, 55);
        Predicate<Integer> ft = n -> n > 5 ;

        iStream.filter(ft).forEach(System.out::println);
    }

    public static void functionTest(){
        Stream<String> str = Stream.of("aaa", "bbb", "ccc");
        Function<String,String> sf = (s) -> s.substring(0,1);
        List<String> collect = str.map(sf).peek(System.out::println).collect(Collectors.toList());
    }

    static class Country {
        public String getPMName() {
            return "Narendra Modi";
        }
        public void printMessage(String msg) {
            System.out.println(msg);
        }
    }
}
