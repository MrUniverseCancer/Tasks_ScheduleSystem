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
        return new Pane(circle);
    }

    private Pane createSun() {
        Pane sunPane = new Pane();
        Circle circle = new Circle();
        circle.setFill(Color.YELLOW);
        sunPane.getChildren().add(circle);

        for (int i = 0; i < 12; i++) {
            Line line = new Line(50, 0, 70, 0);
            line.setStrokeWidth(3);
            line.setTranslateX(50);
            line.setRotate(i * 30);
            line.setStroke(Color.ORANGE);
            sunPane.getChildren().add(line);
        }


        return new Pane(sunPane);
    }

    private Pane createMoon() {
        Pane moonPane = new Pane();
        Image moon = new Image("file:src/main/resources/images/月亮.jpg");
        moonPane.getChildren().add(new ImageView(moon));
        return moonPane;
    }

    private Pane createCrescentMoonPane() {
        Pane pane = new Pane();
        Canvas canvas = new Canvas(400, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the crescent moon
        drawCrescentMoon(gc, 200, 150, 100, 50);

        pane.getChildren().add(canvas);
        return pane;
    }

    private void drawCrescentMoon(GraphicsContext gc, double centerX, double centerY, double radius, double offset) {
        // Draw the yellow crescent moon
        gc.setFill(javafx.scene.paint.Color.YELLOW);
        gc.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Draw the white part to create the crescent effect
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillOval(centerX - radius + offset, centerY - radius, 2 * radius, 2 * radius);

        // If you want to add more detail or adjust the appearance, you can adjust these parameters.
    }


    private Pane createStar() {
        Path star = new Path();
        star.setFill(Color.GOLD);
        star.setStroke(Color.BLACK);

        // Star path points
        double[] points = {
                0, 30,  10, 10,  20, 30,  30, 10,  40, 30,
                30, 50,  40, 70,  20, 60,  0, 70,  10, 50,
                0, 30
        };

        for (int i = 0; i < points.length; i += 2) {
            star.getElements().add(new javafx.scene.shape.MoveTo(points[i], points[i + 1]));
            if (i + 2 < points.length) {
                star.getElements().add(new javafx.scene.shape.LineTo(points[i + 2], points[i + 3]));
            }
        }
        star.getElements().add(new javafx.scene.shape.ClosePath());

        return new Pane(star);
    }

}
