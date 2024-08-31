package GUI_design;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage)  {

        Main_Page main_page = new Main_Page();
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
}
