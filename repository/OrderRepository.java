package repository;

import pojo.Item;
import pojo.Order;

public class OrderRepository {

    Order order;

    public OrderRepository(){
        this.order = new Order();
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
     * Function: removeItemFromOrder()
     * Inputs: an Item and an int
     * For the given item removes the specified quantity to the order
     */
    public void removeItemFromOrder(Item item, int quantity) {
        Item copy = new Item(item);
        this.order.changeQuantity(copy, quantity);
    }
}
