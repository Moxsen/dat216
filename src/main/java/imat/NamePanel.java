/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Customer;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class NamePanel extends AnchorPane {

    @FXML
    TextField fnameTextField;

    @FXML
    TextField lnameTextField;

    private HomeModel model = HomeModel.getInstance();

    private Customer customer;
    private HomeController mainController;

    public NamePanel(HomeController mainController) {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NamePanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.mainController = mainController;
        this.customer = model.getCustomer();
        updateView();
    }

    @FXML
    public void closeNameView(){

        // Save values
        customer.setFirstName(fnameTextField.getText());
        customer.setLastName(lnameTextField.getText());
        mainController.closeNameView();

    }

    private void updateView() {

        fnameTextField.setText(customer.getFirstName());
        lnameTextField.setText(customer.getLastName());
    }

}
