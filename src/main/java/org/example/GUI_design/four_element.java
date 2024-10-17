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
import org.example.GUI_design.generalData.Conditional_Compilation;

public class four_element {
    // 用于创建三种天体的图案
    private double size = 30.0;
    private int inportance_edge = 60;

    private int urgency_edge = 50;


    public Pane create_fig(int i, float x, float y) {
        i = (x > inportance_edge) ? 1 : i; // 用于判断是否是由于重要程度高于60，使得展现圆形红色提高对比度
        // 计算大小，高于50，逐渐线性变大
        size = 30.0;
        double expand_ratio = (y > urgency_edge) ? (y - urgency_edge)/(100 - urgency_edge)*1: 0;
        expand_ratio = expand_ratio + 1;
        size = size * expand_ratio;
        return switch (i) {
            case 1 -> createCircle(x);
            case 2 -> createSun();
            case 3 -> createMoon();
            case 4 -> createStar();
            default -> null;
        };
    }

    private Pane createCircle(float x) {
        Circle circle = new Circle();
        // 选取颜色程度
        float color = (x - inportance_edge) / (100 - inportance_edge);
        // 根据计算的比例生成颜色
        Color fillColor = Color.RED.deriveColor(45*(1-color), 1, 1 - color, 1); // 调整颜色的属性
        circle.setFill(fillColor);
        circle.setRadius(size/2);
        circle.setLayoutX(size/2);
        circle.setLayoutY(size/2);
        return new Pane(circle);
    }


    private Pane createSun(){
        Pane sunPane = new Pane();
        String image = (Conditional_Compilation.Is_Building) ? "images/太阳.jpg" : "file:src/main/resources/images/太阳.jpg";
        Image sun = new Image(image);
        ImageView sunview = new ImageView(sun);
        sunview.setFitHeight(size);
        sunview.setFitWidth(size);


        Circle circle = new Circle(size/2.0 - 3); // Radius of the circle
        circle.setStroke(null);
        circle.setLayoutX(size/2.0);
        circle.setLayoutY(size/2.0);
        // Set the ImageView to be clipped by the Circle
        sunview.setClip(circle);
        
        // Add the ImageView to the Pane
        sunPane.getChildren().add(sunview);        
        return sunPane;
    }
    private Pane createMoon() {
        Pane moonPane = new Pane();
        String image = (Conditional_Compilation.Is_Building) ? "images/月亮2.jpg" : "file:src/main/resources/images/月亮2.jpg";
        Image moon = new Image(image);
        ImageView moonview = new ImageView(moon);
        moonview.setFitHeight(size);
        moonview.setFitWidth(size);
        Circle circle = new Circle(size/2.0 - 3); // Radius of the circle
        circle.setStroke(null);
        circle.setLayoutX(size/2.0);
        circle.setLayoutY(size/2.0);
        // Set the ImageView to be clipped by the Circle
        moonview.setClip(circle);

        // Add the ImageView to the Pane
        moonPane.getChildren().add(moonview);
        return moonPane;
    }

    private Pane createStar() {
        Pane starPane = new Pane();
        String image = (Conditional_Compilation.Is_Building) ? "images/黑洞.jpg" : "file:src/main/resources/images/黑洞.jpg";
        Image star = new Image(image);
        ImageView starview = new ImageView(star);
        starview.setFitHeight(size);
        starview.setFitWidth(size);
        Circle circle = new Circle(size/2.0 - 3); // Radius of the circle
        circle.setStroke(null);
        circle.setLayoutX(size/2.0);
        circle.setLayoutY(size/2.0);
        // Set the ImageView to be clipped by the Circle
        starview.setClip(circle);

        // Add the ImageView to the Pane
        starPane.getChildren().add(starview);
        return starPane;
    }

}
