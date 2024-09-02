package org.example.GUI_design;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.*;

public class task_showing {

    private GridPane gridPane;
    private Main main;
    private Main_Page main_page;
    private DoubleBinding widthProperty;
    private DoubleBinding heightProperty;
    private double columnratio = 9.0;
    private double rowratio = 9.0;

    public task_showing(Main main, Main_Page main_page){
        this.main = main;
        this.main_page = main_page;

        createGridPane();
    }

    public void createGridPane(){
        gridPane = new GridPane();
        gridPane.setHgap(0); // Horizontal gap between columns
        gridPane.setVgap(0); // Vertical gap between rows

        // 设置列约束
        double fact_col = 100.0 / columnratio;
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(fact_col); // 占据33.33%的宽度
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(fact_col * (columnratio-1)/2);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(fact_col * (columnratio-1)/2);
        gridPane.getColumnConstraints().addAll(col1, col2, col3);

        // 设置行约束
        double fact_row = 100.0 / rowratio;
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(fact_row); // 占据33.33%的高度
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(fact_row * (rowratio-1)/2);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(fact_row * (rowratio-1)/2);
        gridPane.getRowConstraints().addAll(row1, row2, row3);

        // 创建GridPane的每一个单元格
        gridPane.add(pane_1_1(), 0, 0);
        gridPane.add(pane_1_2(), 1, 0);
        gridPane.add(pane_1_3(), 2, 0);
        gridPane.add(pane_2_1(), 0, 1);
        gridPane.add(pane_2_2(), 1, 1);
        gridPane.add(pane_2_3(), 2, 1);
        gridPane.add(pane_3_1(), 0, 2);
        gridPane.add(pane_3_2(), 1, 2);
        gridPane.add(pane_3_3(), 2, 2);
    }

    Pane pane_1_1(){
        // 转换标识
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // 添加图片
        Image image = new Image("file:src/main/resources/images/箭头2.jpg");
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(gridPane.widthProperty().divide(columnratio)); // 设置图片宽度
        imageView.fitHeightProperty().bind(gridPane.heightProperty().divide(rowratio)); // 设置图片高度
        pane.getChildren().add(imageView);


        return pane;
    }

    Pane pane_1_2(){
        // 重要性标签1
        Pane pane = new StackPane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // 创建一个"重要"的Label
        Label label = new Label("重要");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        pane.getChildren().add(label);

        return pane;
    }

    Pane pane_1_3(){
        // 重要性标签2
        Pane pane = new StackPane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // 创建一个"不重要"的Label
        Label label = new Label("不重要");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        pane.getChildren().add(label);

        return pane;
    }

    Pane pane_2_1(){
        // 紧迫性标签1
        Pane pane = new StackPane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // 创建一个"紧迫"的Label
        Label label = new Label("紧迫");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        pane.getChildren().add(label);
        return pane;
    }

    Pane pane_3_1(){
        // 紧迫性标签2
        Pane pane = new StackPane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // 创建一个"不紧迫"的Label
        Label label = new Label("不紧迫");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        pane.getChildren().add(label);

        return pane;
    }

    Pane pane_2_2(){
        // 重要*紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // Optionally, add a button or other controls to each pane
        Button button = new Button("紧迫且重要");
        pane.getChildren().add(button);

        return pane;
    }
    Pane pane_2_3(){
        // 不重要*紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // Optionally, add a button or other controls to each pane
        Button button = new Button("不紧迫且重要");
        pane.getChildren().add(button);

        return pane;
    }

    Pane pane_3_2(){
        // 重要*不紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // Optionally, add a button or other controls to each pane
        Button button = new Button("紧迫且不重要");
        pane.getChildren().add(button);

        return pane;
    }

    Pane pane_3_3(){
        // 不重要*不紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // Optionally, add a button or other controls to each pane
        Button button = new Button("不紧迫且不重要");
        pane.getChildren().add(button);

        return pane;
    }


    public GridPane getGridPane(){
        return this.gridPane;
    }
}
