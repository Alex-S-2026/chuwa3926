import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderService implements OrderProcessor {

    @Override
    public void processOrder(Order order) {
        if (order != null) {
            System.out.println("Processing order " + order.getOrderId() + " for " + order.getCustomerEmail());
            BigDecimal total = calculateTotal(order);
            System.out.println("Total: " + OrderProcessor.formatPrice(total));
        }
    }

    public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
        if (orders == null) {
            return List.of();
        }
        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        if (orders == null) {
            return Map.of();
        }
        return orders.stream()
                .filter(order -> order.getItems() != null && !order.getItems().isEmpty())
                .collect(Collectors.groupingBy(order -> order.getItems().get(0).getCategory()));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Optional.empty();
        }
        return orders.stream()
                .max((a, b) -> calculateTotal(a).compareTo(calculateTotal(b)));
    }

    public static void main(String[] args) {
        Product p1 = new Product("P1", "Laptop", new BigDecimal("599.99"), "Electronics", true);
        Product p2 = new Product("P2", "Mouse", new BigDecimal("29.99"), "Electronics", true);
        Product p3 = new Product("P3", "Desk", new BigDecimal("199.00"), "Furniture", true);
        Product p4 = new Product("P4", "Chair", new BigDecimal("149.50"), "Furniture", true);
        Product p5 = new Product("P5", "Keyboard", new BigDecimal("79.99"), "Electronics", true);

        Order order1 = new Order("O1", java.time.LocalDateTime.now(), List.of(p1, p2), "alice@example.com");
        Order order2 = new Order("O2", java.time.LocalDateTime.now(), List.of(p3), "bob@example.com");
        Order order3 = new Order("O3", java.time.LocalDateTime.now(), List.of(p4, p5), "carol@example.com");
        Order order4 = new Order("O4", java.time.LocalDateTime.now(), List.of(p2, p5), "dave@example.com");

        List<Order> orders = List.of(order1, order2, order3, order4);
        OrderService service = new OrderService();

        System.out.println("--- Filter orders with total > $100 ---");
        List<Order> over100 = service.filterOrders(orders, order ->
                service.calculateTotal(order).compareTo(new BigDecimal("100")) > 0);
        over100.forEach(order -> System.out.println(order.getOrderId() + ": " +
                OrderProcessor.formatPrice(service.calculateTotal(order))));

        System.out.println("\n--- Group orders by category ---");
        Map<String, List<Order>> byCategory = service.groupOrdersByCategory(orders);
        byCategory.forEach((category, list) ->
                System.out.println(category + ": " + list.stream().map(Order::getOrderId).collect(Collectors.joining(", "))));

        System.out.println("\n--- Most expensive order ---");
        service.findMostExpensiveOrder(orders)
                .ifPresentOrElse(
                        order -> System.out.println("Order " + order.getOrderId() + " with total " +
                                OrderProcessor.formatPrice(service.calculateTotal(order))),
                        () -> System.out.println("No orders"));
    }
}
