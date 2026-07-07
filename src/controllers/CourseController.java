
package controllers;

import models.Course;
import models.Grade;
import utils.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CourseController {
    private ObservableList<Course> courses;
    private TableView<Course> tableView;
    private TextField courseCodeField, courseNameField, creditsField, instructorField;
    private ComboBox<String> semesterComboBox;
    private ComboBox<Integer> yearComboBox;
    private ComboBox<Grade> gradeComboBox;
    private Button addButton, updateButton, deleteButton, clearButton;

    public CourseController() {
        courses = FXCollections.observableArrayList();
        initializeSampleData();
    }

    public VBox getView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("course-view");

        Label headerLabel = new Label("Course Management");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        GridPane form = createForm();
        tableView = createTable();
        HBox buttonPanel = createButtonPanel();

        view.getChildren().addAll(headerLabel, form, buttonPanel, tableView);
        return view;
    }

    private GridPane createForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.getStyleClass().add("form-grid");

        Label courseCodeLabel = new Label("Course Code:");
        courseCodeField = new TextField();
        courseCodeField.setPromptText("e.g., CS101");

        Label courseNameLabel = new Label("Course Name:");
        courseNameField = new TextField();
        courseNameField.setPromptText("Enter course name");

        Label creditsLabel = new Label("Credits:");
        creditsField = new TextField();
        creditsField.setPromptText("Enter credits");

        Label instructorLabel = new Label("Instructor:");
        instructorField = new TextField();
        instructorField.setPromptText("Enter instructor name");

        Label semesterLabel = new Label("Semester:");
        semesterComboBox = new ComboBox<>();
        semesterComboBox.getItems().addAll("Spring", "Summer", "Fall", "Winter");
        semesterComboBox.setValue("Fall");

        Label yearLabel = new Label("Year:");
        yearComboBox = new ComboBox<>();
        yearComboBox.getItems().addAll(2020, 2021, 2022, 2023, 2024);
        yearComboBox.setValue(2024);

        Label gradeLabel = new Label("Grade:");
        gradeComboBox = new ComboBox<>();
        gradeComboBox.getItems().addAll(Grade.values());
        gradeComboBox.setValue(Grade.NOT_GRADED);

        grid.add(courseCodeLabel, 0, 0);
        grid.add(courseCodeField, 1, 0);
        grid.add(courseNameLabel, 2, 0);
        grid.add(courseNameField, 3, 0);
        grid.add(creditsLabel, 0, 1);
        grid.add(creditsField, 1, 1);
        grid.add(instructorLabel, 2, 1);
        grid.add(instructorField, 3, 1);
        grid.add(semesterLabel, 0, 2);
        grid.add(semesterComboBox, 1, 2);
        grid.add(yearLabel, 2, 2);
        grid.add(yearComboBox, 3, 2);
        grid.add(gradeLabel, 0, 3);
        grid.add(gradeComboBox, 1, 3);

        return grid;
    }

    private TableView<Course> createTable() {
        TableView<Course> table = new TableView<>();
        table.getStyleClass().add("course-table");

        TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(100);

        TableColumn<Course, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        nameCol.setPrefWidth(200);

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        creditsCol.setPrefWidth(80);

        TableColumn<Course, String> instructorCol = new TableColumn<>("Instructor");
        instructorCol.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        instructorCol.setPrefWidth(150);

        TableColumn<Course, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        semesterCol.setPrefWidth(100);

        TableColumn<Course, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeCol.setPrefWidth(80);

        table.getColumns().addAll(codeCol, nameCol, creditsCol, instructorCol, semesterCol, gradeCol);
        table.setItems(courses);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
            }
        });

        return table;
    }

    private HBox createButtonPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));

        addButton = new Button("Add Course");
        addButton.getStyleClass().add("button-primary");
        addButton.setOnAction(e -> addCourse());

        updateButton = new Button("Update Course");
        updateButton.getStyleClass().add("button-secondary");
        updateButton.setOnAction(e -> updateCourse());

        deleteButton = new Button("Delete Course");
        deleteButton.getStyleClass().add("button-danger");
        deleteButton.setOnAction(e -> deleteCourse());

        clearButton = new Button("Clear Form");
        clearButton.getStyleClass().add("button-secondary");
        clearButton.setOnAction(e -> clearForm());

        panel.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        return panel;
    }

    private void addCourse() {
        try {
            // Validate input
            ValidationUtils.validateNotEmpty(courseCodeField.getText(), "Course Code");
            ValidationUtils.validateNotEmpty(courseNameField.getText(), "Course Name");
            ValidationUtils.validatePositiveInteger(creditsField.getText(), "Credits");

            int credits = Integer.parseInt(creditsField.getText());

            Course course = new Course(
                    courseCodeField.getText(),
                    courseNameField.getText(),
                    credits,
                    instructorField.getText(),
                    semesterComboBox.getValue(),
                    yearComboBox.getValue()
            );

            // Set grade if not NOT_GRADED
            if (gradeComboBox.getValue() != Grade.NOT_GRADED) {
                course.setGrade(gradeComboBox.getValue());
            }

            courses.add(course);
            clearForm();
            showAlert("Success", "Course added successfully!", Alert.AlertType.INFORMATION);

        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add course: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateCourse() {
        Course selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a course to update.", Alert.AlertType.WARNING);
            return;
        }

        try {
            ValidationUtils.validateNotEmpty(courseCodeField.getText(), "Course Code");
            ValidationUtils.validateNotEmpty(courseNameField.getText(), "Course Name");
            ValidationUtils.validatePositiveInteger(creditsField.getText(), "Credits");

            selected.setCourseCode(courseCodeField.getText());
            selected.setCourseName(courseNameField.getText());
            selected.setCredits(Integer.parseInt(creditsField.getText()));
            selected.setInstructor(instructorField.getText());
            selected.setSemester(semesterComboBox.getValue());
            selected.setYear(yearComboBox.getValue());
            selected.setGrade(gradeComboBox.getValue());

            tableView.refresh();
            clearForm();
            showAlert("Success", "Course updated successfully!", Alert.AlertType.INFORMATION);

        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update course: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteCourse() {
        Course selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a course to delete.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Course");
        confirmAlert.setContentText("Are you sure you want to delete " + selected.getCourseName() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            courses.remove(selected);
            clearForm();
            showAlert("Success", "Course deleted successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private void populateForm(Course course) {
        courseCodeField.setText(course.getCourseCode());
        courseNameField.setText(course.getCourseName());
        creditsField.setText(String.valueOf(course.getCredits()));
        instructorField.setText(course.getInstructor());
        semesterComboBox.setValue(course.getSemester());
        yearComboBox.setValue(course.getYear());
        gradeComboBox.setValue(course.getGrade());
    }

    private void clearForm() {
        courseCodeField.clear();
        courseNameField.clear();
        creditsField.clear();
        instructorField.clear();
        semesterComboBox.setValue("Fall");
        yearComboBox.setValue(2024);
        gradeComboBox.setValue(Grade.NOT_GRADED);
        tableView.getSelectionModel().clearSelection();
    }

    private void initializeSampleData() {
        Course c1 = new Course("CS101", "Introduction to Computer Science", 3,
                "Dr. Brown", "Fall", 2024);
        c1.setGrade(Grade.A);

        Course c2 = new Course("MATH201", "Calculus I", 4,
                "Dr. Smith", "Fall", 2024);
        c2.setGrade(Grade.B_PLUS);

        Course c3 = new Course("ENG101", "English Composition", 3,
                "Prof. Johnson", "Fall", 2024);
        c3.setGrade(Grade.A_MINUS);

        courses.addAll(c1, c2, c3);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ObservableList<Course> getCourses() {
        return courses;
    }
}
