
package utils;

import models.Course;
import models.Grade;
import java.util.List;

public class GradeCalculator {

    public static double calculateGPA(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
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

    public static String getGradeDescription(Grade grade) {
        if (grade == null) {
            return "No grade assigned";
        }
        return grade.getDescription();
    }

    public static boolean isPassing(Grade grade) {
        return grade != null && grade.isPassing();
    }

    public static String getGradeDistribution(List<Course> courses) {
        StringBuilder sb = new StringBuilder();
        for (Grade grade : Grade.values()) {
            long count = courses.stream()
                    .filter(c -> c.getGrade() == grade)
                    .count();
            if (count > 0) {
                sb.append(grade.getDisplay()).append(": ").append(count).append(" courses\n");
            }
        }
        return sb.toString();
    }
}