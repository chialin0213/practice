package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 以將Stream流操作分為3種型別：
 * 前 - 建立Stream
 * 中 - Stream中間處理
 * 後 - 終止Steam
 *
 * Java 8 stream 不可重複使用
 */
public class StreamAPI {
    private static boolean createStream = false;
    private static boolean flatmap_base_test = false ;
    private static boolean flatmap_advance1_test = false ;
    private static boolean testPeekAndForeach = false ;
    private static boolean testCollectJoinStrings = false ;
    private static boolean testNumberCalculate = false ;
    private static boolean testDelay = true ;

    public static void main(String[] args) {
        if(createStream) createStream();
        if(flatmap_base_test) flatmap_base_test();
        if(flatmap_advance1_test) flatmap_advance1_test();
        if(testPeekAndForeach) testPeekAndforeach();
        if(testCollectJoinStrings) testCollectJoinStrings();
        if(testNumberCalculate) testNumberCalculate();
        if(testDelay) testDelay();
    }

    /**
     * 中間運算的有個重要特性 —— 延遲性
     */
    public static void testDelay(){
        //只有中間操作，所以system.out不會報行
        Stream.of("s1", "s2", "s3", "s4", "s5")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                });

        //加入終止操作
        Stream.of("a1", "b2", "c1", "d3", "e5")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s));

        //執行順序，先針對第1個素:d2，先map->轉大寫，再anyMatch->是否大A
        //而不是先跑完map(要跑4次)再跑anyMatch
        //以此例子來看，map只需跑2次就結束了，因為第2次:a2就己符合anyMatch的條件
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase(); // 转大写
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A"); // 过滤出以 A 为前缀的元素
                });

        //map會跑5次
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase(); // 转大写
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("A"); // 过滤出以 A 为前缀的元素
                })
                .forEach(s -> System.out.println("forEach: " + s)); // for 循环输出

        //掉換filter及map順序，map只會跑1次
        //所以這些小技巧對於stream中存在大量元素來說，是非常很有用的。
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter1: " + s);
                    return s.startsWith("a"); // 过滤出以 A 为前缀的元素
                })
                .map(s -> {
                    System.out.println("map1: " + s);
                    return s.toUpperCase(); // 转大写
                })
                .forEach(s -> System.out.println("forEach: " + s)); // for 循环输出

        //sorted是水平執行的。因此，在這種情況下，sorted會對集合中的元素組合呼叫八次
        Stream.of("d2", "a2", "b1", "b3", "c")
                .sorted((s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2); // 排序
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a"); // 过滤出以 a 为前缀的元素
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase(); // 转大写
                })
                .forEach(s -> System.out.println("forEach: " + s));

        //filter移到前面，sorted只需執行一次
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .sorted((s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }

    /**
     * 數學運算統計
     */
    public static void testNumberCalculate() {
        List<Integer> ids = Arrays.asList(10, 20, 30, 40, 50);
        // 計算平均值
        Double average = ids.stream().collect(Collectors.averagingInt(value -> value));
        System.out.println("平均值：" + average);
        // 資料統計資訊
        IntSummaryStatistics summary = ids.stream().collect(Collectors.summarizingInt(value -> value));
        System.out.println("資料統計資訊： " + summary);
    }

    /**
     * 拼接List成字串by逗點
     */
    public static void testCollectJoinStrings() {
        List<String> ids = Arrays.asList("205", "10", "308", "49", "627", "193", "111", "193");
        String joinResult = ids.stream().collect(Collectors.joining(","));
        System.out.println("拼接後：" + joinResult);
    }

    /**
     * peek and foreach
     * 都可以用於對元素進行遍歷然後逐個的進行處理
     * peek屬於中間方法，而foreach屬於終止方法
     */
    public static void testPeekAndforeach() {
        List<String> sentences = Arrays.asList("hello world","Jia Gou Wu Dao");
        // 僅peek操作，最終不會執行
        System.out.println("----before peek----");
        sentences.stream().peek(sentence -> System.out.println(sentence));
        System.out.println("----after peek----");

        // 僅foreach操作，最終會執行
        System.out.println("----before foreach----");
        sentences.stream().forEach(sentence -> System.out.println(sentence));
        System.out.println("----after foreach----");

        // peek操作後面增加終止操作(也是要看什麼終止操作，count就不會)，peek會執行
        System.out.println("----before peek and collect----");
        sentences.stream().peek(sentence -> System.out.println(sentence)).collect(Collectors.toList());
        System.out.println("----after peek and collect----");
    }

    /**
     * 將一個stream中的每個元素映射到另一個stream中，然後將所有映射出來的stream合併起來成為一個新的stream
     * flatMap操作的時候其實是先每個元素處理並返回一個新的Stream，然後將多個Stream展開合併為了一個完整的新的Stream，
     */
    public static void flatmap_base_test(){
        List<String> words = Arrays.asList("hello", "world");

        words.stream()
                //["h","e","l","l","o"]["w","o","r","l","d"]
                .map(s -> s.split(""))
                //["h","e","l","l","o","w","o","r","l","d"]
                .flatMap(s -> Arrays.stream(s))
                .forEach(s -> System.out.println("flatmap="+s));

        /* 同上面，只是少寫一層map
        words.stream()
                .flatMap(s -> Arrays.stream(s.split("")))
                .forEach(s -> System.out.println("s="+s));
         */
    }

    public static void flatmap_advance1_test(){
        /**
         * 给定列表[1, 2, 3]和列表[3, 4]，
         * 返回[(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]。
         */
        List<Integer> l1 = Arrays.asList(1,2,3);
        List<Integer> l2 = Arrays.asList(3,4);

        List<int[]> result = l1.stream()
                //stream<int[]{1,3}>,stream<int[]{1,4}>,stream<int[]{2,3}>,stream<int[]{2,4}>,stream<int[]{3,3}>,stream<int[]{3,4}>
                .flatMap(n1 -> l2.stream().map(n2 -> new int[]{n1, n2}))
                .collect(Collectors.toList());
        result.stream().forEach(n -> System.out.println("("+n[0]+","+n[1]+")"));
    }

    // 創建 stream 方式
    public static void createStream(){
        //1. 空stream
        Stream<String> streamEmpty = Stream.empty();

        //2. Collection stream
        List<String> list = Arrays.asList("aa", "bb", "cc");
        Stream<String> stream = list.stream();

        //3.stream 來源
        Stream<String> streamOfArray = Stream.of("a", "b", "c");

        //4.現有陳列
        String[] arr = new String[]{"a", "b", "c"};
        Stream<String> streamOfArrayFull = Arrays.stream(arr);
        Stream<String> streamOfArrayPart = Arrays.stream(arr, 1, 3);

        //5.stream builder
        Stream<String> build = Stream.<String>builder().add("a").add("b").add("c").build();

        //6.stream generate
        //應指定所需的size大小
        Stream<String> streamGenerated  = Stream.generate(() -> "aa").limit(10);

        //7.stream iterate
        Stream<Integer> streamIterated  = Stream.iterate(1, n -> n + 2).limit(20);

        //8.基本類型stream
        //IntStream, LongStream, DoubleStream.
        IntStream intStream = IntStream.range(1, 3);

        //Random stream
        Random random = new Random();
        DoubleStream doubleStream = random.doubles(3);

        //String stream
        IntStream streamOfChars = "abc".chars();

        Stream<String> streamOfString = Pattern.compile(", ").splitAsStream("a, b, c");

        //檔案stream
        Path path = Paths.get("C:\\file.txt");
        try {
            Stream<String> streamOfStrings = Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
