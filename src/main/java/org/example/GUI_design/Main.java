package org.example.GUI_design;

import org.example.GUI_design.generalData.Conditional_Compilation;
import org.example.GUI_design.generalData.Length_And_Width;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.server.readTasksFromCsv;

public class Main extends Application {
    private Length_And_Width length_and_width;
    private Main_Page main_page;
    private readTasksFromCsv readTasks_from_csv;
    private Scene scene;
    private Stage stage;

    public Main(){
        // 初始化界面长宽高
        this.length_and_width = new Length_And_Width();
        this.readTasks_from_csv = new readTasksFromCsv();
    }

    @Override
    public void start(Stage stage)  {
        this.stage = stage;
        this.main_page = new Main_Page(this);
        scene = main_page.getScene();

        // 设置总标题栏的文字和图像

        String image = (Conditional_Compilation.Is_Building) ? "images/标题图片2.jpg" : "file:src/main/resources/images/标题图片2.jpg";
        stage.setTitle("TaskScheduler");
        stage.getIcons().add(new Image(image));
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

    public readTasksFromCsv getReadTasksFromCsv(){
        return this.readTasks_from_csv;
    }

    public void refreshScene(Scene scene){
        this.scene = scene;
        stage.setScene(scene);
    }
}
