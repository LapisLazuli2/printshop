package pojo;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private Map<Item, Integer> order; //represents a list of items mapped to the quantity of those items in the order
    private double total; //the sum of price*quantity of all items within this.order
    private int productionTimeMinutes; // reprents the total time to produce the order in minutes
    private int productionTimeHours; // reprents the total time to produce the order in hours

    /*
     * Constructor
     */
    public Order(){
        this.order = new HashMap<>();
    }

    /*
     * Copy constructor
     */
    public Order(Order source){
        this.order = new HashMap<>(source.order);
        this.total = source.total;
    }
    
    /*
     * Function: getOrder()
     * returns a copy of this.order
     */
    public Map<Item,Integer> getOrder(){
        Map<Item, Integer> copy = new HashMap<>(this.order);
        return copy;
    }
    
    /*
     * Function: addItem()
     * Input: an Item and a quantity > 0
     * Adds the specified item with the specified quantity to this.order. If quantity is 0 or less 
     * then it does not add it to the order.
     */
    public void addItem(Item item, int quantity){
        if (quantity > 0) {
            Item copy = new Item(item); 
            this.order.put(copy, this.order.getOrDefault(copy, 0) + quantity);
        } 
    }

    /*
     * Function removeItem()
     * Input: an Item
     * Removes the specified item from the order
     */
    public void removeItem(Item item){
        this.order.remove(item);
    }

     /*
     * Function getQuantity()
     * Input: an Item
     * Returns the quantity mapped to the given item within the order
     */
    public int getQuantity(Item item){
        return this.order.get(item);
    }

     /*
     * Function: getTotal()
     * Sums the price * quantity of every item currently in the order and then returns the total
     */
    public double getTotal(){
        this.total = 0;
        this.order.forEach((Item item, Integer quantity) -> this.total += item.getPrice() * quantity);
        return this.total;
    }

    /*
     * Function: getProductionTimeMinutes
     * Sums the quantity * productionTimeHours * 60 (hours to minutes) of every item currently in the order and then returns the total
     */
    public int getProductionTimeMinutes(){
        this.productionTimeMinutes = 0;
        this.order.forEach((Item item, Integer quantity) -> this.productionTimeMinutes += item.getProductionTimeHours().getHour() * 60 * quantity);
        return this.productionTimeMinutes;
    }

    /*
     * Function: getProductionTimeHours
     * Sums the quantity * productionTimeHours of every item currently in the order and then returns the total
     */
    public int getProductionTimeHours(){
        this.productionTimeHours = 0;
        this.order.forEach((Item item, Integer quantity) -> this.productionTimeHours += item.getProductionTimeHours().getHour() * quantity);
        return this.productionTimeHours;
    }

    /*
     * Function: changeQuantity
     * Input: an Item object and a positive or negative whole number
     * For the given Item adds or reduces the quantity in the order by the amount specified 
     */
    public void changeQuantity(Item item, int quantity){
        int newQuantity = quantity + this.order.getOrDefault(item, 0);

        if (quantity < 0 && this.order.get(item) == null){
            //the case where you are trying reduce the quantity of an item not in the order
            //Do nothing
        } else if (newQuantity <= 0) {
            //the case where the new quantity is 0 or less: remove the item from the order
            removeItem(item);
        } else {
            //otherwise just update the quantity of the order
            this.order.put(item, newQuantity);
        }
    }

}
