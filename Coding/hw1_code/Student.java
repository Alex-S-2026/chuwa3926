/**

 * - alex_shen
 * - 2026-3-11
 * - Java OOP Homework 1
 * - Submission: Submit your answers as a single document or code files
 *
 * Q11. Create a Student class with proper encapsulation:
 *  - Private fields: name (String), age (int), grade (double)
 *  - A constructor that takes all three parameters
 *  - Getter methods for all fields
 *  - Setter methods for all fields, with the following validation:
 *      - age must be between 1 and 150
 *      - grade must be between 0.0 and 100.0
 *      - If invalid value is passed, do not change the field
 * 
 *  Write a main method to test your class.
 */

public class Student {
    private String name;
    private int age;
    private double grade;

    public Student(String name, int age, double grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public double getGrade() {
        return this.grade;
    }

    public void setName(String nameInput) {
        if (nameInput != null && !nameInput.isEmpty()) {
            this.name = nameInput;
        }
    }

    public void setAge(int ageInput) {
        if (ageInput > 0 && ageInput < 151) {
            this.age = ageInput;
        }
    }

    public void setGrade(double gradeInput) {
        if (gradeInput >= 0.0d && gradeInput <= 100.0d) {
            this.grade = gradeInput;
        }
    }

    public static void main(String[] args) {
        Student s = new Student("Test Student 1", 1, 0.0d);
        System.out.println("Student Name: " + s.getName() + ", Age: " + s.getAge() + ", Grade: " + s.getGrade());

        s.setAge(150);
        s.setGrade(100.0d);
        System.out.println("After valid updates - Name: " + s.getName() + ", Age: " + s.getAge() + ", Grade: " + s.getGrade());

        s.setName(null);
        s.setName("");
        s.setAge(0);
        s.setAge(-1);
        s.setAge(151);
        s.setGrade(-0.000000000000001d);
        s.setGrade(100.000000000000001d);
        System.out.println("After invalid updates (unchanged) - Name: " + s.getName() + ", Age: " + s.getAge() + ", Grade: " + s.getGrade());
    }

}