/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

        productCount.setOnMouseClicked((MouseEvent e) -> Platform.runLater(() -> productCount.selectAll()));
    }

    private void updateProductCount() {
        Double count = model.getCartCountOf(product);
        if (count > 0) {
            productRemove.setVisible(true);
            productCount.setVisible(true);
            updateProductCountLabel(count);
        } else {
            productRemove.setVisible(false);
            productCount.setVisible(false);
        }
    }

    private void updateProductCountLabel(Double count) {
        int pos = productCount.getCaretPosition();

        if (this.panel.productDiscrete)
            productCount.setText("" + count.intValue());
        else
            productCount.setText("" + count);

        productCount.positionCaret(pos);
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
    private void handleProductCountEdit(KeyEvent keyEvent) {
        String text = String.format("%.4s", productCount.getText());

        try {
            if (this.panel.productDiscrete) {
                int newCount = Integer.parseInt(text);
                if (newCount < 0) newCount = 0;
                if (newCount >= 100) newCount = 100;

                System.out.println("Amount is " + newCount);

                model.findInShoppingCart(new ShoppingItem(product)).setAmount(newCount);
            } else {
                double newCount = Double.parseDouble(text);
                if (newCount < 0) newCount = 0;
                if (newCount >= 100) newCount = 100;

                System.out.println("Amount is " + newCount);

                model.findInShoppingCart(new ShoppingItem(product)).setAmount(newCount);
            }
        } catch (NumberFormatException ignored) {} finally {
            model.getShoppingCart().fireShoppingCartChanged(new ShoppingItem(product), true);
            updateProductCount();
        }
    }
}
