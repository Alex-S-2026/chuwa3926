# HW4 Java 8 New Features Homework

- alex_shen
- 2026-3-18
- HW4 Java 8 New Features Homework
- Topics Covered:
    - Default & Static Methods in Interface
    - Functional Interface
    - Lambda Expressions
    - Method Reference
    - Optional
    - Stream API
- Submission: Submit your answers as a single document or code files

# Conceptual Questions

## Q1. What is a Functional Interface? What annotation is used to mark a Functional Interface, and is this annotation mandatory? Give two examples of built-in Functional Interfaces in Java 8 and explain their abstract method signatures.

A functional interface is an interface with exactly one abstract method. It can have default and static methods too. You can implement it with a lambda or method reference.

You mark it with `@FunctionalInterface`. The annotation isn’t required—any interface with one abstract method is already treated as functional—but it makes the intent clear and lets the compiler check that you don’t add a second abstract method.

Two common ones from Java 8: `Predicate<T>` has `boolean test(T t)` — one argument, returns true/false, used a lot for filtering. `Function<T, R>` has `R apply(T t)` — one argument in, one value out, used for mapping/transforming.





## Q2. What will be the output of the following code? Explain why.
```
interface Greeting {
    default void sayHello() {
        System.out.println("Hello from Greeting");
    }
}
interface Farewell {
    default void sayHello() {
        System.out.println("Hello from Farewell");
    }
}
class Person implements Greeting, Farewell {
    @Override
    public void sayHello() {
        Greeting.super.sayHello();
        System.out.println("And hello from Person");
    }
}
public class Test {
    public static void main(String[] args) {
        Person p = new Person();
        p.sayHello();
    }
}
```

Output:
```
Hello from Greeting
And hello from Person
```

Person implements both interfaces, and both have a default sayHello(), so the class has to override it. In the override it calls Greeting.super.sayHello() to pick Greeting’s version, then prints its own line. So we only see the Greeting message and the Person message; Farewell’s sayHello() is never used.



## Q3. Convert the following anonymous class to a lambda expression. Explain each step of the conversion process.
```
Comparator<String> comparator = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
};
```

You drop the anonymous class wrapper and keep just the parameters and the return expression. The method is compare(String s1, String s2) returning an int, so you get (s1, s2) and the body s1.length() - s2.length(). With a lambda you write an arrow between them: (s1, s2) -> s1.length() - s2.length(). The types are optional here because the compiler gets them from `Comparator<String>`.

Result:
```
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();
```



## Q4. Which of the following lambda expressions are valid? For invalid ones, explain the reason.
```
// A
Runnable r = () -> System.out.println("Running");
// B
Predicate<String> p = s -> return s.isEmpty();
// C
Function<Integer, Integer> f = x -> { x * 2; };
// D
Consumer<String> c = (String s) -> System.out.println(s);
// E
BiFunction<Integer, Integer, Integer> bi = (a, b) -> a + b;
// F
Supplier<String> sup = () -> { return "Hello"; };
```

A is valid. D, E, and F are valid too.

B is invalid: you can’t write `s -> return s.isEmpty();` because with the arrow form you either have a single expression (no return, no semicolon) like `s -> s.isEmpty()`, or a block like `s -> { return s.isEmpty(); }`.

C is invalid: in a block, `x * 2` by itself doesn’t return anything, the result is just thrown away. So you need either `x -> x * 2` or `x -> { return x * 2; }`.




## Q5. Match each lambda expression with its corresponding method reference. Explain the type of each method reference (Static, Bound Instance, Unbound Instance, or Constructor).

```
// Lambda expressions:
// 1. x -> System.out.println(x)
// 2. s -> s.toUpperCase()
// 3. x -> Math.abs(x)
// 4. () -> new ArrayList<>()
// 5. (s1, s2) -> s1.compareTo(s2)

// Method references:
// A. ArrayList::new
// B. System.out::println
// C. Math::abs
// D. String::toUpperCase
// E. String::compareTo
```

1 goes to B (System.out::println). The object is fixed (System.out), and we pass the argument to it—that’s a bound instance reference.

2 goes to D (String::toUpperCase). The reference doesn’t say which string; the first argument at call time is the receiver. That’s unbound instance.

3 goes to C (Math::abs). Static method, the argument is passed in. Static reference.

4 goes to A (ArrayList::new). No-arg constructor. Constructor reference.

5 goes to E (String::compareTo). First argument is the receiver, second is the argument to compareTo. Unbound instance.



## Q6. What is the difference between Optional.of() and Optional.ofNullable()? What will happen when executing the following code?
```
String value = null;
Optional<String> opt1 = Optional.of(value);
Optional<String> opt2 = Optional.ofNullable(value);
System.out.println(opt1.isPresent());
System.out.println(opt2.isPresent());
```

Optional.of() expects a non-null value; if you pass null it throws NullPointerException. Optional.ofNullable() is fine with null—it gives you an empty Optional instead.

In the code, value is null. So Optional.of(value) throws NullPointerException right away and the two printlns never run. If we only had ofNullable, opt2 would be empty and opt2.isPresent() would print false.




## Q7. What will be the output of the following code? Explain the difference between orElse() and orElseGet().
```
public class Test {
    public static String createDefault() {
        System.out.println("Creating default value");
        return "Default";
    }
    public static void main(String[] args) {
        Optional<String> opt = Optional.of("Hello");
        System.out.println("--- Using orElse ---");
        String result1 = opt.orElse(createDefault());
        System.out.println("Result: " + result1);
        System.out.println("--- Using orElseGet ---");
        String result2 = opt.orElseGet(() -> createDefault());
        System.out.println("Result: " + result2);
    }
}
```

