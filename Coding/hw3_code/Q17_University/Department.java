/**
 * Department - created and owned by University (composition).
 */
public class Department {
    private String name;
    private String building;

    public Department(String name, String building) {
        this.name = name;
        this.building = building;
    }

    public String getName() {
        return name;
    }

    public String getBuilding() {
        return building;
    }

    @Override
    public String toString() {
        return name + " in " + building;
    }
}
