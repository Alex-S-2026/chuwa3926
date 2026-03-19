import java.util.List;

public class Student {
    private String id;
    private String name;
    private int age;
    private String major;
    private List<Double> scores;

    public Student(String id, String name, int age, String major, List<Double> scores) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMajor() {
        return major;
    }

    public List<Double> getScores() {
        return scores;
    }

    public double getAverageScore() {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
