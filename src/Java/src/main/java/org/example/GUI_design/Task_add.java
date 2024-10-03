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
    private Stage stage;


    public Task_add(){

    }


    // 用于标准增加任务时候的界面
    public void openWindows_Task_add(Main_Page mainpage, int status) {
        // Open a new window to add a new task
        stage = new Stage();
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

        HBox name_hbox = getTag("任务名称 : ", null);
        vBox.getChildren().add(name_hbox);

        for(int i = 1; i < 9; i++){
            isFinish[i] = true;
        }

        HBox Fact_importance_hbox = getNum("重要程度 : ", 9);
        vBox.getChildren().add(Fact_importance_hbox);

        HBox Fact_urgency_hbox = getNum("紧急程度 : ", 10);
        vBox.getChildren().add(Fact_urgency_hbox);

        Button button = getButton("确认", mainpage, status);
        vBox.getChildren().add(button);
        stage.show();
    }


    // 在修改任务时，专用的增加界面，不同在于
    // 1. 原始参数会被传入
    // 2. 按钮的功能会有所不同：点击确认修改会优先删除原始任务，再添加新任务
    // 3. 会展示原始值
    public void openWindows_Task_add(String task_name, int Fact_importance, int Fact_urgency, int ID, int status, Main_Page main_page) {
        // Open a new window to add a new task
        stage = new Stage();
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
        Label head_title = new Label("修改任务");
        head_title.setStyle("-fx-font-size: 60px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");;
        head_title.prefWidthProperty().bind(vBox.widthProperty().multiply(0.9));
        head_title.setAlignment(Pos.CENTER); // 设置文本居中对齐
        vBox.getChildren().add(head_title);

        HBox name_hbox = getTag("任务名称 : ", task_name);
        vBox.getChildren().add(name_hbox);

        for(int i = 1; i < 9; i++){
            isFinish[i] = true;
        }

        HBox Fact_importance_hbox = getNum("重要程度 : ", 9, Fact_importance);
        vBox.getChildren().add(Fact_importance_hbox);

        HBox Fact_urgency_hbox = getNum("紧急程度 : ", 10, Fact_urgency);
        vBox.getChildren().add(Fact_urgency_hbox);

        Button button = getButton("确认修改", Fact_importance, Fact_urgency, ID, status, main_page);
        vBox.getChildren().add(button);
        stage.show();
    }


    // 根据old_name是否为null来判断是增加任务还是修改任务
    public HBox getTag(String tag_name, String old_name){
        // 创建 Label 和 TextField
        Label label = new Label(tag_name);
        label.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");;
        TextField textField = new TextField(old_name);
        textField.setPrefHeight(40); // 设置 TextField 的高度

        if(old_name != null){
            task_name = old_name;
            isFinish[0] = true;
        }

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


    // 用于增加任务时的数字输入框
    public HBox getNum(String tag_name, int index){
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
            check(newValue, spinner.getValue(), label3, index, 0);
        });

        // 监听 Spinner 值的变化
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            label2.setText("Selected number: " + newValue);
            check(textField.getText(), newValue, label3, index, 1);
        });
        // 创建 HBox 布局，并将 Label 和 TextField 添加进去
        HBox hbox = new HBox(50); // 10 是 Label 和 TextField 之间的间距
        hbox.setPadding(new Insets(20)); // 设置 HBox 的内边距
        hbox.setAlignment(Pos.CENTER_LEFT); // 将子节点对齐到左侧
        hbox.getChildren().addAll(label1, textField, label2, spinner, label3);
        return hbox;
    }


    // 用于修改任务时的数字输入框
    public HBox getNum(String tag_name, int index, int old_num){
        // 创建 Label 和 TextField
        Label label1 = new Label(tag_name);
        label1.setStyle("-fx-font-size: 30px; -fx-font-family: 'KaiTi';");;

        TextField textField = new TextField(String.valueOf(old_num));
        textField.setPrefHeight(20); // 设置 TextField 的高度

        // 创建一个 Spinner，范围从 1 到 100
        Spinner<Integer> spinner = new Spinner<>(1, 7, 1);
        spinner.setEditable(true); // 允许手动输入
        spinner.getValueFactory().setValue(4); // 设置默认值
        // 创建一个 Label 来显示 Spinner 选择的值
        Label label2 = new Label("Selected number: " + spinner.getValue());
        label2.setStyle("-fx-font-size: 20px; -fx-font-family: 'KaiTi';");




        Label label3 = new Label("原始值为： " + old_num); // 展示最终值或者报错信息
        label3.setStyle("-fx-font-size: 20px;");

        switch (index) {
            case 9 -> {
                Fact_importance = old_num;
                isFinish[9] = true;
            }
            case 10 -> {
                Fact_urgency = old_num;
                isFinish[10] = true;
            }
        }


        // 添加监听器来响应 TextField 文本的变化
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            // 如果文本不是数字，则将文本设置为旧值
            check(newValue, spinner.getValue(), label3, index, old_num, 0);
        });

        // 监听 Spinner 值的变化
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            label2.setText("Selected number: " + newValue);
            check(textField.getText(), newValue, label3, index, old_num, 1);
        });
        // 创建 HBox 布局，并将 Label 和 TextField 添加进去
        HBox hbox = new HBox(50); // 10 是 Label 和 TextField 之间的间距
        hbox.setPadding(new Insets(20)); // 设置 HBox 的内边距
        hbox.setAlignment(Pos.CENTER_LEFT); // 将子节点对齐到左侧
        hbox.getChildren().addAll(label1, textField, label2, spinner, label3);
        return hbox;
    }


    // 用于增加任务时的确认按钮
    public Button getButton(String button_name, Main_Page mainpage, int status){
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
                add_change_del_task.add_task(task_name, Fact_importance, Fact_urgency,-1); // 默认ID为-1，使得自行查询目前最大的ID
                stage.close();
                mainpage.refresh_scene(status);
                System.out.println("增加成功，task_Add 273");
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


    // 用于修改任务时的确认按钮
    public Button getButton(String button_name, int old_importance, int old_urgency, int ID, int status, Main_Page main_page){
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
                // 先删除
                int result = add_change_del_task.delete_task(task_name, old_importance, old_urgency);
                if(result != 0){
                    // 删除失败
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("警告");
                    alert.setHeaderText("删除失败");
                    alert.setContentText((result == 1) ? "请检查数据文件是否存在或损坏" : "匹配失败");
                    alert.showAndWait();
                    return;
                }

                add_change_del_task.add_task(task_name, Fact_importance, Fact_urgency, ID);
                main_page.refresh_scene(status);
                stage.close();
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



    // 用于增加任务时，检查输入是否合法，并且在label3中展示结果
    // 优先展示被修改的数据
    public void check(String textfield_data, int spinner_data, Label label3, int i, int probe){
        int FSM = 0; // 模拟有限状态机
        int text_data = 0;
        int level_Data = 0;
        if(textfield_data.isEmpty() || probe == 1){
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


    // 用于修改任务时，检查输入是否合法，并且在label3中展示结果
    public void check(String textfield_data, int spinner_data, Label label3, int i, int old_num, int probe){
        int FSM = 0; // 模拟有限状态机
        int text_data = 0;
        int level_Data = 0;
        if(textfield_data.isEmpty() || probe == 1){
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
        String message2 = "--> NEW: ";
        String message3 = "错误信息: ";
        String message4 = "OLD: ";
        int fact_data;
        switch (FSM) {
            case 0:
                label3.setText(message1 + message4 + old_num + message2 + text_data );
                label3.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");
                fact_data = text_data;
                break;
            case 1:
                label3.setText(message1 + message4 + old_num + message2 + level_Data);
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


    // 用于将spinner的值映射到1-100，表示等级到百分制的映射
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
