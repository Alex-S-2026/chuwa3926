/**
 * Computer class demonstrating Composition - creates all components internally.
 */
public class Computer {
    private CPU cpu;
    private RAM ram;
    private HardDrive hardDrive;

    public Computer(String cpuBrand, double cpuSpeed, int ramSize, int hdSize, String hdType) {
        this.cpu = new CPU(cpuBrand, cpuSpeed);
        this.ram = new RAM(ramSize);
        this.hardDrive = new HardDrive(hdSize, hdType);
    }

    public String getSpecs() {
        return cpu.toString() + "\n" + ram.toString() + "\n" + hardDrive.toString();
    }

    public static void main(String[] args) {
        Computer computer = new Computer("Intel", 3.2, 16, 512, "SSD");
        System.out.println("Computer specs:\n" + computer.getSpecs());
    }
}
