module dat216 {
    requires javafx.controls;
    requires javafx.fxml;
    requires projectbackend;

    opens imat to javafx.fxml;
    exports imat;
}