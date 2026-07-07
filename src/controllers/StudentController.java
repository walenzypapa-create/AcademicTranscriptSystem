
package controllers;

import javafx.scene.layout.HBox;
import models.Student;
import utils.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    private ObservableList<Student> students;
    private TableView<Student> tableView;
    private TextField firstNameField, lastNameField, studentIdField, majorField;
    private DatePicker dobPicker;
    private ComboBox<Integer> yearComboBox;
    private Button addButton, updateButton, deleteButton, clearButton;

    public StudentController() {
        students = FXCollections.observableArrayList();
        initializeSampleData();
    }

    public VBox getView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("student-view");

        Label headerLabel = new Label("Student Management");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Create form
        GridPane form = createForm();

        // Create table
        tableView = createTable();

        // Button panel
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

        // Labels and fields
        Label firstNameLabel = new Label("First Name:");
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");

        Label lastNameLabel = new Label("Last Name:");
        lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");

        Label studentIdLabel = new Label("Student ID:");
        studentIdField = new TextField();
        studentIdField.setPromptText("Enter student ID");

        Label majorLabel = new Label("Major:");
        majorField = new TextField();
        majorField.setPromptText("Enter major");

        Label dobLabel = new Label("Date of Birth:");
        dobPicker = new DatePicker();
        dobPicker.setPromptText("Select date of birth");

        Label yearLabel = new Label("Enrollment Year:");
        yearComboBox = new ComboBox<>();
        yearComboBox.getItems().addAll(2020, 2021, 2022, 2023, 2024);
        yearComboBox.setValue(2024);

        // Add to grid
        grid.add(firstNameLabel, 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(lastNameLabel, 2, 0);
        grid.add(lastNameField, 3, 0);
        grid.add(studentIdLabel, 0, 1);
        grid.add(studentIdField, 1, 1);
        grid.add(majorLabel, 2, 1);
        grid.add(majorField, 3, 1);
        grid.add(dobLabel, 0, 2);
        grid.add(dobPicker, 1, 2);
        grid.add(yearLabel, 2, 2);
        grid.add(yearComboBox, 3, 2);

        return grid;
    }

    private TableView<Student> createTable() {
        TableView<Student> table = new TableView<>();
        table.getStyleClass().add("student-table");

        TableColumn<Student, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(100);

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(150);

        TableColumn<Student, String> studentIdCol = new TableColumn<>("Student ID");
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentIdCol.setPrefWidth(120);

        TableColumn<Student, String> majorCol = new TableColumn<>("Major");
        majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));
        majorCol.setPrefWidth(150);

        TableColumn<Student, Double> gpaCol = new TableColumn<>("GPA");
        gpaCol.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        gpaCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, nameCol, studentIdCol, majorCol, gpaCol);
        table.setItems(students);

        // Selection listener
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

        addButton = new Button("Add Student");
        addButton.getStyleClass().add("button-primary");
        addButton.setOnAction(e -> addStudent());

        updateButton = new Button("Update Student");
        updateButton.getStyleClass().add("button-secondary");
        updateButton.setOnAction(e -> updateStudent());

        deleteButton = new Button("Delete Student");
        deleteButton.getStyleClass().add("button-danger");
        deleteButton.setOnAction(e -> deleteStudent());

        clearButton = new Button("Clear Form");
        clearButton.getStyleClass().add("button-secondary");
        clearButton.setOnAction(e -> clearForm());

        panel.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        return panel;
    }

    private void addStudent() {
        try {
            // Validate input
            ValidationUtils.validateNotEmpty(firstNameField.getText(), "First Name");
            ValidationUtils.validateNotEmpty(lastNameField.getText(), "Last Name");
            ValidationUtils.validateNotEmpty(studentIdField.getText(), "Student ID");
            ValidationUtils.validateNotEmpty(majorField.getText(), "Major");

            LocalDate dob = dobPicker.getValue();
            if (dob == null) {
                throw new IllegalArgumentException("Date of birth is required");
            }

            int enrollmentYear = yearComboBox.getValue();
            if (enrollmentYear == 0) {
                throw new IllegalArgumentException("Enrollment year is required");
            }

            // Create student
            String id = "S" + System.currentTimeMillis();
            Student student = new Student(id, firstNameField.getText(),
                    lastNameField.getText(), dob,
                    studentIdField.getText(),
                    majorField.getText(), enrollmentYear);

            students.add(student);
            clearForm();
            showAlert("Success", "Student added successfully!", Alert.AlertType.INFORMATION);

        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add student: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateStudent() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to update.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Validate input
            ValidationUtils.validateNotEmpty(firstNameField.getText(), "First Name");
            ValidationUtils.validateNotEmpty(lastNameField.getText(), "Last Name");
            ValidationUtils.validateNotEmpty(studentIdField.getText(), "Student ID");
            ValidationUtils.validateNotEmpty(majorField.getText(), "Major");

            LocalDate dob = dobPicker.getValue();
            if (dob == null) {
                throw new IllegalArgumentException("Date of birth is required");
            }

            // Update student
            selected.setFirstName(firstNameField.getText());
            selected.setLastName(lastNameField.getText());
            selected.setStudentId(studentIdField.getText());
            selected.setMajor(majorField.getText());
            selected.setDateOfBirth(dob);
            selected.setEnrollmentYear(yearComboBox.getValue());

            tableView.refresh();
            clearForm();
            showAlert("Success", "Student updated successfully!", Alert.AlertType.INFORMATION);

        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update student: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteStudent() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to delete.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Student");
        confirmAlert.setContentText("Are you sure you want to delete " + selected.getFullName() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            students.remove(selected);
            clearForm();
            showAlert("Success", "Student deleted successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private void populateForm(Student student) {
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        studentIdField.setText(student.getStudentId());
        majorField.setText(student.getMajor());
        dobPicker.setValue(student.getDateOfBirth());
        yearComboBox.setValue(student.getEnrollmentYear());
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        studentIdField.clear();
        majorField.clear();
        dobPicker.setValue(null);
        yearComboBox.setValue(2024);
        tableView.getSelectionModel().clearSelection();
    }

    private void initializeSampleData() {
        // Add some sample students
        Student s1 = new Student("S1", "John", "Doe", LocalDate.of(2000, 5, 15),
                "STU001", "Computer Science", 2022);
        Student s2 = new Student("S2", "Jane", "Smith", LocalDate.of(2001, 8, 22),
                "STU002", "Engineering", 2022);
        Student s3 = new Student("S3", "Michael", "Johnson", LocalDate.of(1999, 11, 3),
                "STU003", "Mathematics", 2021);

        students.addAll(s1, s2, s3);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ObservableList<Student> getStudents() {
        return students;
    }
}