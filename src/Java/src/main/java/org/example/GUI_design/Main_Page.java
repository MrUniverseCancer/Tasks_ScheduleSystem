package org.example.GUI_design;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Main_Page {

    private Scene scene;
    private Main main;
    private Pane MainPage;
    private Pane TaskPage;

    public Main_Page(Main main) {
        this.main = main;
        createScene();
    }

    public void createScene() {
        MainPage = getMainPage();
        TaskPage = getTaskPage();
        this.scene = new Scene(MainPage);
    }


    // 在数据库有所更新后，重新加载任务页面，需要原始状态信息
    public void refresh_scene(int status){
        MainPage = getMainPage();
        TaskPage = getTaskPage();
        switch (status){
            case 0:
                this.scene = new Scene(MainPage);
                break;
            case 1:
                this.scene = new Scene(TaskPage);
                break;
        }
        main.refreshScene(this.scene);
    }

    public Pane getMainPage(){
        // Task Page About
        BorderPane root = new BorderPane();
        root.setPrefSize(getLength(), getWidth());
        // 设置任务页面的菜单
        Menu_create menu_create = new Menu_create(main);
        root.setTop(menu_create.getMainMenuBar());

        // 设置主页面展示的任务
        task_showing task_showing = new task_showing(main, this);
        root.setCenter(task_showing.getGridPane());

        Button temp = new Button("Temp");
        root.setBottom(temp);
        temp.setOnAction(e -> {
            Task_add task_add = new Task_add();
            task_add.openWindows_Task_add();
        });

        return root;
    }

    public Pane getTaskPage(){
        // Main Page About
        BorderPane root = new BorderPane();
        root.setPrefSize(getLength(), getWidth());
        // 设置主页面的菜单
        Menu_create menu_create = new Menu_create(main);
        root.setTop(menu_create.getTaskMenuBar());

        // 设置主页面展示的任务
//        task_showing task_showing = new task_showing(main, this);
//        root.setCenter(task_showing.getGridPane());
        Task_Page task_page = new Task_Page(main, this);
        root.setCenter(task_page.getTaskPage());
        return root;
    }

    public void from_Main_to_Task(){
        // 从主页面切换到任务页面
        this.scene.setRoot(TaskPage);
    }

    public void from_Task_to_Main(){
        // 从任务页面切换到主页面
        this.scene.setRoot(MainPage);
    }

    public ReadOnlyDoubleProperty widthProperty() {
        return this.scene.widthProperty();
    }
    public ReadOnlyDoubleProperty heightProperty() {
        return this.scene.heightProperty();
    }

    public double getLength() {
        return this.main.getLengthAndWidth().getLength();
    }
    public double getWidth() {
        return this.main.getLengthAndWidth().getWidth();
    }

    public Scene getScene() {
        return this.scene;
    }
}
