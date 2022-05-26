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

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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
    //
    // Strings
    private String favs = "Favoriter";

    // Top pane
    @FXML
    AnchorPane topPane;
    @FXML
    private TextField searchField;

    // Shopping Pane
    @FXML
    private AnchorPane shopPane;
    @FXML
    private AnchorPane grayPane;
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
    private FlowPane categoryFlowPane;

    //Cart pane
    @FXML
    private FlowPane cartFlowPane;

    // Other variables
    private final HomeModel model = HomeModel.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        model.getShoppingCart().addShoppingCartListener(this);

        updateRightPanel();
        updateTopPanel();
        updateCenterPanel();
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
        grayPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                clearSearchField();
            }
        });

        updateProductList(model.getProducts());

        searchField.requestFocus();
    }

    private void updateRightPanel() {
        updateCartPanel();
    }

    private void updateCartPanel() {
        cartFlowPane.getChildren().clear();
        for (ShoppingItem item : model.getShoppingCart().getItems())
            cartFlowPane.getChildren().add(new ItemPanel(this, item));
    }

    private void updateTopPanel() {

    }

    // Shop pane actions
    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }

    //Search field actions
    @FXML
    private void handleSearchAction() {
        if (searchField.lengthProperty().getValue() == 0) return;
        List<Product> matches = model.findProducts(searchField.getText());
        updateProductList(matches);
        System.out.println("# matching products: " + matches.size());
    }

    @FXML
    public void handleSearchTyping(KeyEvent keyEvent) {
        if (searchField.lengthProperty().getValue() == 0) grayPane.toBack();
        else if (searchField.lengthProperty().getValue() < 3) grayPane.toFront();
        else {
            grayPane.toBack();
            handleSearchAction();
        }
    }

    private void clearSearchField() {
        searchField.clear();
        grayPane.toBack();
    }

    //Shopping cart actions
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
    private void handleCategoryClicked(Button source) {
        clearSearchField();
        openCategory(source.getAccessibleText());
    }

    private void openCategory(String categoryString) {
        if (categoryString.equals(favs)) openFavorites();
        else if (categoryString.equals("Just nu")) handleSearchAction();
        else if (categoryString.equals("Fruit")) openFruitCategory();
        else if (categoryString.equals("Vegetable")) openVegetableCategory();
        else updateProductList(model.getProductsInCategory(categoryString));
    }

    private void openVegetableCategory() {
        categoryFlowPane.getChildren().clear();
    }

    private void openFruitCategory() {
        //categoryFlowPane.getChildren().get(3).setVisible(!categoryFlowPane.getChildren().get(3).isVisible());
        //categoryFlowPane.getChildren().get(3).setDisable(!categoryFlowPane.getChildren().get(3).isDisable());
        categoryFlowPane.getChildren().add(1,categoryFlowPane.getChildren().get(3) );
    }

    private void openFavorites() {
        updateProductList(model.getFavorites());
    }

    //Controller stuff
    private void updateLeftPanel() {
        clearCategoryFlowPane();
        updateCategoryFlowPane(categoryFlowPane.getChildren());
    }

    private void clearCategoryFlowPane() {
        categoryFlowPane.getChildren().clear();
    }

    private void updateCategoryFlowPane(ObservableList<Node> nodes) {
        addCategory(nodes, favs);
        addCategory(nodes, "Just nu");
        for (Node node : nodes
        ) {
            node.getStyleClass().setAll("specialCategory");
        }
        addCategories(nodes);
    }

    private void addCategory(ObservableList<Node> nodes, String text) {
        Button button = new Button(text.toLowerCase(Locale.ROOT).trim().replace("_", " ").replaceFirst("[a-z]", text.substring(0,1)));
        button.setAccessibleText(text);
        button.getStyleClass().setAll("categoryButton");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleCategoryClicked((Button) mouseEvent.getSource());
            }
        });
        nodes.add(button);
    }

    private void addCategories(ObservableList<Node> nodes) {
        for (String productCategoryString: model.getProductCategories()
        ) {
            addCategory(nodes, productCategoryString);
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
        updateCenterPanel();
        updateCartPanel();
    }


    private void updateProductList(List<Product> products) {
        System.out.println("updateProductList " + products.size());
        productsFlowPane.getChildren().clear();
        for (Product product : products) {
            productsFlowPane.getChildren().add(new ProductPanel(this, product));
        }
    }

    private void updateCenterPanel() {

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

    public void openProductView(ProductPanel panel, Product product) {
        System.out.println("Open " + product.getName());

        ProductDetail productDetail = new ProductDetail(this, panel, product);

        centerPane.setVisible(false);
        shopPane.getChildren().add(productDetail);
    }

    public void closeProductView(ProductDetail productDetail) {
        centerPane.setVisible(true);
        shopPane.getChildren().remove(productDetail);
    }

    public void openProductView(Product product) {
        System.out.println("Open " + product.getName());

        ProductDetail productDetail = new ProductDetail(this, new ProductPanel(this, product), product);

        centerPane.setVisible(false);
        shopPane.getChildren().add(productDetail);
    }
}
