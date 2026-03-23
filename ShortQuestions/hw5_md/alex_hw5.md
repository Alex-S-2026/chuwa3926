# HW5

- alex_shen
- 2026-3-23
- hw-multiThreading
- Topics Covered:
    - Multithreading
- Submission: Submit your answers as a single document or code files

# Conceptual Questions

## Q1. Read: https://www.interviewbit.com/multithreading-interview-questions/#class-level-lock-vs-object-level-lock

**Object (instance) lock** is the monitor on a specific instance. `synchronized` on an instance method or `synchronized (this)` uses that object’s lock. Two threads can still run the same synchronized instance method at the same time if they use **different** instances.

**Class lock** is the monitor on the `Class` object (e.g. `MyClass.class`). `static synchronized` methods and `synchronized (MyClass.class)` use it. That lock is shared across all instances of the class.

So: instance lock serializes access per object; class lock serializes static-critical sections for the whole type.

## Q2. Write a thread-safe singleton class

**Enum (recommended in Effective Java):** the JVM guarantees one instance and thread-safe initialization.

```java
public enum Singleton {
    INSTANCE;
    public void doSomething() { }
}
```

**Lazy double-checked locking** (if you need a non-enum singleton):

```java
public class SingletonLazy {
    private static volatile SingletonLazy instance;

    private SingletonLazy() {}

    public static SingletonLazy getInstance() {
        if (instance == null) {
            synchronized (SingletonLazy.class) {
                if (instance == null) {
                    instance = new SingletonLazy();
                }
            }
        }
        return instance;
    }
}
```

`volatile` is important so other threads see a fully constructed instance after the second check.

## Q3. How to create a new thread (Please also consider Thread Pool approach)?

1. **Subclass `Thread`** and override `run()`, then `new MyThread().start()`.
2. **Implement `Runnable`** (or `Callable`) and pass it to `new Thread(runnable).start()`.
3. **Thread pool:** use `Executors` or `ThreadPoolExecutor`. Example: `ExecutorService pool = Executors.newFixedThreadPool(4); pool.submit(task);` — the pool manages worker threads and a queue; you submit work instead of creating a raw `Thread` each time.

## Q4. Difference between Runnable and Callable?

Both describe work for another thread. **`Runnable.run()`** returns `void` and does not declare checked exceptions. **`Callable.call()`** returns a value (`V`) and can throw `Exception`, so it fits `ExecutorService.submit()` which gives you a `Future<V>`.

## Q5. What is the difference between t.start() and t.run()?

**`start()`** asks the JVM to create a new thread of execution and eventually call `run()` on that new thread. **`run()`** just executes the method body on the **current** thread (like a normal method call), so you do not get a new thread.

## Q6. Which way of creating threads is better: Thread class or Runnable interface?

Usually **implementing `Runnable` (or `Callable`)** is better: your class can extend another class if needed, logic is separated from `Thread`, and the same task can be handed to executors or multiple threads. Subclassing `Thread` is only needed if you are customizing thread behavior itself.

## Q7. What are the thread statuses?

In Java, `Thread.State` includes: **NEW** (created, not started), **RUNNABLE** (running or ready to run), **BLOCKED** (waiting for a monitor lock), **WAITING** (e.g. `wait()` without timeout), **TIMED_WAITING** (sleep, timed wait), **TERMINATED** (finished).

## Q8. Demonstrate deadlock and how to resolve it in Java code.

Deadlock: two threads each hold one lock and wait for the other’s lock.

```java
Object lockA = new Object();
Object lockB = new Object();

Thread t1 = new Thread(() -> {
    synchronized (lockA) {
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        synchronized (lockB) { System.out.println("t1"); }
    }
});
Thread t2 = new Thread(() -> {
    synchronized (lockB) {
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        synchronized (lockA) { System.out.println("t2"); }
    }
});
t1.start(); t2.start();
```

**Ways to fix:** always acquire locks in a **fixed global order** (e.g. always A then B); use **timeouts** (`tryLock`); reduce nested locking; or use **one** coarser lock if acceptable.

## Q9. How do threads communicate each other?

Shared memory with **synchronization** (`synchronized`, locks): threads read/write guarded data. **`wait()` / `notify()` / `notifyAll()`** coordinate on a monitor. **`BlockingQueue`** passes work between threads. **Pipes** (`PipedInputStream` / `PipedOutputStream`). Higher level: **CompletableFuture**, **CountDownLatch**, **CyclicBarrier**, etc.

## Q10. What’s the difference between class lock and object lock?

**Object lock** is per-instance monitor for instance methods / `synchronized(this)`. **Class lock** is the monitor on the `Class` object for `static` synchronized methods and `synchronized(MyClass.class)`. They are independent: one thread can hold an instance lock while another holds the class lock.

## Q11. What is join() method?

**`thread.join()`** makes the **calling** thread wait until **that** thread finishes (or until the optional timeout). Used to sequence work after another thread completes.

## Q12. what is yield() method

**`Thread.yield()`** is a hint to the scheduler that the current thread is willing to pause so other threads of the same priority may run. It is **not** a binding guarantee and behavior is platform-dependent.

