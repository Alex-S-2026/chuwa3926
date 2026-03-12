# HW1

- alex_shen
- 2026-3-11
- Java OOP Homework 1
- Topics Covered:
    - JDK/JRE/JVM
    - Data Types
    - Pass by Value
    - String Pool
    - final/static
    - Class Loading
    - Encapsulation
- Submission: Submit your answers as a single document or code files

# Conceptual Questions

## Q1. What is the difference between JDK, JRE, and JVM? Which one do you need to run a compiled Java program?

- **JVM (Java Virtual Machine):**
    - Executes Java bytecode
    - the runtime engine that runs compiled `.class` files
    - platform independence
- **JRE (Java Runtime Environment):**
    - Includes the JVM + core libraries + other files needed to run Java applications
    - not tools to develop/compile Java app's
    - we need the **JRE** (which includes JVM inside) to run a compiled Java program
- **JDK (Java Development Kit):** 
    - Includes the JRE (which includes JVM) + development tools (compiler `javac`, debugger, ......)
    - we need JDK to write and compile Java code
    - but JRE itself is enough to run already-compiled programs



## Q2. Given the following code, what will be printed and why?
```
int a = 10;
int b = a;
b = 20;
System.out.println(a);
```

Prints 10. In Java, primitive types (e.g., `int`) are passed by value. So `b = a` copies the **value 10** into `b`. Changing `b` to 20 does not affect `a`, so `a` remains 10.



## Q3. Given the following code, what will be printed and why?
```
int[] arr1 = {1, 2, 3};
int[] arr2 = arr1;
arr2[0] = 100;
System.out.println(arr1[0]);
```

Prints 100. Arrays are reference types in Java. So `arr2 = arr1` makes `arr2` refer to the same **array object** (whose address is the 0th element) as `arr1`. `arr2[0] = 100` modifies that shared array, so `arr1[0]` is set as 100.



## Q4. What is the output of the following code? Explain your answer.
```
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");
System.out.println(s1 == s2);
System.out.println(s1 == s3);
System.out.println(s1.equals(s3));
```

true

false

true

- Both `s1` and `s2` are string literals, so they refer to the same object in the **String Constant Pool**.
- `s3` is created with `new String("hello")`, so it is a different object in Heap Memory, even though the content is the same. `==` compares **references**, not content.
- `equals` compares the character **content** of the strings, and both are `"hello"`.



## Q5. What is the difference between final variable, final method, and final class?

- **final variable:** The variable cannot be reassigned after it is initialized (for primitives, the value is fixed; for references, the reference cannot point to another object).
- **final method:** The method cannot be overridden by subclasses.
- **final class:** The class cannot be extended (no subclasses can be created), so it prevents inheritance.



## Q6. What is the difference between static variable and instance variable? Give a simple example of when you would use a static variable.

- **Instance variable:** Declared without `static`; each object has its own copy. Exists for the lifetime of the object.
- **Static variable:** Declared with `static`; there is one copy shared by all instances of the class (and it belongs to the class itself). It exists for the lifetime of the program (or until the class is unloaded).

**Example:** Use a static variable for a counter that tracks how many instances of a class have been created (e.g., `static int instanceCount`), or for shared configuration (e.g., `static final double TAX_RATE`).



## Q7. What will happen when you try to compile and run this code?
```
public class Test {
    public static void main(String[] args) {
        final int x = 10;
        x = 20;
        System.out.println(x);
    }
}
```

The code will not compile. The variable `x` is declared as `final int x = 10`, so it cannot be reassigned. The line `x = 20;` causes a compilation error (e.g., "cannot assign a value to final variable x").



## Q8. List the four pillars of Object-Oriented Programming and briefly explain each one in one sentence.

1. **Encapsulation:** Bundling data (fields) and behavior (methods) inside a class and controlling access (e.g., via private fields, public methods, ......) so internal state is protected.
2. **Inheritance:** A subclass can extend a superclass and reuse its fields and methods, allowing code reuse and an "is-a" relationship.
3. **Polymorphism:** Objects of different types can be treated through a common interface or superclass type, with the actual behavior determined at runtime (e.g., method overriding).
4. **Abstraction:** Hiding implementation details and exposing only what is necessary (e.g., through abstract classes or interfaces) so users work with a simplified view.



