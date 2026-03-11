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



## Q2. Given the following code, what will be printed and why?
```
int a = 10;
int b = a;
b = 20;
System.out.println(a);
```



## Q3. Given the following code, what will be printed and why?
```
int[] arr1 = {1, 2, 3};
int[] arr2 = arr1;
arr2[0] = 100;
System.out.println(arr1[0]);
```



## Q4. What is the output of the following code? Explain your answer.
```
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");
System.out.println(s1 == s2);
System.out.println(s1 == s3);
System.out.println(s1.equals(s3));
```



## Q5. What is the difference between final variable, final method, and final class?



## Q6. What is the difference between static variable and instance variable? Give a simple example of when you would use a static variable.



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



## Q8. List the four pillars of Object-Oriented Programming and briefly explain each one in one sentence.



## Q9. What is encapsulation? Why do we make instance variables private and provide public getter/setter methods?



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



## Q12. Create a BankAccount class with proper encapsulation:
- Private fields: accountNumber (String), balance (double)
- A constructor that takes accountNumber and sets initial balance to 0
- Getter methods for both fields (no setter for accountNumber - it should not be changed after creation)
- A deposit(double amount) method that adds money to balance (only if amount > 0)
- A withdraw(double amount) method that subtracts money from balance (only if amount > 0 and balance >= amount)
- Both methods should return true if successful, false otherwise

Write a main method to test deposit and withdraw operations.

