# HW2

- alex_shen
- 2026-3-13
- Java OOP Homework 2
- Topics Covered:
    - Inheritance
    - Polymorphism
    - Abstract Classes
    - Interfaces
    - Abstract Class vs Interface
    - The Object Class
- Submission: Submit your answers as a single document or code files

# Conceptual Questions

## Q1. What is the difference between method overloading and method overriding? In which type of polymorphism does each belong?

**Method overloading**
- Same method name, **different parameter list** (different number/types/order of parameters) in the **same class** (or inherited and re-declared with a different signature).
- Resolved at **compile time** (the compiler chooses the method based on the reference type and argument types).
- Belongs to **compile-time (static) polymorphism**.

**Method overriding**
- Subclass provides a new implementation with the **same method signature** as a method in the superclass (and a compatible/covariant return type).
- Resolved at **runtime** via dynamic dispatch (actual object type determines which implementation runs).
- Belongs to **runtime (dynamic) polymorphism**.



## Q2. What will be the output of the following code? Explain why.
```
class Animal {
    public void makeSound() {
        System.out.println("Some sound");
    }
}

class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Bark!");
    }
}

public class Test {
    public static void main(String[] args) {
        Animal a = new Dog();
        a.makeSound();
    }
}
```

Bark!

`a` is an `Animal` reference but points to a `Dog` object. Because `makeSound()` is overridden, Java uses **runtime polymorphism** and calls `Dog.makeSound()`.



## Q3. Why does Java NOT support multiple inheritance with classes? What is the "Diamond Problem"? How does Java solve this issue?

Java does not support multiple inheritance of **classes** mainly to avoid **ambiguity and complexity** (especially around inherited state and method resolution).

Diamond Problem:
A class inherits from two classes that both inherit from a common base. If both parents define/override the same method, the child has ambiguity about **which implementation** to use (and potentially conflicting state).

Java's solution:
- Java allows multiple inheritance of **type** via **interfaces**.
- If interfaces provide conflicting `default` methods, the implementing class must **explicitly override** and choose one (or provide its own implementation), removing ambiguity.



## Q4. What will happen when you try to compile and run the following code? Explain your answer.

```
abstract class Shape {
    public abstract double getArea();
}

public class Test {
    public static void main(String[] args) {
        Shape s = new Shape();
        System.out.println(s.getArea());
    }
}
```

It will not compile.

`Shape` is `abstract`, so it **cannot be instantiated** with `new Shape()`. We must instantiate a concrete subclass that implements `getArea()` (or use an anonymous class).



## Q5. What is the difference between an abstract class and an interface? Give one scenario where you would prefer using an interface over an abstract class.

**Abstract class**
- Can have **instance state** (fields), constructors, and both abstract + concrete methods.
- A class can extend **only one** class.
- Use when you want a shared base with common code and/or shared fields.

**Interface**
- Defines a contract for a capability; can have `default`/`static` methods, but **no instance state** (only constants).
- A class can implement **multiple** interfaces.
- Use when different class hierarchies should share a capability without sharing a base class.

**Prefer interface example**
- A capability like `Drawable`/`Runnable`/`Comparable` that can apply to many unrelated classes.



## Q6. What will be the output of the following code? Explain the concept of upcasting and downcasting.
```
class Animal {
    public void eat() {
        System.out.println("Animal eating");
    }
}

class Cat extends Animal {
    public void meow() {
        System.out.println("Meow!");
    }
}

public class Test {
    public static void main(String[] args) {
        Animal a = new Cat(); // Line 1
        a.eat(); // Line 2
        // a.meow(); // Line 3 (commented out)
        
        if (a instanceof Cat) {
            Cat c = (Cat) a; // Line 4
            c.meow(); // Line 5
        }
    }
}

```

Output:

Animal eating

Meow!

**Upcasting (Line 1)**
- Treating a subclass object (Cat) as a superclass reference (Animal): `Animal a = new Cat();`
- Implicit (no cast needed). You can only call methods declared in the reference type (`Animal`) unless they are overridden.

**Downcasting (Line 4)**
- Casting a superclass reference back to a subclass reference: `Cat c = (Cat) a;`
- Requires an explicit cast and is only safe if the object is actually a `Cat`; otherwise `ClassCastException`. `instanceof` is used to guard the cast.



## Q7. What are the rules for overriding the equals() method? Why must we also override hashCode() when we override equals()?

