package GUI_design;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Main_Page {

    private Scene scene;

    public Main_Page() {
        createScene();
    }

    public void createScene() {
        Pane root = new Pane();



        Circle circle = new Circle();
//        circle.setCenterX(100.0f);
//        circle.setCenterY(100.0f);
//        circle.setRadius(50.0f);
        circle.centerXProperty().bind(root.widthProperty().divide(2));
        circle.centerYProperty().bind(root.heightProperty().divide(2));
        circle.radiusProperty().bind(Bindings.createDoubleBinding(
                () -> radiusPropertyget(root.widthProperty(), root.heightProperty()),
                root.widthProperty(),
                root.heightProperty()
        ));

        root.getChildren().add(circle);
        this.scene = new Scene(root, 300, 250);
    }

    public double radiusPropertyget(ReadOnlyDoubleProperty x, ReadOnlyDoubleProperty y) {
        return Math.min(x.get(), y.get()) / 2.0;
    }

    public Scene getScene() {
        return this.scene;
    }
}
