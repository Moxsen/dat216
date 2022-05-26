/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class ProductDetail extends AnchorPane {

    private HomeModel model = HomeModel.getInstance();

    private HomeController mainController;
    private ProductPanel panel;

    private Product product;

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML Button productRemove;
    @FXML TextField productCount;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public ProductDetail(HomeController mainController, ProductPanel panel, Product product) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductDetail.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.mainController = mainController;
        this.panel = panel;

        this.product = product;

        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));

        updateProductCount();

        //productCount.setOnMouseClicked((MouseEvent e) -> Platform.runLater(() -> productCount.selectAll()));
        //productCount.setOnKeyPressed((KeyEvent e) -> Platform.runLater(() -> productCount.selectAll()));
    }

    private void updateProductCount() {
        Double count = model.getCartCountOf(product);
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
        model.decreaseIfInShoppingCart(new ShoppingItem(product));
        updateProductCount();
    }

    @FXML
    private void closeProductView(ActionEvent event) {
        System.out.println("Close " + this.product.getName());
        this.mainController.closeProductView(this);
        this.panel.updateProductCount();
    }

    @FXML
    public void handleProductCountEdit(KeyEvent keyEvent) {
        try {
            if (Integer.parseInt(productCount.getText()) < 0) productCount.setText("0");
            else if (Integer.parseInt(productCount.getText()) > 99) productCount.setText("99");
            int newCount = Integer.parseInt(productCount.getText());

            model.findInShoppingCart(new ShoppingItem(product)).setAmount(Integer.parseInt(productCount.getText()));
            /*// Maybe increase count
            for (int count = model.getCartCountOf(product); count < newCount; count++) {
                model.addToShoppingCart(product);
            }

            // Maybe decrease count
            for (int count = model.getCartCountOf(product); count > newCount; count--) {
                model.removeFromShoppingCart(product);
            }*/
        } catch (NumberFormatException ignored) {} finally {
            updateProductCount();
            //productCount.selectAll();
        }
    }
}
