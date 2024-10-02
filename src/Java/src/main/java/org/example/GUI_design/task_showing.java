package org.example.GUI_design;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import org.example.GUI_design.generalData.Conditional_Compilation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class task_showing {

    private GridPane gridPane;
    private Main main;
    private Main_Page main_page;
    private four_element four_element;
    private double columnratio = 9.0;
    private double rowratio = 9.0;
    private List<String []> tasks1;
    private List<String []> tasks2;
    private List<String []> tasks3;
    private List<String []> tasks4;
    private Random rand = new Random();
    private double dis = 8;
    private Pane pane22_1;
    private Pane pane23_1;
    private Pane pane32_1;
    private Pane pane33_1;
    private Pane pane22_2;
    private Pane pane23_2;
    private Pane pane32_2;
    private Pane pane33_2;

    public task_showing(Main main, Main_Page main_page){
        this.main = main;
        this.main_page = main_page;
        this.four_element = new four_element();
        tasks1 = new ArrayList<String[]>();
        tasks2 = new ArrayList<String[]>();
        tasks3 = new ArrayList<String[]>();
        tasks4 = new ArrayList<String[]>();
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
        gridPane.add(pane_3_1(), 0, 2);
        pane22_1 = pane_2_2(0);
        pane23_1 = pane_2_3(0);
        pane32_1 = pane_3_2(0);
        pane33_1 = pane_3_3(0);

        pane22_2 = pane_2_2(1);
        pane23_2 = pane_2_3(1);
        pane32_2 = pane_3_2(1);
        pane33_2 = pane_3_3(1);

        pane22_2.setVisible(false);
        pane23_2.setVisible(false);
        pane32_2.setVisible(false);
        pane33_2.setVisible(false);
        
        gridPane.add(pane22_1, 1, 1);
        gridPane.add(pane23_1, 2, 1);
        gridPane.add(pane32_1, 1, 2); // 验证发现，第一个参数是列，第二个参数是行
        gridPane.add(pane33_1, 2, 2);
        gridPane.add(pane22_2, 1, 1);
        gridPane.add(pane23_2, 2, 1);
        gridPane.add(pane32_2, 1, 2);
        gridPane.add(pane33_2, 2, 2);

    }

    Pane pane_1_1(){
        // 转换标识
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        // 添加图片
        String path = (Conditional_Compilation.Is_Building) ? "images/箭头2.jpg" : "file:src/main/resources/images/箭头2.jpg";

        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(gridPane.widthProperty().divide(columnratio)); // 设置图片宽度
        imageView.fitHeightProperty().bind(gridPane.heightProperty().divide(rowratio)); // 设置图片高度


        pane.getChildren().add(imageView);

        // 创建缩放和旋转转换
        Scale scale = new Scale(1.0, 1.0, imageView.getFitWidth() / 2, imageView.getFitHeight() / 2);
        Rotate rotate = new Rotate(0, imageView.getFitWidth() / 2, imageView.getFitHeight() / 2);

        // 应用转换到 imageView
        imageView.getTransforms().addAll(scale, rotate);

        // 创建 Timeline 用于放大和旋转
        Timeline timelineEnter = new Timeline(
                new KeyFrame(Duration.ZERO,
                        e -> {
                            imageView.setRotate(180);
                        }),
                new KeyFrame(Duration.millis(300))
        );

        Timeline timelineExit = new Timeline(
                new KeyFrame(Duration.ZERO,
                        e -> {
                            imageView.setRotate(0);
                        }),
                new KeyFrame(Duration.millis(300))
        );

        // 设置鼠标进入事件
        imageView.setOnMouseEntered(event -> {
            timelineEnter.playFromStart();
            System.out.println("Entered");
        });

        // 设置鼠标离开事件
        imageView.setOnMouseExited(event -> {
            timelineExit.playFromStart();
            System.out.println("Exited");
        });

        imageView.setOnMouseClicked(event -> {
                System.out.println("Clicked");
                if(pane22_1.isVisible()){
                    pane22_1.setVisible(false);
                    pane23_1.setVisible(false);
                    pane32_1.setVisible(false);
                    pane33_1.setVisible(false);
                    pane22_2.setVisible(true);
                    pane23_2.setVisible(true);
                    pane32_2.setVisible(true);
                    pane33_2.setVisible(true);
                }
                else {
                    pane22_1.setVisible(true);
                    pane23_1.setVisible(true);
                    pane32_1.setVisible(true);
                    pane33_1.setVisible(true);
                    pane22_2.setVisible(false);
                    pane23_2.setVisible(false);
                    pane32_2.setVisible(false);
                    pane33_2.setVisible(false);
                }
            }
        );


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

    Pane pane_2_2(int i){
        // 重要*紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        getTasks(2);
        List<String[]> tasks = tasks2;
        switch (i){
            case 0:
                add_element(pane, tasks, 2, 2);
                break;
            case 1:
                add_card(pane, tasks);
                break;
        }
        return pane;
    }
    Pane pane_2_3(int i){
        // 不重要*紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        getTasks(1);
        List<String[]> tasks = tasks1;
        switch (i){
            case 0:
                add_element(pane, tasks, 1,2);
                break;
            case 1:
                add_card(pane, tasks);
                break;
        }
        return pane;
    }

    Pane pane_3_2(int i){
        // 重要*不紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        getTasks(3);
        List<String[]> tasks = tasks3;
        switch (i){
            case 0:
                add_element(pane, tasks, 2,1);
                break;
            case 1:
                add_card(pane, tasks);
                break;
        }
        return pane;
    }

    Pane pane_3_3(int i){
        // 不重要*不紧迫
        Pane pane = new Pane();

        pane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;"); // Styling for visibility

        getTasks(4);
        List<String[]> tasks = tasks4;
        switch (i){
            case 0:
                add_element(pane, tasks, 1,1);
                break;
            case 1:
                add_card(pane, tasks);
                break;
        }
        return pane;
    }

    public List<String []> getTasks(){
        return this.main.getReadTasksFromCsv().readtasks();
    }

    public void getTasks(int i){
        // 笛卡尔象限2134代表参数i的2134的排列d
        List<String []> all = getTasks();
        List<String []> result = new ArrayList<String []>();

        int length = all.size();
        for(int j = 0; j < length; j++){
            float fact_importtance = 0.0f;
            float fact_urgency     = 0.0f;
            try {
                // 将字符串转换为 float
                fact_importtance = Float.parseFloat(all.get(j)[9]);
                fact_urgency     = Float.parseFloat(all.get(j)[10]);
            } catch (NumberFormatException e) {
                // 捕捉转换失败的异常
                System.out.println("无法将字符串转换为浮点数: " + e.getMessage());
            }
            switch (i) {
                case 1:
                    if (fact_importtance < 50.0 && fact_urgency >= 50.0) {
                        result.add(all.get(j));
                    }
                    break;
                case 2:
                    if (fact_importtance >= 50.0 && fact_urgency >= 50.0) {
                        result.add(all.get(j));
                    }
                    break;
                case 3:
                    if (fact_importtance >= 50.0 && fact_urgency < 50.0) {
                        result.add(all.get(j));
                        // System.out.println("yes");
                    }
                    break;
                case 4:
                    if (fact_importtance < 50.0 && fact_urgency < 50.0) {
                        result.add(all.get(j));
                    }
                    break;
            }
        }
        switch (i){
            case 1:
                tasks1 = result;
                break;
            case 2:
                tasks2 = result;
                break;
            case 3:
                tasks3 = result;
                break;
            case 4:
                tasks4 = result;
                break;
        }
    }

    public Pane getelement(int i, float x, float y){
        Pane fig = four_element.create_fig(i, x, y);
        // importtance & urgency 强调在create_fig函数中


        // 创建 Timeline 用于放大和旋转
        double ratio = 1.2;
        Timeline timelineEnter = new Timeline(
                new KeyFrame(Duration.ZERO,
                        e -> {
                            fig.setScaleX(ratio);
                            fig.setScaleY(ratio);
//                            fig.setRotate(180);
                        }),
                new KeyFrame(Duration.millis(300))
        );

        Timeline timelineExit = new Timeline(
                new KeyFrame(Duration.ZERO,
                        e -> {
                            fig.setScaleX(1.0);
                            fig.setScaleY(1.0);
//                            fig.setRotate(0);
                        }),
                new KeyFrame(Duration.millis(300))
        );

        // 设置鼠标进入事件
        fig.setOnMouseEntered(event -> {
            timelineEnter.playFromStart();
            System.out.println("Entered");
        });

        // 设置鼠标离开事件
        fig.setOnMouseExited(event -> {
            timelineExit.playFromStart();
            System.out.println("Exited");
        });
        return fig;
    }

    public int optimize(double ratio){
        //边界支持
        if (ratio < 1/50.0){
            // 离上面/左面太近，减的少一点
            return -1;
        }
        if (ratio > 49/50.0){
            // 离下面/右面太近，减的多一点
            return 1;
        }
        return 0;
    }

    public void add_element(Pane pane, List<String[]> tasks, int num_x, int num_y){
        for (String[] temp_task : tasks){
            task_card card = new task_card(temp_task);

            float x = Float.parseFloat(temp_task[9]);
            float y = Float.parseFloat(temp_task[10]);
            double ratio_x = (50*num_x - x) / 50.0;
            double ratio_y = (50*num_y - y) / 50.0;

            // 边界支持
            int optimize_x = 0;
            int optimize_y = 0;
            optimize_x  = optimize(ratio_x);
            optimize_y = optimize(ratio_y);
            Pane temp_pane = getelement(rand.nextInt(3) + 2, x, y);
            temp_pane.layoutXProperty().bind(pane.widthProperty().multiply(ratio_x).subtract(15+optimize_x*dis));
            temp_pane.layoutYProperty().bind(pane.heightProperty().multiply(ratio_y).subtract(15+optimize_y*dis));
            AtomicBoolean i = new AtomicBoolean(false);
            Pane temp_card = card.getCard1(0, 0, main_page);
            temp_card.layoutXProperty().bind(pane.widthProperty().multiply(ratio_x).add((ratio_x > 0.5 ? -180 : 15)));
            temp_pane.setOnMouseClicked(event -> {
                // 第一次点击展示任务卡片
                // 第二次点击取消任务卡片
                System.out.println("Clicked");
                if(i.get()){
                    // 删除任务卡片
                    pane.getChildren().remove(temp_card);
                    i.set(false);
                }
                else {
                    // 添加任务卡片
                    pane.getChildren().add(temp_card);
                    i.set(true);
                }
            });
            // Find the first Button and add event handler
            Button exit_Button = card.getExit_button();
            exit_Button.setOnAction(event -> {
                // 删除任务卡片
                pane.getChildren().remove(temp_card);
                i.set(false);
            });

            pane.getChildren().add(temp_pane);
        }
        return;
    }

    public void add_card(Pane pane, List<String[]> tasks) {
        // 创建一个 VBox 用于垂直排列按钮
        VBox vbox = new VBox(10); // 10 是按钮之间的间距

        // 创建并添加多个按钮到 VBox
        for(String[] temp_task : tasks){
            task_card card = new task_card(temp_task);
            Button fact_card = card.getCard2();
            vbox.getChildren().add(fact_card);

            Pane temp_card = card.getCard1(0, 0, main_page);
            temp_card.layoutXProperty().bind(pane.widthProperty().multiply(0.6));
            AtomicBoolean i = new AtomicBoolean(false);
            fact_card.setOnMouseClicked(event -> {
                // 第一次点击展示任务卡片
                // 第二次点击取消任务卡片
                System.out.println("Clicked");
                if(i.get()){
                    // 删除任务卡片
                    pane.getChildren().remove(temp_card);
                    i.set(false);
                }
                else {
                    // 添加任务卡片
                    pane.getChildren().add(temp_card);
                    i.set(true);
                }
            });
            // Find the first Button and add event handler
            Button exit_Button = card.getExit_button();
            exit_Button.setOnAction(event -> {
                // 删除任务卡片
                pane.getChildren().remove(temp_card);
                i.set(false);
            });
        }

        // 创建一个 ScrollPane，并将 vbox 添加到 ScrollPane 中
        ScrollPane scrollPane = new ScrollPane(vbox);

        StackPane stackPane1 = new StackPane();
        stackPane1.getChildren().add(scrollPane);
        stackPane1.prefHeightProperty().bind(pane.heightProperty());
        stackPane1.prefWidthProperty().bind(pane.widthProperty().multiply(0.6));
        stackPane1.layoutXProperty().bind(pane.widthProperty().multiply(0.2));

        pane.getChildren().add(stackPane1);
        return;
    }


    public GridPane getGridPane(){
        return this.gridPane;
    }
}
