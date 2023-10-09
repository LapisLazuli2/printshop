package service;

import java.util.Scanner;

import pojo.Item;
import pojo.Order;

public class OrderService {
    private Order order; //contains the items with their specified quantities that the user adds to the order
    private String shoppingCart; //a string containing the shopping cart information based on the current items in the order
    private Scanner sc;
    
    /*
     * Constructor
     */
    public OrderService() {
        this.order = new Order();
        this.sc = new Scanner(System.in);
    }

    /*
     * Function: retrieveOrder()
     * Returns a copy of this.order
     */
    public Order retrieveOrder(){
        return new Order(this.order);
    }
    
    /*
     * Function: retrieveOrderProductionTimeMinutes()
     * Returns how many minutes it takes to produce all the items in the order
     */
    public int retrieveOrderProductionTimeMinutes(){
        return this.order.getProductionTimeMinutes();
    }
    
    /*
     * Function: displayIntroduction()
     * returns a string introducing the user to the store
     */
    public String displayIntroduction() {
        String introductionText = "---------------------------------------------------\n";
        introductionText += "Welcome to the printshop! Please take a look at our available print options:\n";
        return introductionText;
    }

    /*
     * Function: displayOrderInterface()
     * returns a string listing the available store commands
     */
    public String displayOrderInterface() {
        return "Available Commands\nAdd an item to order: add\nRemove an item from the order: remove\nGo the checkout screen: checkout\nReview past orders: review\nExit the application: exit\n>>";
    }

    /*
     * Function: displayShoppingCart()
     * Updates the shoppingCart string with the information of the items currently in the order and returns that string
     */
    public String displayShoppingCart() {
        this.shoppingCart = "--- Shopping Cart ---\nQuantity\tSubtotal\tID\tItem\n";
        this.order.getOrder().forEach((Item item, Integer quantity) -> {
            this.shoppingCart += quantity + "x\t\t" + "$" + (item.getPrice() * quantity) + "\t\t" + item.getId() + "\t" + item.getName()+ "\n";
        });
        this.shoppingCart += "Total: " + "$" + this.order.getTotal();
        return this.shoppingCart;
    }

    /*
     * Function: addItemToOrder()
     * Inputs: an Item and an int
     * For the given item adds the specified quantity to the order
     */
    public void addItemToOrder(Item item, int quantity) {
        Item copy = new Item(item);
        this.order.changeQuantity(copy, quantity);
    }

    /*
     * Function: removeItemFromOrder()
     * Inputs: an Item and an int
     * For the given item removes the specified quantity to the order
     */
    public void removeItemFromOrder(Item item, int quantity) {
        Item copy = new Item(item);
        this.order.changeQuantity(copy, quantity);
    }

    /*
     * Function: promptAddItemToOrder()
     * Prompts the user for an item id and a quantity, and then adds that amount of
     * the item to the order.
     */
    public void promptAddItemToOrder() {
        System.out.println("---------");
        System.out.print("Enter the id for the item you want to order: ");
        String itemId = sc.nextLine();
        // Check if itemId is an integer and a valid itemId (0 < itemId < catalogueSize)
        if (!isInteger(itemId) || Integer.parseInt(itemId) > MenuService.catalogueService.retrieveCatalogueSize()
                || Integer.parseInt(itemId) < 1) {
            System.out.println("--- Please enter a valid item id and a valid quantity ---");
            promptAddItemToOrder();
            return;
        }

        System.out.print("Quantity: ");
        String itemQuantity = sc.nextLine();
        // Check if itemQuantity is an int and greater than 0
        if (!isInteger(itemQuantity) || Integer.parseInt(itemQuantity) < 1) {
            System.out.println("--- Please enter a valid item id and a valid quantity ---");
            promptAddItemToOrder();
        } else {
            if (Integer.parseInt(itemQuantity) > 100) {
                System.out.println(
                        "--- For orders exceeding quantities of 100 units please contact customer support due to limited printer availability ---");
                promptAddItemToOrder();
                return;
            }
            MenuService.orderService.addItemToOrder(MenuService.catalogueService.retrieveItem(Integer.parseInt(itemId) - 1),
                    Integer.parseInt(itemQuantity));
            // Clear the terminal screen
            MenuService.clearScreen();
        }
    }

    /*
     * Function: promptRemoveItemFromOrder()
     * Prompts the user for an item id and a quantity, and then removes that amount
     * of the item from
     * the order. If the quantity exceeds the amount of the item in the order it
     * just removes the item
     * completely from the shopping cart
     */
    public void promptRemoveItemFromOrder() {
        System.out.println("---------");
        System.out.print("Enter the id for the item you want to remove: ");
        String itemId = sc.nextLine();
        //Check if itemId is an integer and a valid itemId (0 < itemId < catalogueSize)
        if (!isInteger(itemId) || Integer.parseInt(itemId) > MenuService.catalogueService.retrieveCatalogueSize() || Integer.parseInt(itemId) < 1) {
            System.out.println("--- Please enter a valid item id and a valid quantity ---");
            promptRemoveItemFromOrder();
            return;
        }

        System.out.print("Quantity to remove: ");
        String itemQuantity = sc.nextLine();
        //Check if itemQuantity is an int and greater or equal to 0
        if (!isInteger(itemQuantity) || Integer.parseInt(itemQuantity) < 0) {
            System.out.println("--- Please enter a valid item id and a valid quantity to remove ---");
            promptRemoveItemFromOrder();
        } else {
            MenuService.orderService.removeItemFromOrder(MenuService.catalogueService.retrieveItem(Integer.parseInt(itemId) - 1), -Integer.parseInt(itemQuantity));
            System.out.println("---------");
            // Clear the terminal screen
            MenuService.clearScreen();            
        }
    }

    /*
     * Function: inInteger()
     * Input: String
     * Returns true if the String represents an integer, else false
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}
