import java.util.ArrayList;
import java.util.List;

/**
 * University: Composition (owns Departments), Aggregation (has Professors).
 */
public class University {
    private String name;
    private List<Department> departments;  // composition - created internally
    private List<Professor> professors;    // aggregation - added from outside

    public University(String name) {
        this.name = name;
        this.departments = new ArrayList<>();
        this.professors = new ArrayList<>();
        // Create 3 departments internally (composition)
        departments.add(new Department("Computer Science", "Building A"));
        departments.add(new Department("Mathematics", "Building B"));
        departments.add(new Department("Physics", "Building C"));
    }

    public void addProfessor(Professor p) {
        professors.add(p);
    }

    public void listProfessors() {
        System.out.println("Professors at " + name + ":");
        for (Professor p : professors) {
            System.out.println("  - " + p);
        }
    }

    public static void main(String[] args) {
        Professor p1 = new Professor("Dr. Smith", "Algorithms");
        Professor p2 = new Professor("Dr. Jones", "Machine Learning");

        University university = new University("State University");
        university.addProfessor(p1);
        university.addProfessor(p2);

        university.listProfessors();

        university = null;  // University reference set to null

        System.out.println("\nAfter setting university to null, professors still exist:");
        System.out.println("  " + p1);
        System.out.println("  " + p2);
    }
}
