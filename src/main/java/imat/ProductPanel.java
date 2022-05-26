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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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

        if(model.isFavorite(product)) {
            heart.setImage(new Image(getClass().getResource(".dat215/heart2.png").toExternalForm()));
        } else {
            heart.setImage(new Image(getClass().getResource(".dat215/heart.png").toExternalForm()));
        }

        if (!product.isEcological()) {
            ecoLabel.setText("");
        }

        updateProductCount();

        heart.setOnMouseClicked(this::fav);
        productPane.setOnMouseClicked((MouseEvent e) -> mainController.openProductView(this, this.product));

        //productCount.setOnMouseClicked((MouseEvent e) -> Platform.runLater(() -> productCount.selectAll()));
        //productCount.setOnKeyPressed((KeyEvent e) -> Platform.runLater(() -> productCount.selectAll()));
    }

    private void fav(MouseEvent e) {
        e.consume();
        if(model.isFavorite(product)) {
            heart.setImage(new Image(getClass().getResource(".dat215/heart.png").toExternalForm()));
            model.removeFavorite(product);
        } else {
            heart.setImage(new Image(getClass().getResource(".dat215/heart2.png").toExternalForm()));
            model.addFavorite(product);
        }
    }

    public void updateProductCount() {
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

    public void updateProductCountLabel(Double count) {
        if (count % 1 == 0) productCount.setText("" + count.intValue());
        else productCount.setText("" + count);
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        updateProductCount();
    }

    @FXML
    private void handleDecreaseAction(ActionEvent event) {
        System.out.println("Decrease " + product.getName());
        model.decreaseIfInShoppingCart(new ShoppingItem(product));
        updateProductCount();
    }

    @FXML
    public void handleProductCountEdit(KeyEvent keyEvent) {
        if (productCount.lengthProperty().getValue() > 0) {
            if (keyEvent.getCharacter().equals("."));
            else if (Double.parseDouble(productCount.getText()) < 0) productCount.setText("0");
            else if (Double.parseDouble(productCount.getText()) > 99) productCount.setText("99");
            else {
                double newCount = Double.parseDouble(productCount.getText());
                System.out.println("Amount is " + newCount);
                model.findInShoppingCart(new ShoppingItem(product)).setAmount(newCount);

                model.getShoppingCart().fireShoppingCartChanged(new ShoppingItem(product), true);
            }
        }
    }
}

