import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Q22: Stand-in for working through {@code com.chuwa.tutorial.t08_multithreading} —
 * runnable demos of thread creation, executor, synchronization, and atomics.
 */
public class Q22_TutorialConceptsDemo {

    public static void main(String[] args) throws Exception {
        demoRunnableThread();
        demoCallableFuture();
        demoThreadPool();
        demoSynchronizedCounter();
        demoAtomicCounter();
        demoCountDownLatch();
        System.out.println("Q22 demos finished.");
    }

    private static void demoRunnableThread() throws InterruptedException {
        System.out.println("\n--- Runnable + Thread ---");
        Thread t = new Thread(() -> System.out.println("Hello from " + Thread.currentThread().getName()));
        t.start();
        t.join();
    }

    private static void demoCallableFuture() throws ExecutionException, InterruptedException {
        System.out.println("\n--- Callable + Future ---");
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Callable<Integer> task = () -> 21 * 2;
            Future<Integer> f = pool.submit(task);
            System.out.println("callable result: " + f.get());
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static void demoThreadPool() throws InterruptedException {
        System.out.println("\n--- ExecutorService (fixed pool) ---");
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            for (int i = 0; i < 3; i++) {
                final int n = i;
                pool.execute(() -> System.out.println("task " + n + " on " + Thread.currentThread().getName()));
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static int shared;

    private static synchronized void incShared() {
        shared++;
    }

    private static void demoSynchronizedCounter() throws InterruptedException {
        System.out.println("\n--- synchronized counter ---");
        shared = 0;
        Thread a = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                incShared();
            }
        });
        Thread b = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                incShared();
            }
        });
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("shared (expect 2000): " + shared);
    }

    private static void demoAtomicCounter() throws InterruptedException {
        System.out.println("\n--- AtomicInteger ---");
        AtomicInteger ai = new AtomicInteger();
        Thread a = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                ai.incrementAndGet();
            }
        });
        Thread b = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                ai.incrementAndGet();
            }
        });
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("atomic (expect 2000): " + ai.get());
    }

    private static void demoCountDownLatch() throws InterruptedException {
        System.out.println("\n--- CountDownLatch ---");
        int n = 3;
        CountDownLatch done = new CountDownLatch(n);
        ExecutorService pool = Executors.newFixedThreadPool(n);
        try {
            for (int i = 0; i < n; i++) {
                final int id = i;
                pool.execute(() -> {
                    System.out.println("worker " + id);
                    done.countDown();
                });
            }
            done.await(5, TimeUnit.SECONDS);
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
