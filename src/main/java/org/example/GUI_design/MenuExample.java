package org.example.GUI_design;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MenuExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a BorderPane
        BorderPane borderPane = new BorderPane();

        // Create a GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows

        // Add panes to the GridPane
        for (int i = 0; i < 9; i++) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

            // Optionally, add a button or other controls to each pane
            Button button = new Button("Button " + (i + 1));
            pane.getChildren().add(button);

            // Add the pane to the GridPane at row and column position
            gridPane.add(pane, i % 3, i / 3); // Place in 3x3 grid
        }

        // Set the GridPane in the center of the BorderPane
        borderPane.setCenter(gridPane);

        // Create a scene and add the BorderPane to it
        Scene scene = new Scene(borderPane, 600, 600);

        primaryStage.setTitle("BorderPane with GridPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