**Rules / contract for `equals(Object)`**
- **Reflexive**: `x.equals(x)` is `true`.
- **Symmetric**: `x.equals(y)` iff `y.equals(x)`.
- **Transitive**: if `x.equals(y)` and `y.equals(z)` then `x.equals(z)`.
- **Consistent**: repeated calls return the same result as long as compared fields don’t change.
- **Non-null**: `x.equals(null)` is `false`.
- Proper signature: must be `public boolean equals(Object o)` (don't overload with `equals(MyType)`).

**Why override `hashCode()` too**
- Java requires: if `a.equals(b)` is `true`, then `a.hashCode() == b.hashCode()`.
- Hash-based collections (`HashMap`, `HashSet`) use `hashCode()` to place/find objects; violating the rule breaks lookups and can allow duplicates.



## Q8. What is the difference between shallow copy and deep copy? Given an object `Person` with a field `Address address`, explain what happens to the `address` field in each type of copy.

**Shallow copy**
- Copies the top-level `Person`, but `address` references the **same `Address` object** as the original.
- Mutating the `Address` through either `Person` is visible to the other.

**Deep copy**
- Copies `Person` and also creates a **new `Address` object** (with copied values).
- Mutating one `Address` does not affect the other.



## Q9. What will be the output of the following code? Explain why.
```
interface Flyable {
    default void takeOff() {
        System.out.println("Taking off from Flyable");
    }
}

interface Swimmable {
    default void takeOff() {
        System.out.println("Diving in from Swimmable");
    }
}

class Duck implements Flyable, Swimmable {
    @Override
    public void takeOff() {
        Flyable.super.takeOff();
    }
}
public class Test {
    public static void main(String[] args) {
        Duck d = new Duck();
        d.takeOff();
    }
}

```

Output:

Taking off from Flyable

Both interfaces define a conflicting `default takeOff()`. `Duck` must override it, and it explicitly chooses `Flyable`’s default via `Flyable.super.takeOff()`.



## Q10. Consider the following code. Which methods are valid overloads of `calculate(int a, int b)`? Which are NOT valid and why?
```
public class Calculator {
    public int calculate(int a, int b) {
        return a + b;
    }
    
    // Method A
    public int calculate(int a, int b, int c) {
        return a + b + c;
    }
    
    // Method B
    public double calculate(double a, double b) {
        return a + b;
    }
    
    // Method C
    public double calculate(int a, int b) {
        return (double)(a + b);
    }

    // Method D
    private int calculate(int x, int y) {
        return x * y;
    }
}

```

Valid overloads:
- **Method A**: different parameter list (`int, int, int`)
- **Method B**: different parameter types (`double, double`)

**NOT valid (won’t compile)**
- **Method C**: same parameter list as original (`int, int`); changing only return type is not overloading.
- **Method D**: same parameter list as original (`int, int`); parameter names and access modifiers don’t change the signature, so it duplicates the method.



# Programming Questions

## Q11. Create a simple shape hierarchy using abstract classes and interfaces:
1. Create an interface Drawable with a method void draw().
2. Create an abstract class Shape with:
    - A protected field String color
    - A constructor that takes the color
    - An abstract method double getArea()
    - An abstract method double getPerimeter()
    - A concrete method String getColor() that returns the color
3. Create a Rectangle class that extends Shape and implements Drawable:
    - Private fields: double width, double height
    - Constructor taking color, width, and height
    - Implement all required methods
4. Create a Circle class that extends Shape and implements Drawable:
    - Private field: double radius
    - Constructor taking color and radius
    - Implement all required methods

Write a main method that:
    - Creates an array of Shape objects containing at least one Rectangle and one Circle
    - Uses a loop to print the area and perimeter of each shape
    - Checks if each shape is Drawable and calls draw() if it is



## Q12. Create a Product class with proper equals(), hashCode(), and toString() implementations:
    - Private fields: String id, String name, double price
    - A constructor that takes all three parameters
    - Override toString() to return a string in the format: Product{id='P001', name='Laptop', price=999.99}
    - Override equals() to compare products by id only (two products are equal if they have the same id)
    - Override hashCode() consistent with equals()

Write a main method to test:
    - Create two Product objects with the same id but different name and price
    - Verify that equals() returns true for these two products
    - Verify that hashCode() returns the same value for both
    - Print both products using toString()