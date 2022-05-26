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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class ItemPanel extends AnchorPane {

    @FXML AnchorPane itemPane;
    @FXML Label productTitleLabel;
    @FXML Label productPrizeLabel;
    @FXML Label productCount;
    @FXML Button productRemove;

    private HomeModel model = HomeModel.getInstance();

    private ShoppingItem shoppingItem;

    public ItemPanel(HomeController mainController, ShoppingItem item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("smallCartItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingItem = item;
        updateItemPanel(this.shoppingItem);
        updateProductCount();

        //heart.setOnMouseClicked((MouseEvent e) -> System.out.println("FAVORITE " + product.getName()));
        itemPane.setOnMouseClicked((MouseEvent e) -> mainController.openProductView(this.shoppingItem.getProduct()));

    }

    private void updateItemPanel(ShoppingItem shoppingItem) {
        productTitleLabel.setText(shoppingItem.getProduct().getName() + " (" + shoppingItem.getProduct().getUnitSuffix() + ")");
        productPrizeLabel.setText(String.format("%.2f", shoppingItem.getProduct().getPrice()) + " " + shoppingItem.getProduct().getUnit().substring(0, 2));
        productCount.setText("" + shoppingItem.getAmount());
        //imageView.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));

    }

    public void updateProductCount() {
        int count = model.getCartCountOf(shoppingItem.getProduct());
        System.out.println(count);
    }
    
    @FXML
    private void handleAddAction(ActionEvent event) {
        System.out.println("Add " + shoppingItem.getProduct().getName());
        model.addToShoppingCart(shoppingItem.getProduct());

        updateProductCount();
    }

    @FXML
    private void handleRemoveAction(ActionEvent event) {
        System.out.println("Remove " + shoppingItem.getProduct().getName());
        model.removeFromShoppingCart(shoppingItem.getProduct());
        updateProductCount();
    }
}
