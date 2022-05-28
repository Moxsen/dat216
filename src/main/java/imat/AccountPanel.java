/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.CreditCard;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.Order;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class AccountPanel extends AnchorPane {

    @FXML AnchorPane accountPane;
    //Creditcard
    @FXML ComboBox cardTypeCombo;
    @FXML ComboBox  monthCombo;
    @FXML ComboBox  yearCombo;
    @FXML TextField numberTextField;
    @FXML TextField nameTextField;
    @FXML Label purchasesLabel;
    //Delivery
    @FXML TextField firstNameTextField;
    @FXML TextField surnameTextField;
    @FXML TextField addressTextField;
    @FXML TextField codeTextField;
    @FXML TextField cityTextField;

    //Order history
    @FXML FlowPane orderHistoryFlowPane;

    @FXML TabPane tabPane;

    private HomeModel model = HomeModel.getInstance();

    private CreditCard creditCard;

    public AccountPanel(HomeController mainController, CreditCard creditCard) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setupAccountPane();
    }

    public void switchToOrderTab() {
        tabPane.getSelectionModel().select(2);
        updateAccountPanel();
    }

    private void updateAccountPanel() {
        updateAccountTab();
        updateDeliveryTab();
        updateorderHistoryTab();
    }

    private void setupAccountPane() {
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        monthCombo.getItems().addAll(model.getMonths());
        yearCombo.getItems().addAll(model.getYears());

        updateAccountPanel();
    }

    private void updateAccountTab() {
        CreditCard card = model.getCreditCard();

        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());

        cardTypeCombo.getItems().add(model.getCardTypes());
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
    }

    private void updateDeliveryTab() {
        Customer customer = model.getCustomer();

        firstNameTextField.setText(customer.getFirstName());
        surnameTextField.setText(customer.getLastName());

        addressTextField.setText(customer.getAddress());
        cityTextField.setText(customer.getPostAddress());
        codeTextField.setText(customer.getPostCode());
    }

    private void updateorderHistoryTab() {
        // Sort orders by most recent
        model.getOrders().sort((a, b) -> b.getOrderNumber() - a.getOrderNumber());

        orderHistoryFlowPane.getChildren().clear();
        for (Order order : model.getOrders()) {
            orderHistoryFlowPane.getChildren().add(new OrderHistoryItem(order));
        }
    }

    @FXML
    private void saveInformation() {
        saveCardInformation();
        saveDeliveryInformation();

        this.toBack();
    }

    private void saveCardInformation() {

        CreditCard card = model.getCreditCard();

        card.setCardNumber(numberTextField.getText());
        card.setHoldersName(nameTextField.getText());

        String selectedValue = (String) cardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);

        selectedValue = (String) monthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));

        selectedValue = (String) yearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));
    }

    private void saveDeliveryInformation() {
        Customer customer = model.getCustomer();

        customer.setFirstName(firstNameTextField.getText());
        customer.setLastName(surnameTextField.getText());

        customer.setAddress(addressTextField.getText());
        customer.setPostAddress(cityTextField.getText());
        customer.setPostCode(codeTextField.getText());
    }

}
