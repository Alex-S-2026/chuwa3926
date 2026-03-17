# HW3

- alex_shen
- 2026-3-16
- Java OOP Homework 3
- Topics Covered:
    - Exception Handling
    - Enums
    - Class Relationships (Aggregation & Composition)
    - Singleton Pattern
- Submission: Submit your answers as a single document or code files

# Conceptual Questions

## Q1. Draw and explain the Java Exception Hierarchy. What are the two main branches under Throwable? Give two examples for each branch.

**Java Exception Hierarchy:**

```
                   Throwable
                   |       |
               Error       E  x  c  e  p  t  i  o  n
               |           |                       |
StackOverflowError         Checked Exceptions      RuntimeException
OutOfMemoryError           |                       |
VirtualMachineError        IOException             Unchecked Exceptions
                           SQLException            | 
                           FileNotFoundException   NPE (NullPointerException)
                                                   IndexOutOfBounds
                                                   ClassCast
```

The two main branches under **Throwable** are:

1. **Error** – serious problems that applications typically should not try to catch (e.g., JVM or system failures).
   - Examples: `OutOfMemoryError`, `StackOverflowError`

2. **Exception** – conditions that applications might want to catch and handle.
   - Examples: `IOException`, `SQLException` (checked); or `NullPointerException`, `IllegalArgumentException` (unchecked, under RuntimeException)



## Q2. What is the difference between Checked Exception and Unchecked Exception? Which one does the compiler force you to handle? Give two examples of each type.

- **Checked Exception**: Subclasses of `Exception` (but not of `RuntimeException`). The compiler **forces** you to handle them—either with `try-catch` or by declaring `throws` in the method signature. They usually represent recoverable external conditions (I/O, network, etc.).
  - Examples: `IOException`, `SQLException`

- **Unchecked Exception**: Subclasses of `RuntimeException` (and of `Error`). The compiler does **not** require handling. They often indicate programming bugs (null reference, invalid argument, etc.).
  - Examples: `NullPointerException`, `IllegalArgumentException`

**Summary:** The compiler forces you to handle **checked exceptions** only.



## Q3. What will be the output of the following code? Explain why.
```
public class ExceptionTest {
    public static int test() {
        try {
            System.out.println("try");
            return 1;
        } catch (Exception e) {
            System.out.println("catch");
            return 2;
        } finally {
            System.out.println("finally");
            return 3;
        }
    }
    public static void main(String[] args) {
        System.out.println(test());
    }
}
```

Output:
```
try
finally
3
```

The `try` block runs and prints "try", then hits `return 1`. Before the method actually returns, the `finally` block always runs and prints "finally". The `finally` block then executes `return 3`, which **overrides** the `return 1` from `try`. So the method returns 3, and `main` prints `3`. The `catch` block never runs because no exception is thrown.



## Q4. What is the difference between throw and throws? Write a short code example demonstrating both.

- **throw**: A keyword used **inside** a method to actually create and throw an exception object. We write `throw new SomeException("message");`.
- **throws**: A keyword used in the **method signature** to declare that the method may throw one or more checked exceptions. Callers must handle or declare those exceptions.

**Example:**
```java
// throws = declaration in method signature
public static void parseAge(String s) throws NumberFormatException {
    if (s == null || s.isEmpty())
        throw new IllegalArgumentException("Input cannot be null or empty");  // throw = actually throwing
    int age = Integer.parseInt(s);  // may throw NumberFormatException
    if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
}
```



## Q5. What will happen when you try to compile and run this code?
```
public class Test {
    public static void main(String[] args) {
        try {
            throw new RuntimeException("Error 1");
        } catch (Exception e) {
            throw new RuntimeException("Error 2");
        } finally {
            throw new RuntimeException("Error 3");
        }
    }
}
```

The code compiles and runs. In execution:
1. `try` throws `RuntimeException("Error 1")`.
2. It is caught by `catch`, which then throws `RuntimeException("Error 2")`.
3. Before that new exception propagates, `finally` runs and throws `RuntimeException("Error 3")`.

When `finally` throws, that exception **replaces** the one from `catch`. So the exception that propagates to `main` is **"Error 3"**, and the program terminates with that exception (stack trace showing "Error 3").



## Q6. Can an Enum extend another class? Can an Enum implement an interface? Explain why.

No. In Java, every enum implicitly extends `java.lang.Enum`. Because Java allows only single inheritance, an enum cannot also extend another class.

Yes. Implementing an interface does not use the single-inheritance slot; a class (or enum) can implement any number of interfaces. So enums can implement one or more interfaces and still extend `Enum`.



