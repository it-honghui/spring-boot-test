package cn.gathub.test.java8;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import cn.gathub.test.model.Employee;

import static java.util.stream.Collectors.toList;

public class LambdaTest {

  @Test
  public void test1() {
    List<String> nameStrs = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur");

    List<String> list = nameStrs.stream()
        .filter(s -> s.startsWith("L"))
        .map(String::toUpperCase)
        .sorted()
        .collect(toList());
    System.out.println(list);
  }

  /**
   * 数组、集合、文本转化为管道流
   */
  @Test
  public void test2() throws IOException {
    // 数组转化为管道流
    String[] array = {"Monkey", "Lion", "Giraffe", "Lemur"};
    Stream<String> nameStrs2 = Stream.of(array);

    Stream<String> nameStrs3 = Stream.of("Monkey", "Lion", "Giraffe", "Lemur");

    // 集合转化为管道流
    List<String> list = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur");
    Stream<String> streamFromList = list.stream();

    Set<String> set = new HashSet<>(list);
    Stream<String> streamFromSet = set.stream();

    // 文本转化为管道流
    Stream<String> lines = Files.lines(Paths.get("file.txt"));
  }

  /**
   * filter
   */
  @Test
  public void test3() {
    Employee e1 = new Employee(1, 23, "M", "Rick", "Beethovan");
    Employee e2 = new Employee(2, 13, "F", "Martina", "Hengis");
    Employee e3 = new Employee(3, 43, "M", "Ricky", "Martin");
    Employee e4 = new Employee(4, 26, "M", "Jon", "Lowman");
    Employee e5 = new Employee(5, 19, "F", "Cristine", "Maria");
    Employee e6 = new Employee(6, 15, "M", "David", "Feezor");
    Employee e7 = new Employee(7, 68, "F", "Melissa", "Roy");
    Employee e8 = new Employee(8, 79, "M", "Alex", "Gussin");
    Employee e9 = new Employee(9, 15, "F", "Neetu", "Singh");
    Employee e10 = new Employee(10, 45, "M", "Naveen", "Jain");

    List<Employee> employeeList = Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);

    List<Employee> collectList = employeeList.stream()
        .filter(e -> e.getAge() > 70 && "M".equals(e.getGender()))
        .collect(toList());

    System.out.println(collectList);

    // 另一种写法 并集
    List<Employee> and = employeeList.stream()
        .filter(Employee.ageGreaterThan70.and(Employee.genderM))
        .collect(Collectors.toList());

    // 或集
    List<Employee> or = employeeList.stream()
        .filter(Employee.ageGreaterThan70.or(Employee.genderM))
        .collect(Collectors.toList());

    // .negate() 取反
    List<Employee> negate = employeeList.stream()
        .filter(Employee.ageGreaterThan70.or(Employee.genderM).negate())
        .collect(Collectors.toList());
  }

  /**
   * map函数的作用就是针对管道流中的每一个数据元素进行转换操作。
   *
   * @throws IOException
   */
  @Test
  public void test4() throws IOException {
    List<String> alpha = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur");

    //不使用Stream管道流
    List<String> alphaUpper = new ArrayList<>();
    for (String s : alpha) {
      alphaUpper.add(s.toUpperCase());
    }
    System.out.println(alphaUpper); //[MONKEY, LION, GIRAFFE, LEMUR]

    // 使用Stream管道流
    List<String> collect = alpha.stream().map(String::toUpperCase).collect(Collectors.toList());
    //上面使用了方法引用，和下面的lambda表达式语法效果是一样的
    //List<String> collect = alpha.stream().map(s -> s.toUpperCase()).collect(Collectors.toList());

    System.out.println(collect); //[MONKEY, LION, GIRAFFE, LEMUR]
  }

  /**
   * map的一些方法
   */
  @Test
  public void test5() {
    List<String> alpha = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur");

    List<Integer> lengths = alpha.stream()
        .map(String::length)
        .collect(Collectors.toList());

    System.out.println(lengths); //[6, 4, 7, 5]

    Stream.of("Monkey", "Lion", "Giraffe", "Lemur")
        .mapToInt(String::length)
        .forEach(System.out::println);
  }

  /**
   * 由于map的参数e就是返回值，所以可以用peek函数。peek函数是一种特殊的map函数，当函数没有返回值或者参数就是返回值的时候可以使用peek函数。
   *
   * @throws IOException
   */
  @Test
  public void test6() {
    Employee e1 = new Employee(1, 22, "M", "Rick", "Beethovan");
    Employee e2 = new Employee(2, 12, "F", "Martina", "Hengis");
    Employee e3 = new Employee(3, 42, "M", "Ricky", "Martin");
    Employee e4 = new Employee(4, 25, "M", "Jon", "Lowman");
    Employee e5 = new Employee(5, 18, "F", "Cristine", "Maria");
    Employee e6 = new Employee(6, 14, "M", "David", "Feezor");
    Employee e7 = new Employee(7, 67, "F", "Melissa", "Roy");
    Employee e8 = new Employee(8, 78, "M", "Alex", "Gussin");
    Employee e9 = new Employee(9, 15, "F", "Neetu", "Singh");
    Employee e10 = new Employee(10, 45, "M", "Naveen", "Jain");

    List<Employee> employees = Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);

