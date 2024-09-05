package org.example.GUI_design;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static java.util.Collections.replaceAll;

import org.example.server.add_change_del_task;


public class Task_add {
    private String task_name;
    private int Fact_importance;
    private int Fact_urgency;
    private boolean isFinish[] = new boolean[11];


    public Task_add(){

    }

    public void openWindows_Task_add(){
        // Open a new window to add a new task
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Set the window to be modal

        Scene scene = new Scene(new Pane(), 1500, 800);
        stage.setScene(scene);

        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: lightgray;");
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(100));
//        vBox.setAlignment(Pos.CENTER);
        vBox.prefWidthProperty().bind(scrollPane.widthProperty());
        vBox.prefHeightProperty().bind(scrollPane.heightProperty());
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBox);
        scene.setRoot(scrollPane);

        // 头部的楷体标题
        Label head_title = new Label("增加任务");
        head_title.setStyle("-fx-font-size: 60px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");;
        head_title.prefWidthProperty().bind(vBox.widthProperty().multiply(0.9));
        head_title.setAlignment(Pos.CENTER); // 设置文本居中对齐
        vBox.getChildren().add(head_title);

        HBox name_hbox = getTag("任务名称 : ");
        vBox.getChildren().add(name_hbox);

        for(int i = 1; i < 9; i++){
            isFinish[i] = true;
        }

        HBox Fact_importance_hbox = getNum("重要程度 : ", 9);
        vBox.getChildren().add(Fact_importance_hbox);

        HBox Fact_urgency_hbox = getNum("紧急程度 : ", 10);
        vBox.getChildren().add(Fact_urgency_hbox);

        Button button = getButton("确认");
//        button.layoutXProperty().bind(vBox.widthProperty=ty().multiply(0.6));
        vBox.getChildren().add(button);


        stage.show();
    }

    public HBox getTag(String tag_name){
        // 创建 Label 和 TextField
        Label label = new Label(tag_name);
        label.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");;
        TextField textField = new TextField();
        textField.setPrefHeight(40); // 设置 TextField 的高度

        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            // 将最新文本放入结果中
            task_name = newValue;
            isFinish[0] = true;
        });

        // 创建 HBox 布局，并将 Label 和 TextField 添加进去
        HBox hbox = new HBox(100); // 10 是 Label 和 TextField 之间的间距
        hbox.setPadding(new Insets(20)); // 设置 HBox 的内边距
        hbox.setAlignment(Pos.CENTER_LEFT); // 将子节点对齐到左侧
        hbox.getChildren().addAll(label, textField);
        return hbox;
    }

    public HBox getNum(String tag_name, int i){
        // 创建 Label 和 TextField
        Label label1 = new Label(tag_name);
        label1.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");;

        TextField textField = new TextField();
        textField.setPrefHeight(20); // 设置 TextField 的高度

        // 创建一个 Spinner，范围从 1 到 100
        Spinner<Integer> spinner = new Spinner<>(1, 7, 1);
        spinner.setEditable(true); // 允许手动输入
        spinner.getValueFactory().setValue(4); // 设置默认值
        // 创建一个 Label 来显示 Spinner 选择的值
        Label label2 = new Label("Selected number: " + spinner.getValue());
        label2.setStyle("-fx-font-size: 20px; -fx-font-family: 'KaiTi';");




        Label label3 = new Label("请在文本框输入1到100的整数\n或者调整选择器（范围1-7）"); // 展示最终值或者报错信息
        label3.setStyle("-fx-font-size: 20px;");


        // 添加监听器来响应 TextField 文本的变化
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            // 如果文本不是数字，则将文本设置为旧值
            check(newValue, spinner.getValue(), label3, i);
        });

        // 监听 Spinner 值的变化
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            label2.setText("Selected number: " + newValue);
            check(textField.getText(), newValue, label3, i);
        });
        // 创建 HBox 布局，并将 Label 和 TextField 添加进去
        HBox hbox = new HBox(50); // 10 是 Label 和 TextField 之间的间距
        hbox.setPadding(new Insets(20)); // 设置 HBox 的内边距
        hbox.setAlignment(Pos.CENTER_LEFT); // 将子节点对齐到左侧
        hbox.getChildren().addAll(label1, textField, label2, spinner, label3);
        return hbox;
    }

    public Button getButton(String button_name){
        Button button = new Button(button_name);
        button.setStyle("-fx-font-size: 40px; -fx-font-family: 'KaiTi';");
        button.setOnAction(e -> {
            // 检查是否所有的输入都是合法的
            boolean isAllFinish = true;
             for(int i = 0; i < 11; i++){
                 isAllFinish = isAllFinish && isFinish[i];
             }
            if(isAllFinish){
                // 所有输入都是合法的
                // 将数据存入数据库
                add_change_del_task.add_task(task_name, Fact_importance, Fact_urgency);
                System.out.println("Get And finish");
            }
            else {
                // 有非法输入
                // 弹出警告框
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("警告");
                alert.setHeaderText("输入不合法");
                alert.setContentText("请检查各项输入是否合法");
                alert.showAndWait();
            }
        });
        return button;
    } 

    public void check(String textfield_data, int spinner_data, Label label3, int i){
        int FSM = 0; // 模拟有限状态机
        int text_data = 0;
        int level_Data = 0;
        if(textfield_data.isEmpty()){
            FSM = 1;
        }
        else {
            try {
                // 检查是否可以是整数
                textfield_data = textfield_data.replaceAll("\\s+", "");
                text_data = Integer.parseInt(textfield_data);
                if(text_data < 0 || text_data > 100){
                    FSM = 3;
                    // Error 超出范围
                }
                else {
                    FSM = 0;
                    // Success
                }
            } catch (NumberFormatException e) {
                FSM = 2;
                // Error 不是合法数据
            }
        }
        level_Data = map(spinner_data);

        String message1 = "提示: ";
        String message2 = "目前的有效结果是: ";
        String message3 = "错误信息: ";
        int fact_data;
        switch (FSM) {
            case 0:
                label3.setText(message1 + message2 + text_data );
                label3.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");
                fact_data = text_data;
                break;
            case 1:
                label3.setText(message1 + message2 + level_Data);
                label3.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");
                fact_data = level_Data;
                break;
            case 2:
                label3.setText(message1 + message3 + "文本框输入的不是数字");   
                label3.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
                fact_data = -1;
                break;
            case 3:
                label3.setText(message1 + message3 + "文本框输入的数字超出范围");
                label3.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
                fact_data = -1;
                break;
            default:
                label3.setText(message1 + message3 + "未定义错误");
                label3.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
                fact_data = -1;
        }

        if(fact_data == -1) {
            // 非法情况
            isFinish[i] = false;
        }
        else {
            isFinish[i] = true;
            switch (i) {
                case 9 -> Fact_importance = fact_data;
                case 10 -> Fact_urgency = fact_data;
            }
        }

        
    }

    public int map(int i){
        return switch (i) {
            case 1 -> 5;
            case 2 -> 20;
            case 3 -> 35;
            case 4 -> 50;
            case 5 -> 65;
            case 6 -> 80;
            case 7 -> 95;
            default -> 0;
        };
    }
}
