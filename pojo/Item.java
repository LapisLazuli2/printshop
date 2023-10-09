package pojo;
import java.time.LocalTime;
import java.util.Objects;

public class Item {
    private int id;
    private String name;
    private double price;
    private LocalTime productionTimeHours; //the number of hours it takes to produce the item

    /*
     * Constructor
     */
    public Item(int id, String name, double price, LocalTime productionTimeHours){
        this.id = id;
        this.name = name;
        this.price = price;
        this.productionTimeHours = productionTimeHours;
    }

    /*
     * Copy constructor
     */
    public Item(Item source){
        this.id = source.id;
        this.name = source.name;
        this.price = source.price;
        this.productionTimeHours = source.productionTimeHours;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public LocalTime getProductionTimeHours() {
        return this.productionTimeHours;
    }  


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
