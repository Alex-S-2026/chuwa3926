/**
 * CPU component (part of Computer - composition).
 */
public class CPU {
    private String brand;
    private double speedGHz;

    public CPU(String brand, double speedGHz) {
        this.brand = brand;
        this.speedGHz = speedGHz;
    }

    @Override
    public String toString() {
        return "CPU: " + brand + ", " + speedGHz + " GHz";
    }
}
