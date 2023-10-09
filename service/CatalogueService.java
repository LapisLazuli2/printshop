package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import pojo.Catalogue;
import pojo.Item;

public class CatalogueService {
    private Catalogue catalogueService = new Catalogue();

    /*
     * Constructor
     */
    public CatalogueService() {
    }

    /*
     * Function: addItem()
     * Input: Item object
     * Adds a copy of the given Item to the Catalogue object
     */
    public void addItem(Item item) {
        this.catalogueService.addItem(new Item(item));
    }

    /*
     * Function: retrieveItem()
     * Input: int reprenting the id of an Item
     * Returns a copy of the Item with the given item id
     */
    public Item retrieveItem(int id) {
        return new Item(this.catalogueService.getItem(id));
    }

    /*
     * Function retrieveAllItem()
     * Returns a List<Item> of all Item in the catalogueService
     */
    public List<Item> retrieveAllItem() {
        List<Item> items = new ArrayList<>();
        for (Item item : catalogueService) {
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
        return this.catalogueService.getSize();
    }

    /*
     * Function: displayCatalogue()
     * Loops through the catalogue and creates a string representation of each item,
     * and then returns a string containing all the items
     */
    public String displayCatalogue() {
        String wholeCatalogue = "ID\tPrice\tProdTime\tName\n";
        for (Item item : this.catalogueService) {
            wholeCatalogue += item.getId() + "\t" + "$" + item.getPrice() + "\t" + item.getProductionTimeHours()
                    + "\t\t" + item.getName() + "\n";
        }
        return wholeCatalogue;
    }

    /*
     * Function: loadCatalogue()
     * Input: a string with the relative file path pointing to a csv file with the
     * shop catalogue
     * Parses the specified csv file containing the information on the shop
     * catalogue, creates Item objects based on that information, and adds
     * the Items to a Catalogue object via the catalogueService.
     */
    public void loadCatalogue(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Files.lines(path)
                .forEach(line -> {
                    String[] elements = line.split(";");
                    this.addItem(new Item(Integer.parseInt(elements[0]), elements[1],
                            Double.parseDouble(elements[2]), LocalTime.parse(elements[3], formatter)));
                });
    }
}
