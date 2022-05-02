module andjoh {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.dat216 to javafx.fxml;
    exports org.dat216;
}