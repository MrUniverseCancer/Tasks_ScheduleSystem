package org.example.GUI_design;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.example.server.ID_maintain;
import org.example.server.add_change_del_task;

import java.util.concurrent.atomic.AtomicBoolean;

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
    int    ID;
    String task_description;
    Button exit_button;

    private task_showing task_showing;

    public task_card(String[] list){
        this.head = list;
        task_name = list[0];
        LeftTime         = Double.parseDouble(list[1]);
        ImportanceLevel  = Double.parseDouble(list[2]);
        TaskConsuming    = Double.parseDouble(list[3]);
        PunishLevel      = Double.parseDouble(list[4]);
        PreferenceLevel  = Double.parseDouble(list[5]);
        DifficultyLevel  = Double.parseDouble(list[6]);
        BufferbtwTasks   = Double.parseDouble(list[7]);
        singalTaskTime   = Double.parseDouble(list[8]);
        FactImportance   = Double.parseDouble(list[9]);
        FactUrgency      = Double.parseDouble(list[10]);
        ID               = Integer.parseInt(list[11]);
        task_description = list[12];
    }

    public Pane getCard1(int index, int status, Main_Page main_page){
        VBox card = new VBox();
        card.setStyle("-fx-background-color: lightgray;-fx-border-color: black;");
        card.setSpacing(1); // Set the spacing between rows

        HBox hbox = new HBox();
        exit_button = new Button("Exit");
        Button button2 = new Button("Edit");
        Button button3 = new Button("Delete");
        hbox.getChildren().addAll(exit_button, button2, button3);
        hbox.setSpacing(10);
        button2.setOnAction(e -> {
            Task_add task_add = new Task_add();
            task_add.openWindows_Task_add(task_name, FactImportance.intValue(), FactUrgency.intValue(), ID, task_description, status, main_page);
        });
        button3.setOnAction(e -> {
            int result = add_change_del_task.delete_task(task_name, FactImportance.intValue(), FactUrgency.intValue());
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


        Label name = new Label(task_name);
        name.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");
        // Create a Line for separation
        Line separator1 = new Line();
        separator1.setStroke(Color.BLACK);
        separator1.setStrokeWidth(3);
        separator1.setStartX(0);
        separator1.endXProperty().bind(card.widthProperty().multiply(0.9));

        Button task_LeftTime        = new Button("LeftTime : "+LeftTime.toString());
        Button task_ImportanceLevel = new Button("ImportanceLevel : "+ImportanceLevel.toString());
        Button task_TaskConsuming   = new Button("TaskConsuming : "+TaskConsuming.toString());
        Button task_PunishLevel     = new Button("PunishLevel : "+PunishLevel.toString());
        Button task_PreferenceLevel = new Button("PreferenceLevel : "+PreferenceLevel.toString());
        Button task_DifficultyLevel = new Button("DifficultyLevel : "+DifficultyLevel.toString());
        Button task_BufferbtwTasks  = new Button("BufferbtwTasks : "+BufferbtwTasks.toString());
        Button task_singalTaskTime  = new Button("singalTaskTime : "+singalTaskTime.toString());
        Button task_FactImportance  = new Button("FactImportance : "+FactImportance.toString());
        Button task_FactUrgency     = new Button("FactUrgency : "+FactUrgency.toString());
        if(index == 1){
            // 在任务界面，希望字体更大
            name.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");
            task_LeftTime.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_ImportanceLevel.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_TaskConsuming.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_PunishLevel.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_PreferenceLevel.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_DifficultyLevel.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_BufferbtwTasks.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_singalTaskTime.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_FactImportance.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
            task_FactUrgency.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");
        }
        card.getChildren().addAll(hbox, name, separator1,task_LeftTime, task_ImportanceLevel,
                task_TaskConsuming, task_PunishLevel, task_PreferenceLevel,
                task_DifficultyLevel, task_BufferbtwTasks, task_singalTaskTime,
                task_FactImportance, task_FactUrgency);
        return card;
    }

    public Button getCard2(){
        Button card = new Button(task_name);
        card.setStyle("-fx-background-color: lightyellow;");
        return card;
    }

    public Pane getTaskCard(){
        StackPane card = new StackPane();
        Label name = new Label(task_name);
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");;
        card.getChildren().add(name);

        // 创建一条底部的黑色直线
        Line bottomLine = new Line();
        bottomLine.setStartX(0);
        bottomLine.endXProperty().bind(card.widthProperty());
        bottomLine.setStroke(Color.BLACK);
        bottomLine.setStrokeWidth(5);
        bottomLine.startYProperty().bind(card.heightProperty().subtract(5));
        bottomLine.endYProperty().bind(card.heightProperty().subtract(5));

//        card.getChildren().add(bottomLine);
        // 设置Pane的边框
        card.setBorder(new Border(new BorderStroke(
                Color.BLACK,           // 边框颜色
                BorderStrokeStyle.SOLID, // 边框样式
                null,                 // 边框圆角
                new BorderWidths(3)   // 边框宽度
        )));
        // 使用Insets添加额外的空间以增加Pane的实际大小
        card.setPadding(new Insets(20)); // 添加10像素的内边距以增加边缘空间
        return card;
    }

    public Button getExit_button() {
        return exit_button;
    }
}
