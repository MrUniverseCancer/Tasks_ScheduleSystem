package org.example.GUI_design;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Main_Page {

    private Scene scene;
    private Main main;
    private Pane root;

    public Main_Page(Main main) {
        this.main = main;
        createScene();
    }

    public void createScene() {

        root = getRoot();
        this.scene = new Scene(root);

    }

    public Pane getRoot(){
        // Main Page About
        BorderPane root = new BorderPane();
        root.setPrefSize(getLength(), getWidth());
        // 设置主页面的菜单
        Menu_MainPage menu_mainPage = new Menu_MainPage(main);
        root.setTop(menu_mainPage.getMenuBar());

        // 设置主页面展示的任务
        task_showing task_showing = new task_showing(main, this);
        root.setCenter(task_showing.getGridPane());
        return root;
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
