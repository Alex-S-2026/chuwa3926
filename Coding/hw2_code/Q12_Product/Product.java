import java.util.Objects;

public class Product {
    private String id;
    private String name;
    private double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(this.id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public static void main(String[] args) {
        Product p1 = new Product("P001", "Laptop", 999.99d);
        Product p2 = new Product("P001", "Different Name", 1.0d);

        // Print both products using toString()
        System.out.println(p1.toString());
        System.out.println(p2.toString());

        System.out.println("equals: " + p1.equals(p2));
        System.out.println("hashCode same: " + (p1.hashCode() == p2.hashCode()));
        System.out.println("p1.hashCode=" + p1.hashCode() + ", p2.hashCode=" + p2.hashCode());
    }
}