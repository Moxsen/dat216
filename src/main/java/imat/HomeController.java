/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import se.chalmers.cse.dat216.project.*;


/**
 *
 * @author oloft
 */
public class HomeController implements Initializable, ShoppingCartListener {
    
    // Shopping Pane
    @FXML
    private AnchorPane shopPane;
    @FXML
    private TextField searchField;
    //@FXML
    //private Label itemsLabel;
    //@FXML
    //private Label costLabel;
    @FXML
    private FlowPane productsFlowPane;
    @FXML
    private GridPane productGridPane;
    @FXML
    private ScrollPane centerPane;
    
    // Account Pane
    @FXML
    private AnchorPane accountPane;
    @FXML 
    ComboBox cardTypeCombo;
    @FXML
    private TextField numberTextField;
    @FXML
    private TextField nameTextField;
    @FXML 
    private ComboBox monthCombo;
    @FXML
    private ComboBox yearCombo;
    @FXML
    private TextField cvcField;
    @FXML
    private Label purchasesLabel;

    @FXML
    private AnchorPane dynamicPane;

    // Categories pane
    @FXML
    private Accordion categoryAccordion;

    // Other variables
    private final HomeModel model = HomeModel.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        model.getShoppingCart().addShoppingCartListener(this);

        updateProductList(model.getProducts());
        updateBottomPanel();
        updateLeftPanel();

        setupAccountPane();

        // Load the NamePanel and add it to dynamicPane
        // This shows how one can develop a view in a separate
        // FXML-file and then load it into on of the panes in the main interface
        // There is an fxml file NamePanel.fxml and a corresponding class NamePanel.java
        // Simply create a new NamePanel object and add it as a child of dynamicPane
        // The NamePanel holds a reference to the main controller (this class)
        AnchorPane namePane = new NamePanel(this);
        dynamicPane.getChildren().add(namePane);

    }

    // Shop pane actions
    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }
    
    @FXML
    private void handleSearchAction(ActionEvent event) {
        
        List<Product> matches = model.findProducts(searchField.getText());
        updateProductList(matches);
        System.out.println("# matching products: " + matches.size());

    }
    
    @FXML
    private void handleClearCartAction(ActionEvent event) {
        model.clearShoppingCart();
    }
    
    @FXML
    private void handleBuyItemsAction(ActionEvent event) {
        model.placeOrder();
        //costLabel.setText("Köpet klart!");
    }

    @FXML
    private void handleNameAction(ActionEvent event) {
        openNameView();
    }

    // Account pane actions
     @FXML
    private void handleDoneAction(ActionEvent event) {
        closeAccountView();
    }

    // Categories pane actions
    @FXML
    private void openFavorites() {
        updateProductList(model.getFavorites());
    }
    @FXML
    private void openCategory(String text) {
        updateProductList(model.getProductsInCategory(text));

    }

    //Controller stuff
    private void updateLeftPanel() {
        fillCategoryAccordion(categoryAccordion.getPanes());
    }

    private void fillCategoryAccordion(List<TitledPane> titledPanes) {
        for (ProductCategory productCategory: model.getProductCategories()
             ) {
            TitledPane titledPane = new TitledPane();
            titledPane.setCollapsible(false);
            titledPane.setAccessibleText(productCategory.name());
            titledPane.setText(productCategory.name().toLowerCase(Locale.ROOT).trim().replace("_", " ").replaceFirst("[a-z]", productCategory.name().substring(0,1)));
            titledPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    TitledPane sourceTitledPane = (TitledPane) mouseEvent.getSource();
                    openCategory(sourceTitledPane.getAccessibleText());
                }
            });
            titledPanes.add(titledPane);
        }
    }

    // Navigation
    public void openAccountView() {
        updateAccountPanel();
        accountPane.toFront();
    }

    public void closeAccountView() {
        updateCreditCard();
        shopPane.toFront();
    }

    public void openNameView() {
        dynamicPane.toFront();
    }

    public void closeNameView() {
        shopPane.toFront();
    }
    // Shope pane methods
    @Override
     public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
    }
   
    
    private void updateProductList(List<Product> products) {

        System.out.println("updateProductList " + products.size());
        productsFlowPane.getChildren().clear();
        //productGridPane.getChildren().clear();
        for (Product product : products) {
            productsFlowPane.getChildren().add(new ProductPanel(this, product));
            //productGridPane.getChildren().add(new ProductPanel(this, product));
        }

    }
    
    private void updateBottomPanel() {
        
        ShoppingCart shoppingCart = model.getShoppingCart();
        
        //itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        //costLabel.setText("Kostnad: " + String.format("%.2f",shoppingCart.getTotal()));
        
    }
    
    private void updateAccountPanel() {
        
        CreditCard card = model.getCreditCard();
        
        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());
        
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        cvcField.setText(""+card.getVerificationCode());
        
        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
        
    }
    
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
        
        card.setVerificationCode(Integer.parseInt(cvcField.getText()));

    }
    
    private void setupAccountPane() {
                
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        
        monthCombo.getItems().addAll(model.getMonths());
        
        yearCombo.getItems().addAll(model.getYears());
        
    }

    public void openProductView(Product product) {
        System.out.println("Open " + product.getName());

        ProductDetail productDetail = new ProductDetail(this, product);

        centerPane.setVisible(false);
        shopPane.getChildren().add(productDetail);
    }

    public void closeProductView(ProductDetail productDetail) {
        centerPane.setVisible(true);
        shopPane.getChildren().remove(productDetail);
    }
}
