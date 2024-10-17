package org.example.GUI_design;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.server.Task;
import org.example.server.readTasksFromCsv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Task_Page {
    private Main main;
    private Main_Page main_page;
    private Pane TaskPage;
    private List<Task> tasks = new ArrayList<>();


    public Task_Page(Main main, Main_Page main_page) {
        this.main = main;
        this.main_page = main_page;
        createTaskPage();
    }

    public void createTaskPage(){
        TaskPage = new Pane();

        ScrollPane scrollPane = new ScrollPane();
        Pane middPane = new Pane();
        VBox vBox = new VBox();

        readTasksFromCsv readTasks_from_csv = main.getReadTasksFromCsv();
        tasks = readTasks_from_csv.readtasks();
        for(Task task: tasks){
            task_card task_card = new task_card(task);
            Pane task_card_pane = task_card.getTaskCard();
            vBox.getChildren().add(task_card_pane);
            AtomicBoolean i = new AtomicBoolean(false);
            Pane temp_card = task_card.getCard1(1, 1, main_page);
//            temp_card.layoutXProperty().bind(Bindings.add(task_card_pane.layoutXProperty(),task_card_pane.widthProperty().multiply(1.2)));
            temp_card.layoutXProperty().bind(scrollPane.widthProperty().multiply(0.5));
            temp_card.layoutYProperty().bind(task_card_pane.layoutYProperty());
            temp_card.prefWidthProperty().bind(scrollPane.widthProperty().multiply(0.4));
            temp_card.prefHeightProperty().bind(scrollPane.heightProperty());
            task_card_pane.setOnMouseClicked(event -> {
                // 第一次点击展示任务卡片
                // 第二次点击取消任务卡片
                System.out.println("Clicked");
                if(i.get()){
                    // 删除任务卡片
                    middPane.getChildren().remove(temp_card);
                    i.set(false);
                }
                else {
                    // 添加任务卡片
                    middPane.getChildren().add(temp_card);
                    i.set(true);
                }
            });
            Button exit_Button = task_card.getExit_button();
            exit_Button.setOnAction(event -> {
                //                        System.out.println("First button clicked!");
                // 删除任务卡片
                middPane.getChildren().remove(temp_card);
                i.set(false);
            });
            // 创建 Timeline 用于放大和旋转
            double ratio = 1.2;
            Timeline timelineEnter = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            e -> {
                                task_card_pane.setScaleX(ratio);
                                task_card_pane.setScaleY(ratio);
//                            fig.setRotate(180);
                            }),
                    new KeyFrame(Duration.millis(300))
            );

            Timeline timelineExit = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            e -> {
                                task_card_pane.setScaleX(1.0);
                                task_card_pane.setScaleY(1.0);
//                            fig.setRotate(0);
                            }),
                    new KeyFrame(Duration.millis(300))
            );

            // 设置鼠标进入事件
            task_card_pane.setOnMouseEntered(event -> {
                timelineEnter.playFromStart();
            });

            // 设置鼠标离开事件
            task_card_pane.setOnMouseExited(event -> {
                timelineExit.playFromStart();
            });
        }
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        middPane.getChildren().add(vBox);
        scrollPane.setContent(middPane);
        scrollPane.layoutXProperty().bind(TaskPage.widthProperty().multiply(0.15));
        scrollPane.layoutYProperty().bind(TaskPage.heightProperty().multiply(0.1));
        scrollPane.prefHeightProperty().bind(TaskPage.heightProperty().multiply(0.9));
        scrollPane.prefWidthProperty().bind(TaskPage.widthProperty().multiply(0.8));
        TaskPage.getChildren().add(scrollPane);
    }

    public Pane getTaskPage(){
        return this.TaskPage;
    }
}
