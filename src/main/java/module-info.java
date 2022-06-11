module com.example.ass4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ass4 to javafx.fxml;
    exports com.example.ass4;
}