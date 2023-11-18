package repository;

import java.util.ArrayList;
import java.util.List;

import pojo.Catalogue;
import pojo.Item;

public class CatalogueRepository {
    private Catalogue catalogue;

    public CatalogueRepository() {
        this.catalogue = new Catalogue();
    }

    /*
     * Function: retrieveCatalogue()
     * Returns a copy of the catalogue
     */

     public Catalogue retrieveCatalogue(){
        return new Catalogue(this.catalogue);
     }
     
    /*
     * Function: addItem()
     * Input: Item object
     * Adds a copy of the given Item to the Catalogue object
     */
    public void addItem(Item item) {
        this.catalogue.addItem(new Item(item));
    }

    /*
     * Function: retrieveItem()
     * Input: int reprenting the id of an Item
     * Returns a copy of the Item with the given item id
     */
    public Item retrieveItem(int id) {
        return new Item(this.catalogue.getItem(id));
    }

    /*
     * Function retrieveAllItem()
     * Returns a List<Item> of all Item in the catalogueService
     */
    public List<Item> retrieveAllItem() {
        List<Item> items = new ArrayList<>();
        for (Item item : this.catalogue) {
            items.add(new Item(item));
        }
        return items;
    }

    /*
     * Function: retrieveCatalogueSize
     * Output: int
     * Returns the size of the catalogue
     */
    public int retrieveCatalogueSize() {
        return this.catalogue.getSize();
    }
}
