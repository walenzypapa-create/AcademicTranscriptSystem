
package models;

import java.io.Serializable;

public enum Grade implements Serializable {
    A("A", 4.0, "Excellent"),
    A_MINUS("A-", 3.7, "Very Good"),
    B_PLUS("B+", 3.3, "Good"),
    B("B", 3.0, "Satisfactory"),
    B_MINUS("B-", 2.7, "Below Average"),
    C_PLUS("C+", 2.3, "Average"),
    C("C", 2.0, "Passing"),
    C_MINUS("C-", 1.7, "Low Pass"),
    D("D", 1.0, "Poor"),
    F("F", 0.0, "Fail"),
    NOT_GRADED("N/A", 0.0, "Not Graded"),
    W("W", 0.0, "Withdrawn"),
    I("I", 0.0, "Incomplete");

    private final String display;
    private final double gradePoints;
    private final String description;

    Grade(String display, double gradePoints, String description) {
        this.display = display;
        this.gradePoints = gradePoints;
        this.description = description;
    }

    public String getDisplay() { return display; }
    public double getGradePoints() { return gradePoints; }
    public String getDescription() { return description; }

    public boolean isPassing() {
        return this != F && this != NOT_GRADED && this != W && this != I;
    }

    @Override
    public String toString() {
        return display + " - " + description;
    }
}