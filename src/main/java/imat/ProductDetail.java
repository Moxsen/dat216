/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class ProductDetail extends AnchorPane {

    private HomeModel model = HomeModel.getInstance();

    private Product product;

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public ProductDetail(Product product) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductDetail.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;

        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        System.out.println("Add " + this.product.getName());
        model.addToShoppingCart(this.product);
    }
}
