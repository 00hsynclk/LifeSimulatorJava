module main.lifesimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.lifesimulator to javafx.fxml;
    exports main.lifesimulator;
}