/**
 * Q23 (1/2): Two threads — odd prints 1,3,5,7,9; even prints 2,4,6,8,10.
 * Uses synchronized + wait / notifyAll.
 */
public class Q23_OddEvenWaitNotify {

    private static final Object lock = new Object();
    private static int n = 1;

    public static void main(String[] args) {
        Runnable odd = () -> {
            synchronized (lock) {
                while (n <= 10) {
                    while (n % 2 == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (n > 10) {
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + n++);
                    lock.notifyAll();
                }
            }
        };
        Runnable even = () -> {
            synchronized (lock) {
                while (n <= 10) {
                    while (n % 2 == 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (n > 10) {
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + n++);
                    lock.notifyAll();
                }
            }
        };
        new Thread(odd).start();
        new Thread(even).start();
    }
}
