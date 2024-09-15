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
