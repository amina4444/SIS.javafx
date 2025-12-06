module com.example.demoproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.demoproject to javafx.fxml;
    opens com.example.demoproject.model to javafx.base, com.fasterxml.jackson.databind;
    exports com.example.demoproject;
}