## Q13. What is ThreadPool? How many types of ThreadPool? What is the TaskQueue in ThreadPool?

A **thread pool** reuses a fixed (or bounded) set of worker threads instead of creating a new thread per task. Common factories from `Executors`: **fixed** pool, **cached** (unbounded threads with reuse), **single** thread, **scheduled** (delayed/periodic tasks), and **`ForkJoinPool`** (work-stealing) for divide-and-conquer.

The **task queue** (often a `BlockingQueue`) holds **runnable tasks** waiting for an idle worker thread.

## Q14. Which Library is used to create ThreadPool? Which Interface provide main functions of thread-pool?

**`java.util.concurrent`** (e.g. `Executors`, `ThreadPoolExecutor`). The main abstraction for submitting tasks is **`Executor`**; the richer **`ExecutorService`** extends it with `submit`, `shutdown`, `invokeAll`, etc.

## Q15. How to submit a task to ThreadPool?

Use **`executor.execute(runnable)`** (fire-and-forget) or **`executor.submit(callableOrRunnable)`** (returns `Future`). With `ScheduledExecutorService`, use `schedule` / `scheduleAtFixedRate`, etc.

## Q16. What is the advantage of ThreadPool?

Reuses threads (lower overhead than creating many threads), caps concurrency, centralizes queueing and rejection policies, and simplifies shutdown.

## Q17. Difference between shutdown() and shutdownNow() methods of executor

**`shutdown()`** stops accepting new tasks; already-submitted tasks still run. **`shutdownNow()`** tries to interrupt workers and returns a list of pending tasks from the queue; it does not guarantee instant stop.

## Q18. What is Atomic classes? How many types of Atomic classes? Give me some code example of Atomic classes and its main methods. when to use it?

**Atomic classes** (`java.util.concurrent.atomic`) provide lock-free, thread-safe updates on single variables using CPU compare-and-swap.

Examples: **`AtomicInteger`**, **`AtomicLong`**, **`AtomicBoolean`**, **`AtomicReference`**, **`AtomicIntegerArray`**, field updaters, **`LongAdder`** / **`LongAccumulator`** (often better for high-contention counters).

```java
AtomicInteger i = new AtomicInteger(0);
i.incrementAndGet();   // ++
i.compareAndSet(0, 1); // if current == 0, set to 1
```

Use when you need a **simple counter or flag** updated by many threads without holding a full `synchronized` block around a single primitive or reference.

## Q19. What is the concurrent collections? Can you list some concurrent data structure (Thread-safe)

**Concurrent collections** are designed for multi-threaded access without external locking on every operation (often finer internal locking or lock-free structures).

Examples: **`ConcurrentHashMap`**, **`ConcurrentSkipListMap` / `ConcurrentSkipListSet`**, **`CopyOnWriteArrayList`**, **`CopyOnWriteArraySet`**, **`BlockingQueue` implementations** (`LinkedBlockingQueue`, `ArrayBlockingQueue`, `PriorityBlockingQueue`, `SynchronousQueue`), **`ConcurrentLinkedQueue`**.

## Q20. What kind of locks do you know? What is the advantage of each lock?

- **`synchronized`**: built-in, JVM-managed; simple, no unlock forget.
- **`ReentrantLock`**: try-lock, timed lock, fair mode, multiple conditions.
- **`ReadWriteLock`**: many readers or one writer; good when reads dominate.
- **`StampedLock`**: optimistic reads, can reduce contention vs `ReadWriteLock` in some patterns.
- **Spin locks / internal locks** in JVM and atomic classes for low-level contention.

## Q21. What is future and completableFuture? List some main methods of ComplertableFuture.

**`Future`** represents an async result: you get it from `ExecutorService.submit()` and use **`get()`** (blocking) or **`cancel()`**.

**`CompletableFuture`** is a `Future` you can complete manually and **compose** with callbacks: `thenApply`, `thenAccept`, `thenRun`, `thenCombine`, `allOf`, `anyOf`, `exceptionally`, `handle`, `supplyAsync`, `runAsync`, etc.

## Q22. Type the code by your self and try to understand it. (package com.chuwa.tutorial.t08_multithreading)



## Q23. Write a code to create 2 threads, one thread print 1,3,5,7,9, another thread print 2,4,6,8,10. (solution is in com.chuwa.tutorial.t08_multithreading.c05_waitNotify.OddEventPrinter)
1. One solution use synchronized and wait notify
2. One solution use ReentrantLock and await, signal

**1) `synchronized` + `wait` / `notify`**

Each thread waits until `n` is on “its” parity, prints once, bumps `n`, wakes the other.

