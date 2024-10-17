package org.example.GUI_design;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.example.server.TaskAdapter;
import org.example.server.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class task_card {
    private Task task;
    private Button exit_button;

    private task_showing task_showing;

    public task_card(Task task) {
        this.task = task;
    }

    public Pane getCard1(int index, int status, Main_Page main_page) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: lightgray;-fx-border-color: black;");
        card.setSpacing(1);

        HBox hbox = new HBox();
        exit_button = new Button("Exit");
        Button button2 = new Button("Edit");
        Button button3 = new Button("Delete");
        hbox.getChildren().addAll(exit_button, button2, button3);
        hbox.setSpacing(10);
        button2.setOnAction(e -> {
            Task_add task_add = new Task_add();
            task_add.openWindows_Task_add(task.getName(), task.getImportance(), task.getUrgency(), task.getUid(), task.getDescription(), status, main_page);
        });
        button3.setOnAction(e -> {
            int result = TaskAdapter.deleteTask(task.getName(), task.getImportance(), task.getUrgency());
            String message = (result == 1) ? "数据文件不存在或损坏" : "删除失败";
            if(result != 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Promotion");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
            else {
                main_page.refresh_scene(status);
            }
        });

        Label name = new Label(task.getName());
        name.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");

        Line separator1 = new Line();
        separator1.setStroke(Color.BLACK);
        separator1.setStrokeWidth(3);
        separator1.setStartX(0);
        separator1.endXProperty().bind(card.widthProperty().multiply(0.9));

        Button task_UID = new Button("UID: " + task.getUid());
        Button task_Description = new Button("Description: " + task.getDescription());
        Button task_Importance = new Button("Importance: " + task.getImportance());
        Button task_Urgency = new Button("Urgency: " + task.getUrgency());
        Button task_TaskTime = new Button("Task Time: " + (task.getTaskTime() != null ? task.getTaskTime() : "N/A"));
        Button task_DDL = new Button("DDL: " + (task.getDdl() != null ? task.getDdl().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "N/A"));

        if(index == 1) {
            name.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");
            task_UID.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_Description.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_Importance.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_Urgency.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_TaskTime.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_DDL.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
        }

        card.getChildren().addAll(hbox, name, separator1, task_UID, task_Description,
                task_Importance, task_Urgency, task_TaskTime, task_DDL);
        return card;
    }

    public Button getCard2() {
        Button card = new Button(task.getName());
        card.setStyle("-fx-background-color: lightyellow;");
        return card;
    }

    public Pane getTaskCard() {
        StackPane card = new StackPane();
        Label name = new Label(task.getName());
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");
        card.getChildren().add(name);

        Line bottomLine = new Line();
        bottomLine.setStartX(0);
        bottomLine.endXProperty().bind(card.widthProperty());
        bottomLine.setStroke(Color.BLACK);
        bottomLine.setStrokeWidth(5);
        bottomLine.startYProperty().bind(card.heightProperty().subtract(5));
        bottomLine.endYProperty().bind(card.heightProperty().subtract(5));

        card.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(3)
        )));
        card.setPadding(new Insets(20));
        return card;
    }

    public Button getExit_button() {
        return exit_button;
    }
}