Output:
```
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
```

In both cases the result is "Hello" because the Optional has a value. The difference is when the default is computed. orElse(createDefault()) always runs createDefault() when you call it—so we see "Creating default value" even though we don’t use it. orElseGet(() -> createDefault()) only runs the supplier when the Optional is empty, so we never call createDefault() and that message doesn’t print. So orElseGet is lazy and better when creating the default is expensive or has side effects.



## Q8. Explain the difference between map() and flatMap() in Stream API. Given the following class structure, write code to get a list of all product names from all orders.
```
class Order {
    private List<Product> products;
    public List<Product> getProducts() { return products; }
}
class Product {
    private String name;
    public String getName() { return name; }
}
// Given: List<Order> orders
// Write code to get: List<String> allProductNames

```

map() turns each element into one new element, so you get a stream of the same length (or less if you filter). If your function returns a list, you end up with a stream of lists. flatMap() is for when each element can turn into multiple elements: your function returns a stream, and flatMap flattens all those streams into one. So you go from "each order has a list of products" to "one stream of all products" with flatMap, then get the names with map.

Code to get all product names from all orders:
```java
List<String> allProductNames = orders.stream()
    .flatMap(order -> order.getProducts().stream())
    .map(Product::getName)
    .collect(Collectors.toList());
```




## Q9. What will be the output of the following code? Explain the concept of lazy evaluation in Stream API.
```
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Stream<Integer> stream = numbers.stream()
    .filter(n -> {
        System.out.println("Filtering: " + n);
        return n % 2 == 0;
    })
    .map(n -> {
        System.out.println("Mapping: " + n);
        return n * 10;
    });
System.out.println("Stream created");
System.out.println("Calling findFirst...");
Optional<Integer> result = stream.findFirst();
System.out.println("Result: " + result.orElse(-1));
```

Output:
```
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20
```

Streams are lazy: the filter and map don’t actually run until you hit a terminal operation like findFirst(). So "Stream created" and "Calling findFirst..." print first. Then the pipeline runs just enough to get one result—it filters 1 (odd, skip), filters 2 (even, keep), maps 2 to 20, and findFirst() returns. It never touches 3, 4, or 5. That’s lazy evaluation: work is done only when needed.




## Q10. Analyze the following code. What is wrong with it? How would you fix it?
```
public class ProductService {
    public void processProducts(Optional<List<Product>> productsOpt) {
        if (productsOpt.isPresent()) {
            List<Product> products = productsOpt.get();
            for (Product p : products) {
                process(p);
            }
        }
    }
    public Optional<BigDecimal> calculateTotal(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return null;
        }
        BigDecimal total = products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Optional.of(total);
    }
}
```

A few issues. First, taking Optional as a parameter is usually a bad idea—it’s awkward for callers and doesn’t add much. Better to have processProducts take a `List<Product>` (or null) and do the null/empty check inside.

Second, calculateTotal returns `Optional<BigDecimal>` but when the list is null or empty it returns null. That’s wrong—callers expect an Optional and might get NPE. It should return Optional.empty() instead of null.

Third, the code uses Product::getPrice, so the Product class needs a getPrice() that returns BigDecimal. If it’s not there, you need to add it.



# Programming Questions

## Q11. Create an order processing system using Java 8 features:
1. Create a Product class with fields: `String id, String name, BigDecimal price, String category, boolean available`.
2. Create an Order class with fields: `String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail`.
3. Create an OrderProcessor interface with:
- A default method BigDecimal calculateTotal(Order order) that calculates the total price of all products in an order
- A static method String formatPrice(BigDecimal price) that formats a price to a currency string (e.g., "$99.99")
- An abstract method void processOrder(Order order)
4. Create an OrderService class that:
- Implements OrderProcessor
- Has a method `List<Order> filterOrders(List<Order> orders, Predicate<Order> condition)` that uses the predicate to filter orders
- Has a method `Map<String, List<Order>> groupOrdersByCategory(List<Order> orders)` that groups orders by the category of their first product
- Has a method `Optional<Order> findMostExpensiveOrder(List<Order> orders)` that returns the order with the highest total
5. Write a main method that demonstrates:
- Creating several products and orders
- Filtering orders with total > $100 using lambda expression
- Grouping orders by category
- Finding the most expensive order and handling the Optional result
- Using method references where appropriate




## Q12. Implement a data analysis utility using Stream API and Optional:
1. Create a Student class with fields: `String id, String name, int age, String major, List<Double> scores`.
2. Create a StudentAnalyzer class with the following methods (all must use Stream API):
- `List<String> getTopStudentNames(List<Student> students, int n)` - Returns names of top n students by average score, sorted in descending order
- `Map<String, Double> getAverageScoreByMajor(List<Student> students)` - Returns a map of major to average score across all students in that major
- `Optional<Student> findStudentWithHighestSingleScore(List<Student> students)` - Returns the student who has the highest single score among all their scores
- `List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major)` - Returns students whose average score is above the average of their major
- `Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore)` - Partitions students into those who passed (average >= passingScore) and those who failed
3. Write a main method that:
- Creates at least 6 students with different majors and varying scores
- Calls each method and prints the results
- Properly handles Optional values using ifPresent(), orElse(), or orElseThrow()
- Uses method references wherever possible


