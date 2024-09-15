package org.example.GUI_design;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SetUp_Page {
    // 设置页面
    private Stage stage;

    public SetUp_Page() {
    }

    // 用于标准设置页面时候的界面
    public void openWindows_Set_UP(Main_Page mainpage, int status) {
        // 打开页面专用于设置
        // 默认设置完成后进入主页面，但是保留status状态参数
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Set the window to be modal

        Scene scene = new Scene(new Pane(), 1500, 800);
        stage.setScene(scene);

        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #e0feff;");
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(100));
//        vBox.setAlignment(Pos.CENTER);
        vBox.prefWidthProperty().bind(scrollPane.widthProperty());
        vBox.prefHeightProperty().bind(scrollPane.heightProperty());
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBox);
        scene.setRoot(scrollPane);

        // 头部的楷体标题
        Label head_title = new Label("设置");
        head_title.setStyle("-fx-font-size: 80px; -fx-font-weight: bold; -fx-font-family: 'KaiTi';");;
        head_title.prefWidthProperty().bind(vBox.widthProperty().multiply(0.9));
        head_title.setAlignment(Pos.CENTER); // 设置文本居中对齐
        vBox.getChildren().add(head_title);

        stage.show();
    }
}
