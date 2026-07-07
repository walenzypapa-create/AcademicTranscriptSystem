
package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private List<Course> courses;
    private LocalDate generatedDate;
    private double cumulativeGPA;
    private int totalCreditsEarned;
    private int totalCreditsAttempted;

    // Constructor
    public Transcript(Student student) {
        this.student = student;
        this.courses = new ArrayList<>();
        this.generatedDate = LocalDate.now();
        this.cumulativeGPA = 0.0;
        this.totalCreditsEarned = 0;
        this.totalCreditsAttempted = 0;
    }

    // Getters and Setters
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }

    public LocalDate getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDate generatedDate) { this.generatedDate = generatedDate; }

    public double getCumulativeGPA() { return cumulativeGPA; }
    public int getTotalCreditsEarned() { return totalCreditsEarned; }
    public int getTotalCreditsAttempted() { return totalCreditsAttempted; }

    // Method to add course
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            updateStatistics();
        }
    }

    // Method to remove course
    public void removeCourse(Course course) {
        courses.remove(course);
        updateStatistics();
    }

    // Calculate GPA
    public double calculateGPA() {
        if (courses.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Course course : courses) {
            if (course.isGraded()) {
                totalGradePoints += course.getGradePoints() * course.getCredits();
                totalCredits += course.getCredits();
            }
        }

        if (totalCredits == 0) {
            return 0.0;
        }

        return totalGradePoints / totalCredits;
    }

    // Update statistics
    private void updateStatistics() {
        this.cumulativeGPA = calculateGPA();

        this.totalCreditsAttempted = courses.stream()
                .filter(Course::isGraded)
                .mapToInt(Course::getCredits)
                .sum();

        this.totalCreditsEarned = courses.stream()
                .filter(c -> c.isGraded() && c.getGrade().isPassing())
                .mapToInt(Course::getCredits)
                .sum();
    }

    // Get completed courses
    public List<Course> getCompletedCourses() {
        return courses.stream()
                .filter(Course::isGraded)
                .collect(Collectors.toList());
    }

    // Get courses by semester
    public List<Course> getCoursesBySemester(String semester) {
        return courses.stream()
                .filter(c -> semester.equals(c.getSemester()))
                .collect(Collectors.toList());
    }

    // Get grade distribution
    public String getGradeDistribution() {
        StringBuilder sb = new StringBuilder();
        for (Grade grade : Grade.values()) {
            long count = courses.stream()
                    .filter(c -> c.getGrade() == grade)
                    .count();
            if (count > 0) {
                sb.append(grade.getDisplay()).append(": ").append(count).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Transcript for " + student.getFullName() +
                " (GPA: " + String.format("%.2f", cumulativeGPA) + ")";
    }
}