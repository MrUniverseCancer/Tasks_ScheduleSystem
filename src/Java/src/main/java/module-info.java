module org.example.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.opencsv;


    exports org.example.GUI_design to javafx.graphics;
    exports org.example.GUI_design.generalData to javafx.graphics;
}