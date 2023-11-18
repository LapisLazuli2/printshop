package service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import pojo.InvoiceJson;
import repository.CatalogueRepository;
import repository.InvoiceRepository;
import repository.OpeningTimeRepository;
import repository.OrderRepository;
import repository.ScheduleRepository;
import repository.UserRepository;

public class MenuService {
    public static CatalogueService catalogueService;
    public static OrderService orderService;
    public static OpeningTimesService openingTimesService;
    public static ScheduleService scheduleService;
    private UserService userService;
    private InvoiceService invoiceService;
    private Scanner sc;

    public MenuService() {
        catalogueService = new CatalogueService(new CatalogueRepository());
        orderService = new OrderService(new OrderRepository());
        openingTimesService = new OpeningTimesService(new OpeningTimeRepository());
        scheduleService = new ScheduleService(new ScheduleRepository());
        this.userService = new UserService(new UserRepository());
        this.invoiceService = new InvoiceService(new InvoiceRepository());
        this.sc = new Scanner(System.in);
        this.loadFiles();
    }

    private void loadFiles() {
        try {
            // Initialize the store catalogue from the csv file
            catalogueService.loadCatalogue("data/Printshop_PriceList.csv");

            // Initialize the opening times from the csv file
            openingTimesService.loadOpeningTimes("data/Printshop_OpeningHours.csv");

            // Initialize the LocalDateTime for when the printers are available for printing
            scheduleService.loadSchedule("data/Printshop_Schedule.csv");

            // Update when the printers are available again incase the previously registered
            // time was in the past
            LocalDateTime prevTime = scheduleService.getNextAvailableTime();
            LocalDateTime updatedTime = scheduleService.updateNextAvailableTime(prevTime);
            scheduleService.setNextAvailableTime(updatedTime);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void displayMenu() {
        // Clear the terminal screen
        clearScreen();

        // Display the user interface
        System.out.println(orderService.displayIntroduction());
        System.out.println(catalogueService.displayCatalogue());
        System.out.print(orderService.displayOrderInterface());

        // Prompt the user to input a store command
        parseOrderUserInput(sc.nextLine());
    }

    /*
     * Function: clearScreen()
     * Clears the text in the terminal screen
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /*
     * Function: parserOrderUserInput()
     * Input: a string containing the user's input retrieved from a Scanner
     * Handles the user input based on the available shop commands:
     * add remove checkout review exit
     * If the user enters a non-command an error message will be displayed and the
     * user will be prompted again for an input
     */
    public void parseOrderUserInput(String input) {
        switch (input) {
            case "add":
                // Call the UI for adding an item to the order
                orderService.promptAddItemToOrder(sc);

                // Display the catalogue, shopping cart, and prompt the user for what to do next
                System.out.println(catalogueService.displayCatalogue());
                System.out.println(orderService.displayShoppingCart());
                System.out.println("Would you like to add another item? add | remove | checkout | exit");
                System.out.print(">>");
                parseOrderUserInput(sc.nextLine());
                break;
            case "remove":
                // Call the UI for removing an item from the order
                orderService.promptRemoveItemFromOrder(sc);

                // Display the catalogue, shopping cart, and prompt the user for what to do next
                System.out.println(catalogueService.displayCatalogue());
                System.out.println(orderService.displayShoppingCart());
                System.out.println("Would you like to continue further? add | remove | checkout | exit");
                System.out.print(">>");
                parseOrderUserInput(sc.nextLine());
                break;
            case "checkout":
                startCheckOutProcess();
                break;
            case "review":
                reviewPreviousOrders();
                System.out.println("Press enter to return to the shop screen");
                sc.nextLine();
                
                // Display catalogue, shopping cart, and prompt user for their choice
                clearScreen();
                System.out.println(orderService.displayIntroduction());
                System.out.println(catalogueService.displayCatalogue());
                System.out.print(orderService.displayOrderInterface());
                parseOrderUserInput(sc.nextLine());
                break;
            case "exit":
                System.out.println("Thank you for using our service. Have a nice day!");
                System.exit(0);
                break;
            default:
                // When the user inputs something that is not add remove checkout exit the user
                // will be prompted again
                System.out.println("*** Invalid Command: Please use add | remove | checkout | exit ***");
                System.out.print(">>");
                parseOrderUserInput(sc.nextLine());
                break;
        }
    }

    public void reviewPreviousOrders() {
        // Clear the terminal screen
        clearScreen();

        // Loop through all invoices in data/invoices, the jsons to strings,
        // convert the strings to InvoiceJson objects, then print each invoice
        System.out.println("--- Invoices ---");
        File dir = new File("data/invoices");
        File[] listOfFiles = dir.listFiles();
        String filePath = "";
        String jsonAsString = "";
        for (File file : listOfFiles) {
            filePath = file.toString();
            Path path = Paths.get(filePath);
            try {
                jsonAsString = Files.lines(path)
                        .map(line -> line.toString())
                        .collect(Collectors.joining(""));
                Gson gson = new Gson();
                InvoiceJson invoiceJson = gson.fromJson(jsonAsString, InvoiceJson.class);
                System.out.println(invoiceJson);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void startCheckOutProcess() {
        // Clear the terminal screen
        clearScreen();

        // Call the UI for the checkout process
        // Fill in user information if user has not been created yet
        if (userService.isUserCreated() == false) {
            userService.promptCreateNewUser(sc);
        }

        // Create invoice with order and user info
        invoiceService.createInvoice(orderService.retrieveOrder(), userService.retrieveUser());

        // Notify the user from which day the printers are available again
        // incase there have been previously placed orders
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        System.out.println("---  Notice: the printers are next available from "
                + scheduleService.getNextAvailableTime().format(formatter) + " ---");

        // Calculate how long the order will take to produce and pick up time
        int productionTimeMinutes = orderService.retrieveOrderProductionTimeMinutes();
        LocalDateTime pickUpDateTime = scheduleService.calculatePickUpTime(productionTimeMinutes);
        invoiceService.setPickUpDateTime(pickUpDateTime);

        // Display the invoice
        System.out.println(invoiceService.displayInvoiceText());

        // Confirmation for placing the order or changing order/user info
        System.out.println("--- Confirmation ---");
        System.out.println(
                "Available commands\nPlace order: confirm\nGo back to shop: shop\nChange personal info: info\nCancel order: exit");
        System.out.print(">>");
        confirmInvoice(sc.nextLine());
    }

    /*
     * Function: confirmInvoice()
     * Input: a string containing the user's input retrieved from a Scanner
     * Handles the user input based on the invoice confirmation screen commands:
     * confirm shop info exit
     * If the user enters a non-command an error message will be displayed and the
     * user will be prompted again for an input
     */
    public void confirmInvoice(String input) {
        switch (input) {
            case "confirm":
                // Write the invoice to a json file
                // Write the time for when the printers will be available again to
                // Printshop_Schedule.csv
                try {
                    invoiceService.writeInvoiceToJsonFile();
                    scheduleService.writeNextAvailableTimeToFile("data/Printshop_Schedule.csv");
                } catch (Exception e) {
                    System.out.println(e);
                }

                System.out.println("Thank you for shopping with us!");
                break;
            case "shop":
                // Clear the terminal screen
                clearScreen();
                // Display catalogue, shopping cart, and prompt user for their choice
                System.out.println(catalogueService.displayCatalogue());
                System.out.println(orderService.displayShoppingCart());
                System.out.println("Would you like to add/remove another item? add | remove | checkout | exit");
                System.out.print(">>");
                parseOrderUserInput(sc.nextLine());
                break;
            case "info":
                // Clear the terminal screen
                clearScreen();
                // Overwrite user with new user info
                userService.promptCreateNewUser(sc);

                // Go back to checkout screen
                parseOrderUserInput("checkout");
                break;
            case "exit":
                System.out.println("Thank you for using our service. Have a nice day!");
                System.exit(0);
                break;
            default:
                // When the user inputs something that is not confirm shop info exit the user
                // will be prompted again
                System.out.println("*** Invalid Command: Please use confirm | shop | info | exit ***");
                System.out.print(">>");
                confirmInvoice(sc.nextLine());
                break;
        }
    }
}