```java
public class OddEvenWaitNotify {
    private static final Object lock = new Object();
    private static int n = 1;

    public static void main(String[] args) {
        Runnable odd = () -> {
            synchronized (lock) {
                while (n <= 10) {
                    while (n % 2 == 0) {
                        try { lock.wait(); }
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                    }
                    if (n > 10) break;
                    System.out.println(Thread.currentThread().getName() + ": " + n++);
                    lock.notifyAll();
                }
            }
        };
        Runnable even = () -> {
            synchronized (lock) {
                while (n <= 10) {
                    while (n % 2 == 1) {
                        try { lock.wait(); }
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                    }
                    if (n > 10) break;
                    System.out.println(Thread.currentThread().getName() + ": " + n++);
                    lock.notifyAll();
                }
            }
        };
        new Thread(odd).start();
        new Thread(even).start();
    }
}
```

**2) `ReentrantLock` + `await` / `signal`**

Same idea: one `Condition`, `signalAll()` after each print.

```java
import java.util.concurrent.locks.*;

public class OddEvenLock {
    private static final Lock lock = new ReentrantLock();
    private static final Condition cond = lock.newCondition();
    private static int n = 1;

    public static void main(String[] args) {
        Runnable odd = () -> {
            lock.lock();
            try {
                while (n <= 10) {
                    while (n % 2 == 0) cond.await();
                    if (n > 10) break;
                    System.out.println(Thread.currentThread().getName() + ": " + n++);
                    cond.signalAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        };
        Runnable even = () -> {
            lock.lock();
            try {
                while (n <= 10) {
                    while (n % 2 == 1) cond.await();
                    if (n > 10) break;
                    System.out.println(Thread.currentThread().getName() + ": " + n++);
                    cond.signalAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        };
        new Thread(odd).start();
        new Thread(even).start();
    }
}
```

Sample output pattern:

```
Thread-0: 1
Thread-1: 2
Thread-0: 3
...
```

## Q24. create 3 threads, one thread ouput 1-10, one thread output 11-20, one thread output 21-22. threads run sequence is random. (solution is in com.chuwa.exercise.t08_multithreading.PrintNumber1)

```java
public class ThreeThreadsRandomOrder {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> printRange(1, 10));
        Thread t2 = new Thread(() -> printRange(11, 20));
        Thread t3 = new Thread(() -> printRange(21, 22));
        t1.start();
        t2.start();
        t3.start();
    }

    private static void printRange(int from, int to) {
        for (int i = from; i <= to; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }
    }
}
```

Order of threads is **non-deterministic**; each thread still prints its own range in order.

## Q25. completable future:

### 1. Homework 1: sum and product of two integers asynchronously

```java
import java.util.concurrent.CompletableFuture;

public class SumProduct {
    public static void main(String[] args) {
        int a = 6, b = 7;
        CompletableFuture<Integer> sum = CompletableFuture.supplyAsync(() -> a + b);
        CompletableFuture<Integer> product = CompletableFuture.supplyAsync(() -> a * b);
        CompletableFuture.allOf(sum, product).join();
        System.out.println("sum = " + sum.join() + ", product = " + product.join());
    }
}
```

### 2. Homework 2: three APIs in parallel (using public JSON placeholders)

No API keys needed: three different `jsonplaceholder.typicode.com` endpoints stand in for products, reviews, and inventory. Merge with `thenCombine` / `allOf`.

```java
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class StoreApiMerge {
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static CompletableFuture<String> get(String uri) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                        .GET()
                        .timeout(Duration.ofSeconds(10))
                        .build();
                return HTTP.send(req, HttpResponse.BodyHandlers.ofString()).body();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        CompletableFuture<String> products = get("https://jsonplaceholder.typicode.com/posts/1");
        CompletableFuture<String> reviews = get("https://jsonplaceholder.typicode.com/comments/1");
        CompletableFuture<String> inventory = get("https://jsonplaceholder.typicode.com/albums/1");

        CompletableFuture.allOf(products, reviews, inventory).thenRun(() -> {
            String merged = String.join("\n---\n",
                    products.join(), reviews.join(), inventory.join());
            System.out.println(merged);
        }).join();
    }
}
```

(Requires **Java 11+** for `HttpClient`.)

### 3. Homework 3: exception handling with defaults

```java
import java.util.concurrent.CompletableFuture;

public class StoreApiMergeSafe {
    public static void main(String[] args) {
        CompletableFuture<String> products = fetchOrDefault(
                "https://jsonplaceholder.typicode.com/posts/1", "{}");
        CompletableFuture<String> reviews = fetchOrDefault(
                "https://jsonplaceholder.typicode.com/comments/1", "{}");
        CompletableFuture<String> inventory = fetchOrDefault(
                "https://jsonplaceholder.typicode.com/albums/1", "{}");

        CompletableFuture.allOf(products, reviews, inventory).join();
        System.out.println(products.join());
        System.out.println(reviews.join());
        System.out.println(inventory.join());
    }

    private static CompletableFuture<String> fetchOrDefault(String url, String def) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var client = java.net.http.HttpClient.newHttpClient();
                var req = java.net.http.HttpRequest.newBuilder(java.net.URI.create(url)).GET().build();
                return client.send(req, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
            } catch (Exception e) {
                System.err.println("API failed: " + url + " -> " + e.getMessage());
                return def;
            }
        });
    }
}
```

If any call throws, we log and return **`{}`** (or any default you choose).
