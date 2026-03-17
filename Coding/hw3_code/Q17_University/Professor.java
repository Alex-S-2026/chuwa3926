/**
 * Professor - exists independently (aggregation with University).
 */
public class Professor {
    private String name;
    private String specialization;

    public Professor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}
