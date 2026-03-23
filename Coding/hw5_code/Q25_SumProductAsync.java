import java.util.concurrent.CompletableFuture;

/** Q25 Homework 1: async sum and product of two integers. */
public class Q25_SumProductAsync {

    public static void main(String[] args) {
        int a = 6;
        int b = 7;
        CompletableFuture<Integer> sum = CompletableFuture.supplyAsync(() -> a + b);
        CompletableFuture<Integer> product = CompletableFuture.supplyAsync(() -> a * b);
        CompletableFuture.allOf(sum, product).join();
        System.out.println("sum = " + sum.join() + ", product = " + product.join());
    }
}
