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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import se.chalmers.cse.dat216.project.*;


/**
 *
 * @author oloft
 */
public class HomeController implements Initializable, ShoppingCartListener {
    // Strings
    private String favs = "Favoriter";

    @FXML
    StackPane stackPane;
    // Top pane
    @FXML
    AnchorPane topPane;
    @FXML
    private TextField searchField;
    @FXML
    VBox dynamicArea;

    // Shopping Pane
    @FXML
    private AnchorPane shopPane;
    @FXML
    private AnchorPane grayPane;
    @FXML
    private FlowPane productsFlowPane;
    @FXML
    private GridPane productGridPane;
    @FXML
    private ScrollPane centerPane;

    @FXML
    private AnchorPane dynamicPane;

    // Categories pane
    @FXML
    private FlowPane categoryFlowPane;

    //Cart pane
    @FXML
    private FlowPane cartFlowPane;

    //Wizard pane
    @FXML
    private AnchorPane wizardCart;
    @FXML
    private AnchorPane wizardInfo;
    @FXML
    private AnchorPane wizardBuy;
    @FXML
    private FlowPane wizardFlowPane;
    @FXML RadioButton time1;
    @FXML RadioButton time2;
    @FXML RadioButton time3;
    @FXML RadioButton time4;

    //Creditcard
    @FXML ComboBox cardTypeCombo;
    @FXML ComboBox  monthCombo;
    @FXML ComboBox  yearCombo;
    @FXML TextField numberTextField;
    @FXML TextField nameTextField;
    @FXML Label purchasesLabel;
    @FXML Label cvcLabel;
    @FXML TextField cvcField;
    //Delivery
    @FXML TextField firstNameTextField;
    @FXML TextField surnameTextField;
    @FXML TextField addressTextField;
    @FXML TextField codeTextField;
    @FXML TextField cityTextField;
    //Checkout
    @FXML Label wizardArticleCount;
    @FXML Label wizardTotalCost;


    // Other variables
    private final HomeModel model = HomeModel.getInstance();
    private AccountPanel accountPane;
    @FXML private Label totalCostLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model.getShoppingCart().addShoppingCartListener(this);
        updateProductList(model.getProducts());

        updateRightPanel();
        updateTopPanel();
        updateCenterPanel();
        updateLeftPanel();

        searchField.requestFocus();
        // Load the NamePanel and add it to dynamicPane
        // This shows how one can develop a view in a separate
        // FXML-file and then load it into on of the panes in the main interface
        // There is an fxml file NamePanel.fxml and a corresponding class NamePanel.java
        // Simply create a new NamePanel object and add it as a child of dynamicPane
        // The NamePanel holds a reference to the main controller (this class)
        accountPane = new AccountPanel(this, model.getCreditCard());
        stackPane.getChildren().add(accountPane);
        accountPane.toBack();
        //grayPane.setOnMouseClicked(mouseEvent -> clearSearchField());
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

    @FXML
    private void goHome(ActionEvent event) {
        closeWizard();
        //closeProductView(...);
        updateProductList(model.getProducts());
        for (Node node : categoryFlowPane.getChildren()) {
            node.getStyleClass().remove("category-marked");
        }
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
        closeWizard();
        openAccountView();
        accountPane.switchToOrderTab();
    }

    @FXML
    private void handleNameAction(ActionEvent event) {
        openNameView();
    }

    // Account pane actions


    @FXML
    private void handleOpenAccountAction(ActionEvent event) { openAccountView(); }

    //@FXML
   // private void handleDoneAction(ActionEvent event) {closeAccountView();}

    // Categories pane actions
    @FXML
    private void handleCategoryClicked(Button source) {
        clearSearchField();
        openCategory(source.getAccessibleText());

        for (Node node : categoryFlowPane.getChildren()) {
            node.getStyleClass().remove("category-marked");
        }
        source.getStyleClass().add("category-marked");
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
        for (Node node : nodes) {
            node.getStyleClass().setAll("specialCategory");
        }
        addCategories(nodes);
    }

    private void addCategory(ObservableList<Node> nodes, String text) {
        Button button = new Button(text.toLowerCase(Locale.ROOT).trim().replace("_", " ").replaceFirst("[a-z]", text.substring(0,1)));
        button.setAccessibleText(text);
        button.getStyleClass().setAll("categoryButton");
        button.setOnMouseClicked(mouseEvent -> handleCategoryClicked((Button) mouseEvent.getSource()));
        nodes.add(button);
    }

    private void addCategories(ObservableList<Node> nodes) {
        for (String productCategoryString: model.getProductCategories()) {
            addCategory(nodes, productCategoryString);
        }
    }

    // Navigation
    public void openAccountView() {
        accountPane.toFront();
    }

    public void closeAccountView() {
        //updateCreditCard();
        shopPane.toFront();
    }

    public void openWizard() {
        wizardCart.toFront();
        updateWizProductList(model.getShoppingCart().getItems());
    }

    public void openWizardInfo() {
        wizardInfo.toFront();
        updateWizardInfo();
    }

    private void updateWizardInfo() {
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        monthCombo.getItems().addAll(model.getMonths());
        yearCombo.getItems().addAll(model.getYears());

        updateCheckoutInfo();
    }

    private void updateCheckoutInfo() {
        CreditCard card = model.getCreditCard();

        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());

        cardTypeCombo.getItems().add(model.getCardTypes());
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        Customer customer = model.getCustomer();

        firstNameTextField.setText(customer.getFirstName());
        surnameTextField.setText(customer.getLastName());

        addressTextField.setText(customer.getAddress());
        cityTextField.setText(customer.getPostAddress());
        codeTextField.setText(customer.getPostCode());

        wizardArticleCount.setText(model.getShoppingCart().getItems().size() + " st");
        wizardTotalCost.setText(model.getShoppingCart().getTotal() + " SEK");
    }

    public void openBuy() {
        cvcLabel.setStyle("-fx-font-size: 16");
        if (cvcField.getText().length() == 3)
            wizardBuy.toFront();
        else cvcLabel.setStyle("-fx-font-size: 20");
        
        final ToggleGroup group = new ToggleGroup();

        time1.setToggleGroup(group);
        time1.setSelected(true);
        time2.setToggleGroup(group);
        time3.setToggleGroup(group);
        time4.setToggleGroup(group);
    }

    public void closeWizard() {
        wizardCart.toBack();
        wizardBuy.toBack();
        wizardInfo.toBack();
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
        totalCostLabel.setText(model.getShoppingCart().getTotal() + " SEK");
    }

    private void updateProductList(List<Product> products) {
        System.out.println("updateProductList " + products.size());
        productsFlowPane.getChildren().clear();
        for (Product product : products) {
            productsFlowPane.getChildren().add(new ProductPanel(this, product));
        }
    }

    private void updateWizProductList(List<ShoppingItem> items) {
        System.out.println("updateWizProductList " + items.size());
        wizardFlowPane.getChildren().clear();
        for (ShoppingItem item : items) {
            wizardFlowPane.getChildren().add(new ProductPanel(this, item.getProduct()));
        }
    }

    private void updateCenterPanel() {
        ShoppingCart shoppingCart = model.getShoppingCart();
        for (Node node : productsFlowPane.getChildren()) {
            ProductPanel prdPnl = (ProductPanel) node;
            prdPnl.updateProductCount();
        }
        //itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        //costLabel.setText("Kostnad: " + String.format("%.2f",shoppingCart.getTotal()));
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
