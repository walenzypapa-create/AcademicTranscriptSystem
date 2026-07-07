
package controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class MainController {
    private StudentController studentController;
    private CourseController courseController;
    private TranscriptController transcriptController;
    private TabPane tabPane;

    public MainController() {
        studentController = new StudentController();
        courseController = new CourseController();
        transcriptController = new TranscriptController();
    }

    public void initRootLayout(BorderPane rootLayout) {
        // Create header
        VBox header = createHeader();
        rootLayout.setTop(header);

        // Create tab pane
        tabPane = new TabPane();
        tabPane.getStyleClass().add("tab-pane");

        // Add tabs
        Tab studentTab = new Tab("Student Management", studentController.getView());
        Tab courseTab = new Tab("Course Management", courseController.getView());
        Tab transcriptTab = new Tab("Transcript Generation", transcriptController.getView());

        studentTab.setClosable(false);
        courseTab.setClosable(false);
        transcriptTab.setClosable(false);

        tabPane.getTabs().addAll(studentTab, courseTab, transcriptTab);

        rootLayout.setCenter(tabPane);

        // Create footer
        HBox footer = createFooter();
        rootLayout.setBottom(footer);
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db);" +
                "-fx-padding: 15px;");

        Label title = new Label("🎓 Academic Transcript Generation System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Manage Students, Courses, and Generate Transcripts");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#ecf0f1"));

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10px; -fx-alignment: center;");

        Label statusLabel = new Label("© 2024 Academic Transcript System | All Rights Reserved");
        statusLabel.setTextFill(Color.web("#ecf0f1"));

        footer.getChildren().add(statusLabel);
        return footer;
    }
}
