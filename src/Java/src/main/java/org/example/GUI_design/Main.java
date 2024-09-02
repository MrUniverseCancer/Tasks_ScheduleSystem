package org.example.GUI_design;

import org.example.GUI_design.generalData.Length_And_Width;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.server.readTasks_FROM_csv;

public class Main extends Application {
    private Length_And_Width length_and_width;
    private Main_Page main_page;
    private readTasks_FROM_csv readTasks_from_csv;

    public Main(){
        // 初始化界面长宽高
        this.length_and_width = new Length_And_Width();
        this.readTasks_from_csv = new readTasks_FROM_csv();
    }

    @Override
    public void start(Stage stage)  {

        this.main_page = new Main_Page(this);
        Scene scene = main_page.getScene();

        // 设置总标题栏的文字和图像
        stage.setTitle("TaskScheduler");
        stage.getIcons().add(new Image("file:src/main/resources/images/标题图片2.jpg"));
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

    public Length_And_Width getLengthAndWidth(){
        return this.length_and_width;
    }

    public Main_Page getMainPage(){
        return this.main_page;
    }

    public readTasks_FROM_csv getReadTasksFromCsv(){
        return this.readTasks_from_csv;
    }
}
