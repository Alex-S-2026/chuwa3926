import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/** Q25 Homework 2: fetch three public JSON endpoints in parallel and merge (Java 11+ HttpClient). */
public class Q25_StoreApiMerge {

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
            String merged = String.join("\n---\n", products.join(), reviews.join(), inventory.join());
            System.out.println(merged);
        }).join();
    }
}