//        List<Employee> maped = employees.stream()
//                .map(e -> {
//                    e.setAge(e.getAge() + 1);
//                    e.setGender(e.getGender().equals("M") ? "male" : "female");
//                    return e;
//                }).collect(Collectors.toList());

    List<Employee> maped = employees.stream()
        .peek(e -> {
          e.setAge(e.getAge() + 1);
          e.setGender(e.getGender().equals("M") ? "male" : "female");
        }).collect(Collectors.toList());

    System.out.println(maped);
  }

  /**
   * 如何处理二维数组及二维集合类，实现一个简单的需求：将“hello”，“world”两个字符串组成的集合，元素的每一个字母打印出来。
   * 如果管道中还有管道该如何处理，用map方法是做不到的，这个需求用map方法无法实现。map只能针对一维数组进行操作，数组里面还有数组，管道里面还有管道，它是处理不了每一个元素的。
   *
   * @throws IOException
   */
  @Test
  public void test7() {
    List<String> words = Arrays.asList("hello", "word");
    words.stream()
        .map(w -> Arrays.stream(w.split("")))    //[[h,e,l,l,o],[w,o,r,l,d]]
        .forEach(System.out::println);
  }

  /**
   * flatMap可以理解为将若干个子管道中的数据全都，平面展开到父管道中进行处理。
   */
  @Test
  public void test8() {
    List<String> words = Arrays.asList("hello", "word");
    words.stream()
        .flatMap(w -> Arrays.stream(w.split("")))    //[[h,e,l,l,o],[w,o,r,l,d]]
        .forEach(System.out::println);
  }

  /**
   * Limit与Skip管道数据截取 limt方法传入一个整数n，用于截取管道中的前n个元素。经过管道处理之后的数据是：[Monkey, Lion]。
   * skip方法与limit方法的使用相反，用于跳过前n个元素，截取从n到末尾的元素。经过管道处理之后的数据是： [Giraffe, Lemur]
   */
  @Test
  public void test9() {
    List<String> limitN = Stream.of("Monkey", "Lion", "Giraffe", "Lemur")
        .limit(2)
        .collect(Collectors.toList());
    System.out.println(limitN);

    List<String> skipN = Stream.of("Monkey", "Lion", "Giraffe", "Lemur")
        .skip(2)
        .collect(Collectors.toList());
    System.out.println(skipN);
  }

  /**
   * distinct 去重 sorted 排序，是按照字母的自然顺序进行排序，第一位无法区分顺序，就比较第二位字母。
   */
  @Test
  public void test10() {
    List<String> uniqueAnimals = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .distinct()
        .collect(Collectors.toList());
    System.out.println(uniqueAnimals);

//        List<String> alphabeticOrder = Stream.of("Monkey", "Lion", "Giraffe", "Lemur")
    List<String> alphabeticOrder = Stream.of("1", "4", "3", "2")
        .sorted()
        .collect(Collectors.toList());
    System.out.println(alphabeticOrder);
  }

  /**
   * 串行、并行、順序 串行的好处是可以保证顺序，但是通常情况下处理速度慢一些 并行的好处是对于元素的处理速度快一些（通常情况下），但是顺序无法保证。这可能会导致进行一些有状态操作的时候，最后得到的不是你想要的结果。
   * parallel()函数表示对管道中的元素进行并行处理，而不是串行处理。但是这样就有可能导致管道流中后面的元素先处理，前面的元素后处理，也就是元素的顺序无法保证。
   * 通常情况下，parallel()能够很好的利用CPU的多核处理器，达到更好的执行效率和性能，建议使用。 从处理性能的角度，parallel()更适合处理ArrayList，而不是LinkedList。因为ArrayList从数据结构上讲是基于数组的，可以根据索引很容易的拆分为多个。
   */
  @Test
  public void test11() {
    Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .parallel()
        .forEach(System.out::println);
  }

  /**
   * 字符串排序
   */
  @Test
  public void test12() {
    List<String> cities = Arrays.asList("Milan", "london", "San Francisco", "Tokyo", "New Delhi");
    System.out.println(cities);
    //[Milan, london, San Francisco, Tokyo, New Delhi]

    cities.sort(String.CASE_INSENSITIVE_ORDER);// 不区分大小写的顺序
    System.out.println(cities);
    //[london, Milan, New Delhi, San Francisco, Tokyo]

    cities.sort(Comparator.naturalOrder());// 自然字母顺序
    System.out.println(cities);
    //[Milan, New Delhi, San Francisco, Tokyo, london]

    cities.stream().sorted(Comparator.naturalOrder()).forEach(System.out::println);
    //Milan
    //New Delhi
    //San Francisco
    //Tokyo
    //london
  }

  /**
   * 按对象字段排序
   */
  @Test
  public void test13() {
    Employee e1 = new Employee(1, 23, "M", "Rick", "Beethovan");
    Employee e2 = new Employee(2, 13, "F", "Martina", "Hengis");
    Employee e3 = new Employee(3, 43, "M", "Ricky", "Martin");
    Employee e4 = new Employee(4, 26, "M", "Jon", "Lowman");
    Employee e5 = new Employee(5, 19, "F", "Cristine", "Maria");
    Employee e6 = new Employee(6, 15, "M", "David", "Feezor");
    Employee e7 = new Employee(7, 68, "F", "Melissa", "Roy");
    Employee e8 = new Employee(8, 79, "M", "Alex", "Gussin");
    Employee e9 = new Employee(9, 15, "F", "Neetu", "Singh");
    Employee e10 = new Employee(10, 45, "M", "Naveen", "Jain");

    List<Employee> employees = Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);

