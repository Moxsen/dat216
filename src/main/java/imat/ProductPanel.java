/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;

/**
 *
 * @author oloft
 */
public class ProductPanel extends AnchorPane {

    @FXML AnchorPane productPane;
    @FXML ImageView imageView;
    @FXML ImageView heart;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML Label ecoLabel;
    @FXML Button productRemove;
    @FXML TextField productCount;
    
    private HomeModel model = HomeModel.getInstance();

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

        this.product = product;
        nameLabel.setText(product.getName() + " (" + product.getUnitSuffix() + ")");
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit().substring(0, 2));
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));
        heart.setImage(new Image(getClass().getResource(".dat215/heart.png").toExternalForm()));
        if (!product.isEcological()) {
            ecoLabel.setText("");
        }

        updateProductCount();

        heart.setOnMouseClicked((MouseEvent e) -> System.out.println("FAVORITE " + product.getName()));
        productPane.setOnMouseClicked((MouseEvent e) -> mainController.openProductView(this, this.product));

        productCount.setOnMouseClicked((MouseEvent e) -> Platform.runLater(() -> productCount.selectAll()));
        productCount.setOnKeyPressed((KeyEvent e) -> Platform.runLater(() -> productCount.selectAll()));
    }

    public void updateProductCount() {
        int count = model.getCartCountOf(product);
        if (count > 0) {
            productRemove.setVisible(true);
            productCount.setVisible(true);
            productCount.setText(String.valueOf(count));
        } else {
            productRemove.setVisible(false);
            productCount.setVisible(false);
        }
    }
    
    @FXML
    private void handleAddAction(ActionEvent event) {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        updateProductCount();
    }

    @FXML
    private void handleRemoveAction(ActionEvent event) {
        System.out.println("Remove " + product.getName());
        model.removeFromShoppingCart(product);
        updateProductCount();
    }

    @FXML
    public void handleProductCountEdit(KeyEvent keyEvent) {
        try {
            int newCount = Integer.parseInt(productCount.getText());

            // Maybe increase count
            for (int count = model.getCartCountOf(product); count < newCount; count++) {
                model.addToShoppingCart(product);
            }

            // Maybe decrease count
            for (int count = model.getCartCountOf(product); count > newCount; count--) {
                model.removeFromShoppingCart(product);
            }
        } catch (NumberFormatException ignored) {} finally {
            updateProductCount();
            productCount.selectAll();
        }
    }
}
