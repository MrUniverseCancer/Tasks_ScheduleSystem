package org.example.GUI_design;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
        Label name = new Label(task_name);
        Label task_LeftTime        = new Label(LeftTime.toString());
        Label task_ImportanceLevel = new Label(ImportanceLevel.toString());
        Label task_TaskConsuming   = new Label(TaskConsuming.toString());
        Label task_PunishLevel     = new Label(PunishLevel.toString());
        Label task_PreferenceLevel = new Label(PreferenceLevel.toString());
        Label task_DifficultyLevel = new Label(DifficultyLevel.toString());
        Label task_BufferbtwTasks  = new Label(BufferbtwTasks.toString());
        Label task_singalTaskTime  = new Label(singalTaskTime.toString());
        Label task_FactImportance  = new Label(FactImportance.toString());
        Label task_FactUrgency     = new Label(FactUrgency.toString());
        card.getChildren().addAll(name, task_LeftTime, task_ImportanceLevel,
                task_TaskConsuming, task_PunishLevel, task_PreferenceLevel,
                task_DifficultyLevel, task_BufferbtwTasks, task_singalTaskTime,
                task_FactImportance, task_FactUrgency);
        return card;
    }
}
