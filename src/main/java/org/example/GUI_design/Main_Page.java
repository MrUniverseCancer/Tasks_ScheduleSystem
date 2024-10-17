package org.example.GUI_design;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.example.GUI_design.planPage.plan_page;

public class Main_Page {

    private Scene scene;
    private Main main;
    private Pane MainPage;
    private Pane TaskPage;
    private Pane PlanPage;
    private SetUp_Page setUp_page;

    public Main_Page(Main main) {
        this.main = main;
        this.setUp_page = new SetUp_Page();
        createScene();
    }

    public void createScene() {
        MainPage = getMainPage();
        TaskPage = getTaskPage();
        PlanPage = getPlanPage();
        this.scene = new Scene(MainPage);
        // 设置快捷键处理器
        setupKeyBindings(scene);
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
        // 设置快捷键处理器
        setupKeyBindings(scene);
        main.refreshScene(this.scene);
    }




    public void setupKeyBindings(Scene scene) {
        // 创建快捷键组合
        KeyCombination ctrlX = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
        KeyCombination ctrlY = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);

        // 为快捷键组合添加事件处理器
        scene.getAccelerators().put(ctrlX, () -> {
            System.out.println("Ctrl + X was pressed");
            setUp_page.openWindows_Set_UP(this, 1);
            // 在这里添加 Ctrl + X 快捷键触发时的逻辑
        });

        scene.getAccelerators().put(ctrlY, () -> {
            System.out.println("Ctrl + Y was pressed");
            // 在这里添加 Ctrl + Y 快捷键触发时的逻辑
        });
    }




    public Pane getMainPage(){
        // Task Page About
        BorderPane root = new BorderPane();
        root.setPrefSize(getLength(), getWidth());
        // 设置主页面的菜单
        Menu_create menu_create = new Menu_create(main);
        root.setTop(menu_create.getMainMenuBar());

        // 设置主页面展示的任务
        task_showing task_showing = new task_showing(main, this);
        root.setCenter(task_showing.getGridPane());

        Button temp = new Button("Temp");
        root.setBottom(temp);
        temp.setOnAction(e -> {
            Task_add task_add = new Task_add();
            task_add.openWindows_Task_add(this, 1);
        });

        return root;
    }

    public Pane getTaskPage(){
        // Main Page About
        BorderPane root = new BorderPane();
        root.setPrefSize(getLength(), getWidth());
        // 设置任务页面的菜单
        Menu_create menu_create = new Menu_create(main);
        root.setTop(menu_create.getTaskMenuBar());

        // 设置任务页面展示的任务
        Task_Page task_page = new Task_Page(main, this);
        root.setCenter(task_page.getTaskPage());
        return root;
    }

    public Pane getPlanPage(){
        // Plan Page About
        BorderPane root = new BorderPane();
        root.setPrefSize(getLength(), getWidth());
        // 设置计划页面的菜单
        Menu_create menu_create = new Menu_create(main);
        root.setTop(menu_create.getPlanMenuBar());

        // 设置计划页面展示的任务
        plan_page plan_page = new plan_page(main, this);
        root.setCenter(plan_page.getPlanPage());
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

    public void from_Main_to_Plan(){
        // 从主页面切换到计划页面
        this.scene.setRoot(PlanPage);
    }

    public void from_Plan_to_Main(){
        // 从计划页面切换到主页面
        this.scene.setRoot(MainPage);
    }

    public void from_Task_to_Plan(){
        // 从任务页面切换到计划页面
        this.scene.setRoot(PlanPage);
    }

    public void from_Plan_to_Task(){
        // 从计划页面切换到任务页面
        this.scene.setRoot(TaskPage);
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
