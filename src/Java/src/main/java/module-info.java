module org.example.java {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.java to javafx.fxml;
    exports org.example.java;
    exports GUI_design to javafx.graphics;
}