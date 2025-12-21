module com.example.plantfeedgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.plantfeedgui to javafx.fxml;
    exports com.example.plantfeedgui;
}