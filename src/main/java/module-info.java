module org.dat216 {
    requires javafx.controls;
    requires javafx.fxml;
    requires projectbackend;

    opens org.dat216 to javafx.fxml;
    exports org.dat216;
}