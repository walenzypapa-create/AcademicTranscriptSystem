
package controllers;

import models.Course;
import models.Student;
import models.Transcript;
import models.Grade;
import utils.GradeCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
        import javafx.scene.layout.*;
        import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;

public class TranscriptController {
    private StudentController studentController;
    private CourseController courseController;
    private ComboBox<Student> studentComboBox;
    private ListView<Course> assignedCoursesListView;
    private ListView<Course> availableCoursesListView;
    private TextArea transcriptTextArea;
    private Button generateButton, addCourseButton, removeCourseButton, printButton;
    private Label gpaLabel, creditsLabel;

    public TranscriptController() {
        studentController = new StudentController();
        courseController = new CourseController();
    }

    public VBox getView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("transcript-view");

        Label headerLabel = new Label("Transcript Generation");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Selection Panel
        HBox selectionPanel = createSelectionPanel();

        // Course Assignment Panel
        HBox assignmentPanel = createAssignmentPanel();

        // Statistics Panel
        HBox statsPanel = createStatisticsPanel();

        // Transcript Display
        VBox transcriptPanel = createTranscriptPanel();

        // Button Panel
        HBox buttonPanel = createButtonPanel();

        view.getChildren().addAll(headerLabel, selectionPanel, assignmentPanel,
                statsPanel, transcriptPanel, buttonPanel);
        return view;
    }

    private HBox createSelectionPanel() {
        HBox panel = new HBox(15);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("selection-panel");

        Label studentLabel = new Label("Select Student:");
        studentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        studentComboBox = new ComboBox<>();
        studentComboBox.setPromptText("Choose a student");
        studentComboBox.setPrefWidth(300);
        studentComboBox.setItems(studentController.getStudents());

        // Listener to load courses when student is selected
        studentComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadStudentCourses(newVal);
                updateStatistics(newVal);
            }
        });

        generateButton = new Button("Generate Transcript");
        generateButton.getStyleClass().add("button-primary");
        generateButton.setOnAction(e -> generateTranscript());

        panel.getChildren().addAll(studentLabel, studentComboBox, generateButton);
        return panel;
    }

    private HBox createAssignmentPanel() {
        HBox panel = new HBox(15);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("assignment-panel");

        VBox availableBox = new VBox(5);
        Label availableLabel = new Label("Available Courses:");
        availableLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        availableCoursesListView = new ListView<>();
        availableCoursesListView.setPrefHeight(200);
        availableCoursesListView.setPrefWidth(300);
        availableCoursesListView.setItems(courseController.getCourses());
        availableBox.getChildren().addAll(availableLabel, availableCoursesListView);

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        addCourseButton = new Button("→ Add →");
        addCourseButton.getStyleClass().add("button-secondary");
        addCourseButton.setOnAction(e -> addCourseToStudent());
        removeCourseButton = new Button("← Remove ←");
        removeCourseButton.getStyleClass().add("button-danger");
        removeCourseButton.setOnAction(e -> removeCourseFromStudent());
        buttonBox.getChildren().addAll(addCourseButton, removeCourseButton);

        VBox assignedBox = new VBox(5);
        Label assignedLabel = new Label("Assigned Courses:");
        assignedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        assignedCoursesListView = new ListView<>();
        assignedCoursesListView.setPrefHeight(200);
        assignedCoursesListView.setPrefWidth(300);
        assignedBox.getChildren().addAll(assignedLabel, assignedCoursesListView);

        panel.getChildren().addAll(availableBox, buttonBox, assignedBox);
        return panel;
    }

    private HBox createStatisticsPanel() {
        HBox panel = new HBox(30);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("statistics-panel");

        gpaLabel = new Label("GPA: 0.00");
        gpaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gpaLabel.setTextFill(Color.web("#2c3e50"));

        creditsLabel = new Label("Credits: 0");
        creditsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        creditsLabel.setTextFill(Color.web("#2c3e50"));

        panel.getChildren().addAll(gpaLabel, creditsLabel);
        return panel;
    }

    private VBox createTranscriptPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("transcript-panel");

        Label transcriptLabel = new Label("Transcript Preview");
        transcriptLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        transcriptTextArea = new TextArea();
        transcriptTextArea.setPrefHeight(400);
        transcriptTextArea.setEditable(false);
        transcriptTextArea.setWrapText(true);
        transcriptTextArea.setFont(Font.font("Courier New", 13));
        transcriptTextArea.getStyleClass().add("transcript-textarea");

        panel.getChildren().addAll(transcriptLabel, transcriptTextArea);
        return panel;
    }

    private HBox createButtonPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));

        printButton = new Button("Print Transcript");
        printButton.getStyleClass().add("button-primary");
        printButton.setOnAction(e -> printTranscript());

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("button-secondary");
        clearButton.setOnAction(e -> clearTranscript());

        panel.getChildren().addAll(printButton, clearButton);
        return panel;
    }

    private void loadStudentCourses(Student student) {
        assignedCoursesListView.setItems(FXCollections.observableArrayList(student.getEnrolledCourses()));
        updateAvailableCourses(student);
    }

    private void updateAvailableCourses(Student student) {
        ObservableList<Course> available = FXCollections.observableArrayList(courseController.getCourses());
        available.removeAll(student.getEnrolledCourses());
        availableCoursesListView.setItems(available);
    }

    private void addCourseToStudent() {
        Student student = studentComboBox.getValue();
        Course course = availableCoursesListView.getSelectionModel().getSelectedItem();

        if (student == null) {
            showAlert("No Student", "Please select a student first.", Alert.AlertType.WARNING);
            return;
        }

        if (course == null) {
            showAlert("No Course", "Please select a course to add.", Alert.AlertType.WARNING);
            return;
        }

        student.addCourse(course);
        loadStudentCourses(student);
        updateStatistics(student);
        showAlert("Success", "Course added to student successfully!", Alert.AlertType.INFORMATION);
    }

    private void removeCourseFromStudent() {
        Student student = studentComboBox.getValue();
        Course course = assignedCoursesListView.getSelectionModel().getSelectedItem();

        if (student == null) {
            showAlert("No Student", "Please select a student first.", Alert.AlertType.WARNING);
            return;
        }

        if (course == null) {
            showAlert("No Course", "Please select a course to remove.", Alert.AlertType.WARNING);
            return;
        }

        student.removeCourse(course);
        loadStudentCourses(student);
        updateStatistics(student);
        showAlert("Success", "Course removed from student successfully!", Alert.AlertType.INFORMATION);
    }

    private void updateStatistics(Student student) {
        if (student != null) {
            student.calculateGPA();
            gpaLabel.setText("GPA: " + String.format("%.2f", student.getGpa()));
            Transcript transcript = student.getTranscript();
            creditsLabel.setText("Credits: " + transcript.getTotalCreditsAttempted() +
                    " (Attempted) | " + transcript.getTotalCreditsEarned() + " (Earned)");
        }
    }

    private void generateTranscript() {
        Student student = studentComboBox.getValue();
        if (student == null) {
            showAlert("No Student", "Please select a student first.", Alert.AlertType.WARNING);
            return;
        }

        StringBuilder transcript = new StringBuilder();
        transcript.append("=".repeat(80)).append("\n");
        transcript.append("              ACADEMIC TRANSCRIPT\n");
        transcript.append("=".repeat(80)).append("\n\n");

        transcript.append("Student Information:\n");
        transcript.append("-".repeat(50)).append("\n");
        transcript.append("Name: ").append(student.getFullName()).append("\n");
        transcript.append("Student ID: ").append(student.getStudentId()).append("\n");
        transcript.append("Major: ").append(student.getMajor()).append("\n");
        transcript.append("Date of Birth: ").append(student.getFormattedDateOfBirth()).append("\n");
        transcript.append("Enrollment Year: ").append(student.getEnrollmentYear()).append("\n");
        transcript.append("\n");

        transcript.append("Course History:\n");
        transcript.append("-".repeat(80)).append("\n");
        transcript.append(String.format("%-15s %-40s %-10s %-10s\n",
                "Code", "Course Name", "Credits", "Grade"));
        transcript.append("-".repeat(80)).append("\n");

        for (Course course : student.getEnrolledCourses()) {
            transcript.append(String.format("%-15s %-40s %-10d %-10s\n",
                    course.getCourseCode(),
                    course.getCourseName().length() > 40 ?
                            course.getCourseName().substring(0, 37) + "..." :
                            course.getCourseName(),
                    course.getCredits(),
                    course.getGrade().getDisplay()));
        }

        transcript.append("-".repeat(80)).append("\n");
        transcript.append("\n");

        Transcript studentTranscript = student.getTranscript();
        transcript.append("Academic Summary:\n");
        transcript.append("-".repeat(50)).append("\n");
        transcript.append("Cumulative GPA: ").append(String.format("%.2f", studentTranscript.getCumulativeGPA())).append("\n");
        transcript.append("Total Credits Attempted: ").append(studentTranscript.getTotalCreditsAttempted()).append("\n");
        transcript.append("Total Credits Earned: ").append(studentTranscript.getTotalCreditsEarned()).append("\n");

        transcript.append("\nGrade Distribution:\n");
        transcript.append(studentTranscript.getGradeDistribution());

        transcript.append("\n").append("=".repeat(80)).append("\n");
        transcript.append("Transcript Generated: ").append(
                studentTranscript.getGeneratedDate().format(
                        DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm:ss"))
        ).append("\n");
        transcript.append("=".repeat(80));

        transcriptTextArea.setText(transcript.toString());
    }

    private void printTranscript() {
        String content = transcriptTextArea.getText();
        if (content.isEmpty()) {
            showAlert("No Transcript", "Please generate a transcript first.", Alert.AlertType.WARNING);
            return;
        }

        // Show print dialog (simplified)
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Print Transcript");
        infoAlert.setHeaderText("Printing Transcript");
        infoAlert.setContentText("In a production environment, this would print the transcript.\n" +
                "Current transcript is displayed in the text area.");
        infoAlert.showAndWait();
    }

    private void clearTranscript() {
        transcriptTextArea.clear();
        assignedCoursesListView.getItems().clear();
        availableCoursesListView.getItems().clear();
        if (studentComboBox.getValue() != null) {
            loadStudentCourses(studentComboBox.getValue());
        }
        gpaLabel.setText("GPA: 0.00");
        creditsLabel.setText("Credits: 0");
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}