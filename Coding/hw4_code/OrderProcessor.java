import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public interface OrderProcessor {

    default BigDecimal calculateTotal(Order order) {
        if (order == null || order.getItems() == null) {
            return BigDecimal.ZERO;
        }
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        if (price == null) {
            return "$0.00";
        }
        return "$" + price.setScale(2, java.math.RoundingMode.HALF_UP).toString();
    }

    void processOrder(Order order);
}
