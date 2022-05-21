/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

/**
 *
 * @author oloft
 */
public class ProductPanel extends AnchorPane {

    @FXML AnchorPane productPane;
    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML Label ecoLabel;
    @FXML TextField productCount;
    
    private HomeModel model = HomeModel.getInstance();

    private HomeController mainController;

    private Product product;
    
    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public ProductPanel(HomeController mainController, Product product) {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.mainController = mainController;

        this.product = product;
        nameLabel.setText(product.getName() + " (" + product.getUnitSuffix() + ")");
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit().substring(0, 2));
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));
        if (!product.isEcological()) {
            ecoLabel.setText("");
        }

        productPane.setOnMouseClicked((MouseEvent e) -> mainController.openProductView(this.product));
    }
    
    @FXML
    private void handleAddAction(ActionEvent event) {
        System.out.println("Add " + product.getName());
        productCount.setText(String.valueOf(Integer.parseInt(productCount.getText()) + 1));
        model.addToShoppingCart(product);
        model.printShoppingCart();
    }

    @FXML
    private void handleRemoveAction(ActionEvent event) {
        System.out.println("Remove " + product.getName());
        productCount.setText(String.valueOf(Integer.parseInt(productCount.getText()) - 1));
        model.removeFromShoppingCart(product);
        model.printShoppingCart();
    }
}
