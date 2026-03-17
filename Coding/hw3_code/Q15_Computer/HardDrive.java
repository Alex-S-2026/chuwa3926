/**
 * HardDrive component (part of Computer - composition).
 */
public class HardDrive {
    private int sizeGB;
    private String type; // "SSD" or "HDD"

    public HardDrive(int sizeGB, String type) {
        this.sizeGB = sizeGB;
        this.type = type;
    }

    @Override
    public String toString() {
        return "HardDrive: " + sizeGB + " GB " + type;
    }
}
