import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/** Q25 Homework 3: same three calls; on failure log and return default body. */
public class Q25_StoreApiMergeSafe {

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
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
                return client.send(req, HttpResponse.BodyHandlers.ofString()).body();
            } catch (Exception e) {
                System.err.println("API failed: " + url + " -> " + e.getMessage());
                return def;
            }
        });
    }
}
