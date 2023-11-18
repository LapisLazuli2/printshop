package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.google.gson.Gson;

import pojo.Invoice;
import pojo.InvoiceJson;
import pojo.Item;
import pojo.Order;
import pojo.User;
import repository.InvoiceRepository;

public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    /*
     * Constructor
     */
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /*
     * Function: createInvoice()
     * Input: Order object, User object
     * Initializes this.invoice with the order and user information
     */
    public void createInvoice(Order order, User user) {
        this.invoiceRepository.createInvoice(order, user);
    }

    /*
     * Function: retrieveInvoice()
     * Returns a copy of this.invoice
     */
    public Invoice retrieveInvoice() {
        return this.invoiceRepository.retrieveInvoice();
    }

    /*
     * Function: setPickUpDateTime()
     * Input: a LocalDateTime representing when the order is completed and can be
     * picked up
     */
    public void setPickUpDateTime(LocalDateTime pickUpDateTime) {
        this.invoiceRepository.setPickUpDateTime(pickUpDateTime);
    }

    /*
     * Function: writeInvoiceToJsonFile()
     * Input: an Invoice object
     * Creates a new json file, converts Invoice to a json object, and stores it in the file
     */
    public void writeInvoiceToJsonFile() throws IOException {
        // Create new file with the name in yyyymmddhhmm.json format where the name
        // is based on the pick up datetime of the invoice
        Invoice invoice = this.invoiceRepository.retrieveInvoice();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmm");
        String fileName = "data/invoices/" + invoice.getPickUpDateTime().format(formatter) + ".json";
        File file = new File(fileName);
        file.createNewFile();

        //Convert Invoice to an InvoiceJson object that replaces all LocalDateTime fields with String fields,
        //due to gson throwing errors when converting LocalDatetime fields to json
        InvoiceJson invoiceJson = new InvoiceJson(invoice);
        
        //Convert InvoiceJson to a json object and write it to the file
        Gson gson = new Gson();
        String invoiceInJsonFormat = gson.toJson(invoiceJson);
        FileWriter writer = new FileWriter(file.getPath());
        writer.append(invoiceInJsonFormat);
        writer.close();
    }

    /*
     * Function: displayInvoiceText()
     * Creates a string representing an Invoice that contains customer info, order
     * info, total price to pay, and pickup time.
     */
    public String displayInvoiceText() {
        Invoice invoice = this.invoiceRepository.retrieveInvoice();

        //Header
        String invoiceText = "--- Invoice ---\n";

        //Completion time
        LocalDateTime completionDateTime = invoice.getCompletionTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        invoiceText += "Order placed at: " + completionDateTime.format(formatter) + "\n";

        //Production time
        String productionTime = "Production time: " + invoice.getOrder().getProductionTimeHours() + " hours"
                + "\n";
        invoiceText += productionTime;

        //Pickup time
        LocalDateTime pickUpDateTime = invoice.getPickUpDateTime();
        invoiceText += "Order Pick Up Time: " + pickUpDateTime.format(formatter) + "\n";

        //Customer information
        String customerInformation = "-- Customer Information --\n";
        customerInformation += invoice.getUser().getFirstName() + " " + invoice.getUser().getLastName() +
                "\n" + invoice.getUser().getAddress() + ", " + invoice.getUser().getPostcode() + "\n" +
                invoice.getUser().getPhoneNumber() + "\n" + invoice.getUser().getEmail() + "\n";

        //Order information
        String orderInformation = "-- Order --\nQuantity\tSubtotal\tItem\n";
        for (Map.Entry<Item, Integer> set : invoice.getOrder().getOrder().entrySet()) {
            orderInformation += set.getValue() + "x\t\t" + "$" + (set.getKey().getPrice() * set.getValue()) +
                    "\t\t" + set.getKey().getName() + "\n";
        }
        orderInformation += "Total: $" + invoice.getOrder().getTotal();
        
        return invoiceText + customerInformation + orderInformation;
    }
}