## Q7. What is the output of the following code?
```
enum Status {
    PENDING(0),
    PROCESSING(1),
    COMPLETED(2);
    private int code;
    Status(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
public class EnumTest {
    public static void main(String[] args) {
    for (Status s : Status.values()) {
        System.out.println(s.name() + " -> " + s.getCode() + " -> " + s.ordinal());
        }
    }
}
```

output:
```
PENDING -> 0 -> 0
PROCESSING -> 1 -> 1
COMPLETED -> 2 -> 2
```

`Status.values()` returns all enum constants in declaration order. For each constant, the code prints: `name()` (e.g. "PENDING"), `getCode()` (the constructor argument: 0, 1, 2), and `ordinal()` (position: 0, 1, 2). So each line is: name → code → ordinal.



## Q8. What is the difference between Aggregation and Composition? Complete the table below:
| Aspect                    | Aggregation | Composition |
|---------------------------|-------------|-------------|
| Relationship Type         | "has-a" (weak ownership) | "has-a / part-of" (strong ownership, part-of) |
| Lifecycle                 | Child can outlive parent | Child is created and destroyed with parent |
| Object Creation Location  | Outside the parent; passed in or referenced | Inside the parent; parent creates the child |
| UML Symbol                | Empty diamond (◇) on parent side | Filled diamond (◆) on parent side |
| Example                   | University has Professors (professors exist independently) | Car has Engine (engine is part of the car) |



## Q9. Look at the following code. Is the relationship between Library and Book an Aggregation or Composition? Explain your reasoning.
```
class Book {
    private String title;
    public Book(String title) { this.title = title; }
}
class Library {
    private List<Book> books = new ArrayList<>();
    public void addBook(Book book) {
        books.add(book);
    }
}
// Usage
Book b1 = new Book("Java Programming");
Library lib = new Library();
lib.addBook(b1);
```

Aggregation. The `Book` object `b1` is created **outside** the `Library` (`Book b1 = new Book("Java Programming");`). The library receives an existing book via `addBook(book)` and stores a reference to it. Books can exist without a library, and the same book could conceptually be in multiple collections. The library does not create or own the lifecycle of the books—it only holds references. So the relationship is aggregation (weak ownership), not composition (where the library would create and own the books).



## Q10. List the three key elements of the Singleton pattern. Why must the constructor be private?

1. **Private static instance** – Holds the single instance of the class.
2. **Private constructor** – Prevents other code from creating new instances with `new`.
3. **Public static getter** – Provides the only way to obtain the instance (e.g. `getInstance()`).

If the constructor were public, any code could call `new Singleton()` and create multiple instances, breaking the “single instance” guarantee. Making it private ensures that only the class itself can create the instance, and only through the controlled `getInstance()` method.



## Q11. What is the difference between Eager Initialization Singleton and Lazy Initialization Singleton? Which one creates the instance first?

- **Eager initialization:** The single instance is created when the class is loaded (e.g. `private static final Singleton instance = new Singleton();`). The instance exists as soon as the class is used, even if `getInstance()` is never called.

- **Lazy initialization:** The instance is created only when `getInstance()` is called for the first time (e.g. create inside `getInstance()` when `instance == null`). Until then, no instance exists.

**Eager initialization** creates the instance first (at class load time). Lazy initialization creates it later, on first use.



