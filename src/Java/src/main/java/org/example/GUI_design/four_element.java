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


    private Pane createSun(){
        Pane sunPane = new Pane();
        Image sun = new Image("file:src/main/resources/images/太阳.jpg");
        ImageView sunview = new ImageView(sun);
        sunview.setFitHeight(size);
        sunview.setFitWidth(size);
//        sunview.setX(-size/2.0);
//        sunview.setY(-size/2.0);


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
        Image moon = new Image("file:src/main/resources/images/月亮2.jpg");
        ImageView moonview = new ImageView(moon);
        moonview.setFitHeight(size);
        moonview.setFitWidth(size);
//        moonview.setX(-size/2.0);
//        moonview.setY(-size/2.0);
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
        Image star = new Image("file:src/main/resources/images/黑洞.jpg");
        ImageView starview = new ImageView(star);
        starview.setFitHeight(size);
        starview.setFitWidth(size);
//        starview.setX(-size/2.0);
//        starview.setY(-size/2.0);
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
