/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class OrderHistoryItem extends AnchorPane {

    @FXML AnchorPane orderItemPane;
    @FXML Label orderLabel;
    @FXML Label dateLabel;
    @FXML Label orderCost;


    private final HomeModel model = HomeModel.getInstance();

    private Order order;

    public OrderHistoryItem(Order order, AccountPanel panel) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.order = order;
        updateOrderPanel(this.order);

        orderItemPane.setOnMouseClicked((MouseEvent e) -> panel.openOrder(order));
    }

    private void updateOrderPanel(Order order) {
        orderLabel.setText("Ordernummer: " + String.valueOf(order.getOrderNumber()));
        dateLabel.setText(String.valueOf(order.getDate()));
        orderCost.setText(String.valueOf(getTotalOrderCost()) + " Kr");
    }

    public double getTotalOrderCost() {
        double totalCostDouble = 0.0;
        for (ShoppingItem item : order.getItems()
        ) {
            totalCostDouble =+ item.getTotal();
        }
        return totalCostDouble;
    }
}
