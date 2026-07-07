
package application;

import controllers.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private MainController mainController;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize main controller
            mainController = new MainController();

            // Create root layout
            rootLayout = new BorderPane();

            // Set up the main scene
            Scene scene = new Scene(rootLayout, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            // Configure stage
            primaryStage.setTitle("Academic Transcript Generation System");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.show();

            // Initialize the main view
            mainController.initRootLayout(rootLayout);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}