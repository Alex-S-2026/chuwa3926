import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentAnalyzer {

    public List<String> getTopStudentNames(List<Student> students, int n) {
        if (students == null) {
            return List.of();
        }
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        if (students == null) {
            return Map.of();
        }
        return students.stream()
                .collect(Collectors.groupingBy(Student::getMajor,
                        Collectors.averagingDouble(Student::getAverageScore)));
    }

    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return Optional.empty();
        }
        return students.stream()
                .max(Comparator.comparingDouble(s ->
                        s.getScores() == null || s.getScores().isEmpty() ? 0.0
                                : s.getScores().stream().mapToDouble(Double::doubleValue).max().orElse(0.0)));
    }

    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        if (students == null || major == null) {
            return List.of();
        }
        List<Student> inMajor = students.stream()
                .filter(s -> major.equals(s.getMajor()))
                .collect(Collectors.toList());
        double majorAvg = inMajor.stream()
                .mapToDouble(Student::getAverageScore)
                .average()
                .orElse(0.0);
        return inMajor.stream()
                .filter(s -> s.getAverageScore() > majorAvg)
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        if (students == null) {
            return Map.of(true, List.of(), false, List.of());
        }
        return students.stream()
                .collect(Collectors.partitioningBy(s -> s.getAverageScore() >= passingScore));
    }

    public static void main(String[] args) {
        Student s1 = new Student("1", "Alice", 20, "CS", List.of(85.0, 90.0, 88.0));
        Student s2 = new Student("2", "Bob", 21, "CS", List.of(70.0, 72.0, 75.0));
        Student s3 = new Student("3", "Carol", 19, "Math", List.of(95.0, 92.0, 98.0));
        Student s4 = new Student("4", "Dave", 22, "Math", List.of(80.0, 78.0, 82.0));
        Student s5 = new Student("5", "Eve", 20, "Physics", List.of(88.0, 91.0, 89.0));
        Student s6 = new Student("6", "Frank", 21, "Physics", List.of(65.0, 70.0, 68.0));

        List<Student> students = List.of(s1, s2, s3, s4, s5, s6);
        StudentAnalyzer analyzer = new StudentAnalyzer();

        System.out.println("--- Top 3 student names by average score ---");
        System.out.println(analyzer.getTopStudentNames(students, 3));

        System.out.println("\n--- Average score by major ---");
        analyzer.getAverageScoreByMajor(students).forEach((major, avg) ->
                System.out.println(major + ": " + avg));

        System.out.println("\n--- Student with highest single score ---");
        String topName = analyzer.findStudentWithHighestSingleScore(students)
                .map(Student::getName)
                .orElse("none");
        System.out.println(topName);

        System.out.println("\n--- Students above average in CS ---");
        analyzer.getStudentsAboveAverageInMajor(students, "CS").forEach(s ->
                System.out.println(s.getName() + " avg=" + s.getAverageScore()));

        System.out.println("\n--- Partition by pass/fail (70.0) ---");
        Map<Boolean, List<Student>> passFail = analyzer.partitionByPassFail(students, 70.0);
        System.out.println("Passed: " + passFail.get(true).stream().map(Student::getName).collect(Collectors.joining(", ")));
        System.out.println("Failed: " + passFail.get(false).stream().map(Student::getName).collect(Collectors.joining(", ")));
    }
}
