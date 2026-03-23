import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Q23 (2/2): Same alternating output using ReentrantLock + Condition (await / signalAll).
 */
public class Q23_OddEvenReentrantLock {

    private static final Lock lock = new ReentrantLock();
    private static final Condition cond = lock.newCondition();
    private static int n = 1;

    public static void main(String[] args) {
        Runnable odd = () -> {
            lock.lock();
            try {
                while (n <= 10) {
                    while (n % 2 == 0) {
                        cond.await();
                    }
                    if (n > 10) {
                        break;
                    }
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
                    while (n % 2 == 1) {
                        cond.await();
                    }
                    if (n > 10) {
                        break;
                    }
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
