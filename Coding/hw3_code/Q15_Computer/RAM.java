/**
 * RAM component (part of Computer - composition).
 */
public class RAM {
    private int sizeGB;

    public RAM(int sizeGB) {
        this.sizeGB = sizeGB;
    }

    @Override
    public String toString() {
        return "RAM: " + sizeGB + " GB";
    }
}
