module org.example.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.opencsv;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    exports org.example.GUI_design to javafx.graphics;
    exports org.example.GUI_design.generalData to javafx.graphics;
    exports org.example.server;
}