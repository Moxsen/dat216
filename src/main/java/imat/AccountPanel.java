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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.CreditCard;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class AccountPanel extends AnchorPane {

    @FXML AnchorPane accountPane;
    @FXML ComboBox cardTypeCombo;
    @FXML ComboBox  monthCombo;
    @FXML ComboBox  yearCombo;
    @FXML TextField numberTextField;
    @FXML TextField nameTextField;
    @FXML TextField issuer;
    @FXML TextField cvcField;
    @FXML Label purchasesLabel;

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

        this.creditCard = creditCard;
        updateAccountPanel();

    }

    private void updateAccountPanel() {
        CreditCard card = model.getCreditCard();

        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());

        cardTypeCombo.getItems().add(model.getCardTypes());
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");

    }
    private void setupAccountPane() {

        cardTypeCombo.getItems().addAll(model.getCardTypes());

        monthCombo.getItems().addAll(model.getMonths());

        yearCombo.getItems().addAll(model.getYears());

    }



    @FXML
    private void updateCreditCard() {

        CreditCard card = model.getCreditCard();

        card.setCardNumber(numberTextField.getText());
        card.setHoldersName(nameTextField.getText());

        String selectedValue = (String) cardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);

        selectedValue = (String) monthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));

        selectedValue = (String) yearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));

        this.toBack();
    }


}
