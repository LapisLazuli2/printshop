package pojo;

import java.util.HashMap;
import java.util.Map;

public class OrderJson {
    private Map<String, Integer> order; // represents a list of items mapped to the quantity of those items in the order
    private double total; // the sum of price*quantity of all items within this.order
    private int productionTimeMinutes; // reprents the total time to produce the order in minutes
    private int productionTimeHours; // represents the total time to produce the order in hours

    /*
     * Constructor
     * Converts a Order object into a OrderJson object that has a
     * Map<String,Integer> field instead of a Map<Item,Integer> field. This is
     * due to Item objects having LocalHour fields, which makes gson throw errors
     * when attemping to convert an object to a json object
     */
    public OrderJson(Order source) {
        this.order = new HashMap<>();
        source.getOrder().forEach((Item item, Integer quantity) -> this.order.put(item.getName(), quantity));
        this.total = source.getTotal();
        this.productionTimeMinutes = source.getProductionTimeMinutes();
        this.productionTimeHours = source.getProductionTimeHours();
    }

    public Map<String, Integer> getOrder() {
        return this.order;
    }

    public void setOrder(Map<String, Integer> order) {
        this.order = order;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getProductionTimeMinutes() {
        return this.productionTimeMinutes;
    }

    public void setProductionTimeMinutes(int productionTimeMinutes) {
        this.productionTimeMinutes = productionTimeMinutes;
    }

    public int getProductionTimeHours() {
        return this.productionTimeHours;
    }

    public void setProductionTimeHours(int productionTimeHours) {
        this.productionTimeHours = productionTimeHours;
    }

}
