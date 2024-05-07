module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}