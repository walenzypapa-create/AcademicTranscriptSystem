
package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person implements Serializable {
    private static final long serialVersionUID = 1L;

    // Additional fields specific to Student
    private String studentId;
    private String major;
    private int enrollmentYear;
    private double gpa;
    private List<Course> enrolledCourses;
    private Transcript transcript;

    // Constructor
    public Student(String id, String firstName, String lastName, LocalDate dateOfBirth,
                   String studentId, String major, int enrollmentYear) {
        super(id, firstName, lastName, dateOfBirth);
        this.studentId = studentId;
        this.major = major;
        this.enrollmentYear = enrollmentYear;
        this.enrolledCourses = new ArrayList<>();
        this.transcript = new Transcript(this);
        this.gpa = 0.0;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int enrollmentYear) { this.enrollmentYear = enrollmentYear; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public List<Course> getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(List<Course> enrolledCourses) { this.enrolledCourses = enrolledCourses; }

    public Transcript getTranscript() { return transcript; }
    public void setTranscript(Transcript transcript) { this.transcript = transcript; }

    // Implementation of abstract method
    @Override
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    // Method to add course (Polymorphism - method overloading)
    public void addCourse(Course course) {
        if (course != null && !enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            transcript.addCourse(course);
        }
    }

    public void addCourse(String courseCode, String courseName, int credits) {
        Course course = new Course(courseCode, courseName, credits);
        addCourse(course);
    }

    // Method to remove course
    public boolean removeCourse(Course course) {
        boolean removed = enrolledCourses.remove(course);
        if (removed) {
            transcript.removeCourse(course);
        }
        return removed;
    }

    // Calculate GPA
    public void calculateGPA() {
        gpa = transcript.calculateGPA();
    }

    @Override
    public String toString() {
        return getFullName() + " - " + studentId + " - " + major;
    }
}