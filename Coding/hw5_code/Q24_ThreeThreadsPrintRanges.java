/**
 * Q24: Three threads print 1–10, 11–20, and 21–22. Start order is nondeterministic;
 * each thread prints its range in order (matches com.chuwa.exercise.t08_multithreading.PrintNumber1 idea).
 */
public class Q24_ThreeThreadsPrintRanges {

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
