/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imat;

/**
 *
 * @author oloft
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;
import se.chalmers.cse.dat216.project.*;


/**
 * Wrapper around the IMatDataHandler. The idea is that it might be useful to
 * add an extra layer that can provide special features
 *
 */
public class HomeModel {

    private static HomeModel instance = null;
    private IMatDataHandler iMatDataHandler;

    private final ArrayList<String> availableCardTypes = new ArrayList<String>(Arrays.asList("MasterCard", "Visa"));
    private final ArrayList<String> months = new ArrayList<String>(Arrays.asList("1", "2","3", "4", "5", "6"));
    private final ArrayList<String> years = new ArrayList<String>(Arrays.asList("19", "20", "21", "22", "23", "24", "25"));
    /**
     * Constructor that should never be called, use getInstance() instead.
     */
    protected HomeModel() {
        // Exists only to defeat instantiation.
    }

    /**
     * Returns the single instance of the Model class.
     */
    public static HomeModel getInstance() {
        if (instance == null) {
            instance = new HomeModel();
            instance.init();
        }
        return instance;
    }

    private void init() {

        iMatDataHandler = IMatDataHandler.getInstance();

    }

    public List<Product> getProducts() {
        return iMatDataHandler.getProducts();
    }

    public List<String> getProductCategories() {
        List<String> result = new ArrayList<>();
        for (ProductCategory productCategory: ProductCategory.values()
        ) {
            result.add(productCategory.toString());
        }
        //TODO Sort in nice order
        return result;
    }

    public Product getProduct(int idNbr) {
        return iMatDataHandler.getProduct(idNbr);
    }

    public List<Product> findProducts(java.lang.String s) {
        return iMatDataHandler.findProducts(s);
    }

    public Image getImage(Product p) {
        return iMatDataHandler.getFXImage(p);
    }

    public Image getImage(Product p, double width, double height) {
        return iMatDataHandler.getFXImage(p, width, height);
    }

    public boolean addToShoppingCart(Product p) {
        ShoppingItem item = new ShoppingItem(p);
        if (increaseAmountIfInCart(item));
        else if (getCartCountOf(p) < 9)
            HomeModel.getInstance().getShoppingCart().addItem(item);
        printShoppingCart();
        getShoppingCart().fireShoppingCartChanged(item,true);
        return true;
    }

    private boolean increaseAmountIfInCart(ShoppingItem item) {
        ShoppingItem itemAlreadyInCart = findInShoppingCart(item);
        if (itemAlreadyInCart != null) {
            itemAlreadyInCart.setAmount(itemAlreadyInCart.getAmount() + 1);
            return true;
        } else return false;

    }


    private boolean existsInShoppingCart(ShoppingItem p) {
        if (findInShoppingCart(p) != null) return true;
        return false;
    }

    ShoppingItem findInShoppingCart(ShoppingItem p) {
        ShoppingCart cart = HomeModel.getInstance().getShoppingCart();
        for(ShoppingItem i : cart.getItems()) {
            if(i.getProduct().getName().equals(p.getProduct().getName())) {
                return i;
            }
        }
        return null;
    }

    public void removeFromShoppingCart(Product p) {
        ShoppingCart cart = HomeModel.getInstance().getShoppingCart();
        ShoppingItem item = new ShoppingItem(p);
        // Should be a better way to do this but couldn't find it
        for(ShoppingItem i : cart.getItems()) {
            if(i.getProduct().getName().equals(item.getProduct().getName())) {
                if (i.getAmount() > 1) {
                    i.setAmount(i.getAmount() - 1);
                    getShoppingCart().fireShoppingCartChanged(item,true);
                } else cart.removeItem(i);
                break;
            }
        }
        printShoppingCart();
    }

    public Double getCartCountOf(Product p) {
        Double count = 0.0;
        ShoppingCart cart = HomeModel.getInstance().getShoppingCart();
        ShoppingItem item = new ShoppingItem(p);
        for(ShoppingItem i : cart.getItems()) {
            if(i.getProduct().getName().equals(item.getProduct().getName())) {
                count = (count + i.getAmount());
            }
        }

        return count;
    }

    public List<String> getCardTypes() {
        return availableCardTypes;
    }

    public List<String> getMonths() {
        return months;
    }

    public List<String> getYears() {
        return years;
    }

    public CreditCard getCreditCard() {
        return iMatDataHandler.getCreditCard();
    }

    public Customer getCustomer() {
        return iMatDataHandler.getCustomer();
    }

    public ShoppingCart getShoppingCart() {
        return iMatDataHandler.getShoppingCart();
    }

    public void printShoppingCart() {
        System.out.print("CART: ");
        for(ShoppingItem item : this.getShoppingCart().getItems()) {
            System.out.print(item.getAmount() + " " + item.getProduct().getName() + ", ");
        }
        System.out.println();
    }

    public void clearShoppingCart() {

        iMatDataHandler.getShoppingCart().clear();

    }

    public void placeOrder() {

        iMatDataHandler.placeOrder();

    }


    public int getNumberOfOrders() {

        return iMatDataHandler.getOrders().size();

    }

    public void shutDown() {
        iMatDataHandler.shutDown();
    }

    public List<Product> getProductsInCategory(String text) {
        return iMatDataHandler.getProducts(ProductCategory.valueOf(text));

    }

    public List<Product> getFavorites() {
        return iMatDataHandler.favorites();
    }
}
