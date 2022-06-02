/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class BigCartItem extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML TextField productCount;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public final Boolean productDiscrete;

    public BigCartItem(ShoppingItem item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BigCartItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        Product product = item.getProduct();
        this.productDiscrete = !product.getUnitSuffix().equals("kg") && !product.getUnitSuffix().equals("l");

        nameLabel.setText(product.getName() + " (" + product.getUnitSuffix() + ")");
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit().substring(0, 2));
        imageView.setImage(HomeModel.getInstance().getImage(product, kImageWidth, kImageWidth*kImageRatio));

        updateProductCountLabel(item.getAmount());
    }

    private void updateProductCountLabel(Double count) {
        productCount.setEditable(false);
        if (productDiscrete)
            productCount.setText("" + count.intValue());
        else
            productCount.setText("" + count);
    }
}

