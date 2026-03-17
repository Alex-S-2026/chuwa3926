/**
 * OrderStatus enum implementing IStatusCode.
 */
public enum OrderStatus implements IStatusCode {
    PENDING(0, "Order is pending"),
    PAID(1, "Payment received"),
    SHIPPED(2, "Order has been shipped"),
    DELIVERED(3, "Order delivered"),
    CANCELLED(-1, "Order cancelled");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static void main(String[] args) {
        System.out.println("All OrderStatus values:");
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println(status.name() + " -> Code: " + status.getCode()
                + ", Description: " + status.getDescription());
        }

        System.out.println("\nGet status by name OrderStatus.valueOf(\"PAID\"): ");
        OrderStatus paid = OrderStatus.valueOf("PAID");
        System.out.println(paid.name() + " -> " + paid.getCode() + " - " + paid.getDescription());
    }
}