//        employees.sort(Comparator.comparing(Employee::getAge));
    employees.sort(Comparator.comparing(Employee::getAge).reversed());// 倒序
    employees.forEach(System.out::println);

    System.out.println("========================================================================");

    employees.sort(
        Comparator.comparing(Employee::getGender)
            .thenComparing(Employee::getAge)
            .reversed()
    );
    employees.forEach(System.out::println);

    //都是正序 ，不加reversed
    //都是倒序，最后面加一个reserved
    //先是倒序（加reserved），然后正序
    //先是正序（加reserved），然后倒序（加reserved）
  }

  /**
   * 自定义Comparator排序
   */
  @Test
  public void test14() {
    Employee e1 = new Employee(1, 23, "M", "Rick", "Beethovan");
    Employee e2 = new Employee(2, 13, "F", "Martina", "Hengis");
    Employee e3 = new Employee(3, 43, "M", "Ricky", "Martin");
    Employee e4 = new Employee(4, 26, "M", "Jon", "Lowman");
    Employee e5 = new Employee(5, 19, "F", "Cristine", "Maria");
    Employee e6 = new Employee(6, 15, "M", "David", "Feezor");
    Employee e7 = new Employee(7, 68, "F", "Melissa", "Roy");
    Employee e8 = new Employee(8, 79, "M", "Alex", "Gussin");
    Employee e9 = new Employee(9, 15, "F", "Neetu", "Singh");
    Employee e10 = new Employee(10, 45, "M", "Naveen", "Jain");

    List<Employee> employees = Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);

    employees.sort(new Comparator<Employee>() {
      @Override
      public int compare(Employee em1, Employee em2) {
        if (em1.getAge() == em2.getAge()) {
          return 0;
        }
        return em1.getAge() - em2.getAge() > 0 ? -1 : 1;
      }
    });
    employees.forEach(System.out::println);

    // 以lambda表达式简写
    employees.sort((em1, em2) -> {
      if (em1.getAge() == em2.getAge()) {
        return 0;
      }
      return em1.getAge() - em2.getAge() > 0 ? -1 : 1;
    });
    employees.forEach(System.out::println);
  }

  /**
   * Map的排序
   */
  @Test
  public void test15() {
    // 创建一个Map，并填入数据
    Map<String, Integer> codes = new HashMap<>();
    codes.put("United States", 1);
    codes.put("Germany", 49);
    codes.put("France", 33);
    codes.put("China", 86);
    codes.put("Pakistan", 92);

    // 按照Map的键进行排序
    Map<String, Integer> sortedMap = codes.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldVal, newVal) -> oldVal,
                LinkedHashMap::new
            )
        );

    // 将排序后的Map打印
    sortedMap.entrySet().forEach(System.out::println);

    // sort the map by values
    Map<String, Integer> sorted = codes.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldVal, newVal) -> oldVal,
            LinkedHashMap::new));

    sorted.entrySet().forEach(System.out::println);
  }

  /**
   * ForEach和ForEachOrdered parallel 利用系统多核CPU多线程执行，不能保证顺序 ForEachOrdered 加上了排序，可以保证顺序
   */
  @Test
  public void test16() {
    Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .parallel()
        .forEach(System.out::println);

    System.out.println("=================================");

    Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .parallel()
        .forEachOrdered(System.out::println);
  }

  /**
   * 元素的收集collect 通用的元素收集方式
   */
  @Test
  public void test17() {
    // 最终collectToSet 中的元素是:[Monkey, Lion, Giraffe, Lemur]，注意Set会去重。
    Set<String> collectToSet = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .collect(Collectors.toSet());

    // 最终collectToList中的元素是: [Monkey, Lion, Giraffe, Lemur, Lion]
    List<String> collectToList = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .collect(Collectors.toList());

    // 最终collectToCollection中的元素是: [Monkey, Lion, Giraffe, Lemur, Lion]
    LinkedList<String> collectToCollection = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .collect(Collectors.toCollection(LinkedList::new));

    List<String> collectToArrayList = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .collect(Collectors.toCollection(ArrayList::new));

    // 最终toArray字符串数组中的元素是: [Monkey, Lion, Giraffe, Lemur, Lion]
    String[] toArray = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .toArray(String[]::new);

    // 最终toMap的结果是: {Monkey=6, Lion=4, Lemur=5, Giraffe=6}
    Map<String, Integer> toMap = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .distinct()
        .collect(Collectors.toMap(
            Function.identity(), //元素输入就是输出，作为key  key -> key,
            s -> (int) s.chars().distinct().count() // 输入元素的不同的字母个数，作为value
        ));
  }

  /**
   * 分组收集groupingBy
   */
  @Test
  public void test18() {
    Map<Character, List<String>> groupingByList = Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
        .collect(Collectors.groupingBy(
            s -> s.charAt(0)  // 根据元素首字母分组，相同的在一组
            // counting()        // 加上这一行代码可以实现分组统计
        ));

    // 最终groupingByList内的元素: {G=[Giraffe], L=[Lion, Lemur, Lion], M=[Monkey]}
    // 如果加上counting() ，结果是:  {G=1, L=3, M=1}
  }

  /**
   * 其他常用方法
   */
  @Test
  public void test19() {
    // 判断管道中是否包含2，结果是: true
    boolean containsTwo = IntStream.of(1, 2, 3).anyMatch(i -> i == 2);

    // 管道中元素数据总计结果nrOfAnimals: 4
    long nrOfAnimals = Stream.of("Monkey", "Lion", "Giraffe", "Lemur").count();

    // 管道中元素数据累加结果sum: 6
    int sum = IntStream.of(1, 2, 3).sum();

    // 管道中元素数据平均值average: OptionalDouble[2.0]
    OptionalDouble average = IntStream.of(1, 2, 3).average();

    // 管道中元素数据最大值max: 3
    int max = IntStream.of(1, 2, 3).max().orElse(0);

    // 全面的统计结果statistics: IntSummaryStatistics{count=3, sum=6, min=1, average=2.000000, max=3}
    IntSummaryStatistics statistics = IntStream.of(1, 2, 3).summaryStatistics();
  }

  @Test
  public void test20() {
    List<String> list = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur", "Lion");
    list.stream()
        .filter(x -> x.startsWith("L"))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Test
  public void test21() {
    String lastOrderId = "Lemur";
    List<String> list = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur", "Lion");

    Set<String> saveCcOrderList = new HashSet<>();
    for (int i = 0; i < 5; i++) {
      for (String cc : list) {
        if (!cc.equals(lastOrderId)) {
          saveCcOrderList.add(cc);
        } else {
          break;
        }
      }
    }

    saveCcOrderList.forEach(System.out::println);
  }

  /**
   * 测试调试工具
   */
  @Test
  public void test22() {
    int[] arr = IntStream.of(2, 1, 6, 8, 10, 5, 7, 3, 4, 9)
        .filter(x -> x > 2)
        .map(x -> x * 2)
        .distinct()
        .sorted()
        .toArray();
  }

}