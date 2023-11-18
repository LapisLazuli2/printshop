package pojo;

import java.util.ArrayList;
import java.util.Iterator;

public class Catalogue implements Iterable<Item> {
    private ArrayList<Item> catalogue;

    /*
     * Constructor
     */
    public Catalogue(){
        this.catalogue = new ArrayList<Item>();
    }

    /*
     * Copy constructor
     */
    public Catalogue(Catalogue source){
        this.catalogue = new ArrayList<Item>();
        for (Item item : source) {
            this.addItem(item);
        }
    }

    /*
     * Function: addItem()
     * Input: Item
     * Adds a copy of the given item to catalogue
     */
    public void addItem(Item item){
        Item copy = new Item(item);
        this.catalogue.add(copy);
    }

    /*
     * Function: getItem()
     * Input: int representing the index where the Item is 
     * Returns a copy of the item at the given index
     */
    public Item getItem(int index){
        Item copy = new Item(this.catalogue.get(index));
        return copy;
    }

    /*
     * Function: getSize()
     * Output: int
     * Returns the size of the catalogue ArrayList
     */
    public int getSize(){
        return this.catalogue.size();
    }
    
    @Override
    public Iterator<Item> iterator(){
        return catalogue.iterator();
    }
}
