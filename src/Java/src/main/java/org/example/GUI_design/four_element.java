package org.example.GUI_design;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;

public class four_element {
    // 用于创建三种天体的图案
    private double size = 30.0;

    public Pane create_fig(int i){
        return switch (i) {
            case 1 -> createCircle();
            case 2 -> createSun();
            case 3 -> createMoon();
            case 4 -> createStar();
            default -> null;
        };
    }

    private Pane createCircle() {
        Circle circle = new Circle();
        circle.setFill(Color.YELLOW);
        circle.setRadius(size/2);
        return new Pane(circle);
    }

//    private Pane createSun() {
//        Pane sunPane = new Pane();
//        Circle circle = new Circle();
//        circle.setFill(Color.YELLOW);
//        sunPane.getChildren().add(circle);
//
//        for (int i = 0; i < 12; i++) {
//            Line line = new Line(50, 0, 70, 0);
//            line.setStrokeWidth(3);
//            line.setTranslateX(50);
//            line.setRotate(i * 30);
//            line.setStroke(Color.ORANGE);
//            sunPane.getChildren().add(line);
//        }
//
//
//        return new Pane(sunPane);
//    }


    private Pane createSun(){
        Pane sunPane = new Pane();
        Image sun = new Image("file:src/main/resources/images/太阳.jpg");
        ImageView sunview = new ImageView(sun);
        sunview.setFitHeight(size);
        sunview.setFitWidth(size);
        sunPane.getChildren().add(sunview);
        return sunPane;
    }
    private Pane createMoon() {
        Pane moonPane = new Pane();
        Image moon = new Image("file:src/main/resources/images/月亮.jpg");
        ImageView moonview = new ImageView(moon);
        moonview.setFitHeight(size);
        moonview.setFitWidth(size);
        moonPane.getChildren().add(moonview);
        return moonPane;
    }

    private Pane createStar() {
        Pane starPane = new Pane();
        Image star = new Image("file:src/main/resources/images/星星.jpg");
        ImageView starview = new ImageView(star);
        starview.setFitHeight(size);
        starview.setFitWidth(size);
        starPane.getChildren().add(starview);
        return starPane;
    }

}
