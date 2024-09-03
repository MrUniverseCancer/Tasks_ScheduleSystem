package org.example.GUI_design;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class task_card {
    String [] head;
    String task_name;
    Double LeftTime;
    Double ImportanceLevel;
    Double TaskConsuming;
    Double PunishLevel;
    Double PreferenceLevel;
    Double DifficultyLevel;
    Double BufferbtwTasks;
    Double singalTaskTime;
    Double FactImportance;
    Double FactUrgency;

    public task_card(String[] list){
        this.head = list;
        task_name = list[0];
        LeftTime        = Double.parseDouble(list[1]);
        ImportanceLevel = Double.parseDouble(list[2]);
        TaskConsuming   = Double.parseDouble(list[3]);
        PunishLevel     = Double.parseDouble(list[4]);
        PreferenceLevel = Double.parseDouble(list[5]);
        DifficultyLevel = Double.parseDouble(list[6]);
        BufferbtwTasks  = Double.parseDouble(list[7]);
        singalTaskTime  = Double.parseDouble(list[8]);
        FactImportance  = Double.parseDouble(list[9]);
        FactUrgency     = Double.parseDouble(list[10]);
    }

    public Pane getCard(){
        VBox card = new VBox();
        card.setStyle("-fx-background-color: green;");
        card.setSpacing(10); // Set the spacing between rows
        Button button = new Button("Exit");
        Label name = new Label(task_name);
        // Create a Line for separation
        Line separator1 = new Line();
        separator1.setStroke(Color.BLACK);
        separator1.setStrokeWidth(3);
        separator1.setStartX(0);
        separator1.endXProperty().bind(card.widthProperty().multiply(0.9));

        Label task_LeftTime        = new Label("LeftTime"+LeftTime.toString());
        Label task_ImportanceLevel = new Label("ImportanceLevel"+ImportanceLevel.toString());
        Label task_TaskConsuming   = new Label("TaskConsuming"+TaskConsuming.toString());
        Label task_PunishLevel     = new Label("PunishLevel"+PunishLevel.toString());
        Label task_PreferenceLevel = new Label("PreferenceLevel"+PreferenceLevel.toString());
        Label task_DifficultyLevel = new Label("DifficultyLevel"+DifficultyLevel.toString());
        Label task_BufferbtwTasks  = new Label("BufferbtwTasks"+BufferbtwTasks.toString());
        Label task_singalTaskTime  = new Label("singalTaskTime"+singalTaskTime.toString());
        Label task_FactImportance  = new Label("FactImportance"+FactImportance.toString());
        Label task_FactUrgency     = new Label("FactUrgency"+FactUrgency.toString());
        card.getChildren().addAll(button, name, separator1,task_LeftTime, task_ImportanceLevel,
                task_TaskConsuming, task_PunishLevel, task_PreferenceLevel,
                task_DifficultyLevel, task_BufferbtwTasks, task_singalTaskTime,
                task_FactImportance, task_FactUrgency);
        return card;
    }
}