## Q9. What is encapsulation? Why do we make instance variables private and provide public getter/setter methods?

Encapsulation is the OOP idea of bundling data and the methods that operate on that data inside a class, and restricting direct access to the internal state.

We make instance variables **private** and provide **public getters/setters** so that:
1. external code cannot arbitrarily read or change the data—access goes through the class’s own methods
2. we can add validation in setters (e.g., reject invalid age or grade)
3. we can change how data is stored or computed internally without breaking code that uses the class
4. we control read-only vs. read-write exposure (e.g., only getters and no setter for immutable fields).



## Q10. What is the output of the following code?
```
public class Counter {
    static int count = 0;

    public Counter() {
        count++;
    }

    public static void main(String[] args) {
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = new Counter();
        System.out.println(Counter.count);
    }
}
```

Prints 3. `count` is a static variable, so there is a single shared copy for the class. Each time `new Counter()` is called, the constructor runs and executes `count++`. After creating c1, c2, and c3, the constructor has run three times, so `Counter.count` is 3.



# Programming Questions

## Q11. Create a Student class with proper encapsulation:
- Private fields: name (String), age (int), grade (double)
- A constructor that takes all three parameters
- Getter methods for all fields
- Setter methods for all fields, with the following validation:
    - age must be between 1 and 150
    - grade must be between 0.0 and 100.0
    - If invalid value is passed, do not change the field

Write a main method to test your class.

```
public class Student {
    private String name;
    private int age;
    private double grade;

    public Student(String name, int age, double grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public double getGrade() {
        return this.grade;
    }

    public void setName(String nameInput) {
        if (nameInput != null && !nameInput.isEmpty()) {
            this.name = nameInput;
        }
    }

    public void setAge(int ageInput) {
        if (ageInput > 0 && ageInput < 151) {
            this.age = ageInput;
        }
    }

    public void setGrade(double gradeInput) {
        if (gradeInput >= 0.0d && gradeInput <= 100.0d) {
            this.grade = gradeInput;
        }
    }

    public static void main(String[] args) {
        Student s = new Student("Test Student 1", 1, 0.0d);
        System.out.println("Student Name: " + s.getName() + ", Age: " + s.getAge() + ", Grade: " + s.getGrade());

        s.setAge(150);
        s.setGrade(100.0d);
        System.out.println("After valid updates - Name: " + s.getName() + ", Age: " + s.getAge() + ", Grade: " + s.getGrade());

        s.setName(null);
        s.setName("");
        s.setAge(0);
        s.setAge(-1);
        s.setAge(151);
        s.setGrade(-0.000000000000001d);
        s.setGrade(100.000000000000001d);
        System.out.println("After invalid updates (unchanged) - Name: " + s.getName() + ", Age: " + s.getAge() + ", Grade: " + s.getGrade());
    }

}
```

## Q12. Create a BankAccount class with proper encapsulation:
- Private fields: accountNumber (String), balance (double)
- A constructor that takes accountNumber and sets initial balance to 0
- Getter methods for both fields (no setter for accountNumber - it should not be changed after creation)
- A deposit(double amount) method that adds money to balance (only if amount > 0)
- A withdraw(double amount) method that subtracts money from balance (only if amount > 0 and balance >= amount)
- Both methods should return true if successful, false otherwise

Write a main method to test deposit and withdraw operations.

```
public class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber) {
        if (accountNumber != null && !accountNumber.isEmpty()) {
            this.accountNumber = accountNumber;
        }
        this.balance = 0.0d;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0.0d) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0.0d && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount("Test Account 1");
        System.out.println("Account: " + account.getAccountNumber() + ", Balance: " + account.getBalance());

        System.out.println("Deposit 1: " + account.deposit(1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Deposit -0 (invalid): " + account.deposit(-0));
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Deposit -1 (invalid): " + account.deposit(-1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Withdraw 1: " + account.withdraw(1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Withdraw 0.000000000000001 (exceeds balance): " + account.withdraw(0.000000000000001d));
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Withdraw -1 (invalid): " + account.withdraw(-1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Deposit 0.000000000000001: " + account.deposit(0.000000000000001d));
        System.out.println("Balance: " + account.getBalance());
    }
}
```