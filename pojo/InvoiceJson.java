package pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class InvoiceJson {
    private User user;
    private OrderJson order;
    private String completionDateTime;
    private String pickUpDateTime;

    /*
     * Constructor
     * Creates an InvoiceJson object from an Invoice object where the
     * completionDateTime and pickUpDateTime are Strings instead of LocalDateTime
     * objects. This is done to make it easier to convert the Invoice to a json 
     * since gson throws errors when converting LocalDateTime objects
     */
    public InvoiceJson(Invoice source) {
        this.user = new User(source.getUser());
        this.order = new OrderJson(source.getOrder());
        this.setCompletionDateTime(this.localDateToString(source.getCompletionTime()));
        this.setPickUpDateTime(this.localDateToString(source.getPickUpDateTime()));
    }

    /*
     * Function: localDateToString()
     * Input: LocalDateTime
     * Output: String
     * Converts the given LocalDateTime to a String representation of that date 
     */
    private String localDateToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return localDateTime.format(formatter);
    }

    public void setCompletionDateTime(String completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    public void setPickUpDateTime(String pickUpDateTime) {
        this.pickUpDateTime = pickUpDateTime;
    }

    /*
     * Function: displayInvoiceText()
     * Output: String
     * Creates a string representing an Invoice that contains customer info, order
     * info, total price to pay, and pickup time.
     */
    public String toString() {
        String invoiceText = "\n--- Invoice ---\n";
        invoiceText += "Order placed at: " + this.completionDateTime + "\n";
        String productionTime = "Production time: " + this.order.getProductionTimeHours() + " hours" + "\n";
        invoiceText += productionTime;
        invoiceText += "Order Pick Up Time: " + this.pickUpDateTime + "\n";
        String customerInformation = "-- Customer Information --\n";
        customerInformation += this.user.getFirstName() + " " + this.user.getLastName() +
                "\n" + this.user.getAddress() + ", " + this.user.getPostcode() + "\n" +
                this.user.getPhoneNumber() + "\n" + this.user.getEmail() + "\n";
        String orderInformation = "-- Order --\nQuantity\tItem\n";

        for (Map.Entry<String, Integer> set : this.order.getOrder().entrySet()) {
            orderInformation += set.getValue() + "x\t\t" + set.getKey() + "\n";
        }

        orderInformation += "Total: $" + this.order.getTotal() + "\n";
        return invoiceText + customerInformation + orderInformation;
    }
}