## Q12. Is this Singleton implementation correct? If not, explain the problem and how to fix it.
```
public class Singleton {
    private static Singleton instance;
    public Singleton() { }
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

No, this Singleton implementation is not correct.

**Problems:**
1. **Constructor is public.** Anyone can call `new Singleton()`, so multiple instances can be created and the singleton guarantee is broken.
2. **Not thread-safe (for lazy init).** Two threads can see `instance == null` at the same time and each create an instance, so you may get two different objects.

**Fixes:**
1. Make the constructor **private**: `private Singleton() { }`
2. For thread-safe lazy initialization, either:
   - Use `synchronized` on `getInstance()`, or
   - Use the “double-checked locking” pattern, or
   - Use the Bill Pugh (static inner class) approach so the instance is created in a thread-safe way at first use.



# Programming Questions

## Q13. Create a custom exception called InsufficientBalanceException that extends Exception. Then create a Wallet class with:
- Private field: balance (double)
- Constructor that sets initial balance
- deposit(double amount) method - throws IllegalArgumentException if amount <= 0
- withdraw(double amount) method - throws InsufficientBalanceException if balance < amount
- getBalance() method

Write a main method that:
1. Creates a wallet with $100
2. Deposits $50
3. Tries to withdraw $200 (should throw exception)
4. Catches the exception and prints an appropriate message
5. Prints the final balance

**Answer:** Code in `Coding/hw3_code/Q13_Wallet/`
- `InsufficientBalanceException.java` – custom exception extending Exception
- `Wallet.java` – balance, constructor, deposit (throws IllegalArgumentException if amount <= 0), withdraw (throws InsufficientBalanceException), getBalance(), and main as specified
- Run: `javac InsufficientBalanceException.java Wallet.java && java Wallet`



## Q14. Create an Enum called OrderStatus with the following states and implement the interface IStatusCode:
```
interface IStatusCode {
 int getCode();
 String getDescription();
}
```

| Status    | Code | Description            |
|-----------|------|------------------------|
| PENDING   | 0    | Order is pending       |
| PAID      | 1    | Payment received       |
| SHIPPED   | 2    | Order has been shipped |
| DELIVERED | 3    | Order delivered        |
| CANCELLED | -1   | Order cancelled        |

Then write a main method that:
1. Iterates through all OrderStatus values
2. Prints each status with its code and description
3. Demonstrates getting a status by name using OrderStatus.valueOf("PAID")

**Answer:** Code in `Coding/hw3_code/Q14_OrderStatus/`
- `IStatusCode.java` – interface with getCode() and getDescription()
- `OrderStatus.java` – enum with PENDING(0), PAID(1), SHIPPED(2), DELIVERED(3), CANCELLED(-1) and descriptions; implements IStatusCode; main does the three steps above
- Run: `javac IStatusCode.java OrderStatus.java && java OrderStatus`



## Q15. Design a Computer class that demonstrates Composition. A computer is composed of:
- CPU (brand, speed in GHz)
- RAM (size in GB)
- HardDrive (size in GB, type: "SSD" or "HDD")

Requirements:
- All component classes should have appropriate constructors and toString() methods
- The Computer class must CREATE all components internally (not receive them via constructor)
- The Computer constructor should accept: cpuBrand, cpuSpeed, ramSize, hdSize, hdType
- The Computer class should have a getSpecs() method that returns all component information

Write a main method to create a computer and display its specs.

**Answer:** Code in `Coding/hw3_code/Q15_Computer/`
- `CPU.java`, `RAM.java`, `HardDrive.java` – component classes with constructors and toString()
- `Computer.java` – creates CPU, RAM, HardDrive inside constructor from (cpuBrand, cpuSpeed, ramSize, hdSize, hdType); getSpecs() returns all component info; main creates a computer and prints specs
- Run: `javac CPU.java RAM.java HardDrive.java Computer.java && java Computer`



## Q16. Implement a Thread-Safe Singleton class called DatabaseConnection using the Bill Pugh Singleton pattern (static inner class). The class should have:
- Private constructor that prints "Database connection created"
- A method executeQuery(String sql) that prints "Executing: " + sql
- A static getInstance() method

Write a main method that:
1. Gets the instance twice and verifies they are the same object (using ==)
2. Executes a sample query

**Answer:** Code in `Coding/hw3_code/Q16_DatabaseConnection/`
- `DatabaseConnection.java` – Bill Pugh Singleton (static inner class Holder holding INSTANCE), private constructor printing "Database connection created", executeQuery(String sql), getInstance(); main gets instance twice, checks conn1 == conn2, runs a sample query
- Run: `javac DatabaseConnection.java && java DatabaseConnection`


## Q17. Design an University system that demonstrates both Aggregation and Composition:

### Composition (University owns Departments):
- University creates and owns Department objects
- When University is destroyed, Departments are destroyed

### Aggregation (University has Professors):
- Professor objects exist independently
- University can add/remove professors
- Professors can exist without a University

Requirements:
- Professor class: name, specialization
- Department class: name, building
- University class: name, list of departments, list of professors
- University constructor creates 3 departments internally
- University has addProfessor(Professor p) and listProfessors() methods

Write a main method that:
1. Creates 2 Professor objects
2. Creates a University
3. Adds both professors to the university
4. Demonstrates that professors still exist after setting university to null

**Answer:** Code in `Coding/hw3_code/Q17_University/`
- `Professor.java` – name, specialization; toString()
- `Department.java` – name, building; toString()
- `University.java` – name, list of departments, list of professors; constructor creates 3 departments internally (composition); addProfessor(Professor p), listProfessors(); main creates 2 professors, creates university, adds both, sets university to null, then shows professors still exist (aggregation)
- Run: `javac Professor.java Department.java University.java && java University`
