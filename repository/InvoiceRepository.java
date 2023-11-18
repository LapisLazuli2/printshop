package repository;

import java.time.LocalDateTime;

import pojo.Invoice;
import pojo.Order;
import pojo.User;

public class InvoiceRepository {
    private Invoice invoice;

    public InvoiceRepository() {
    }

    /*
     * Function: createInvoice()
     * Input: Order object, User object
     * Initializes this.invoice with the order and user information
     */
    public void createInvoice(Order order, User user) {
        this.invoice = new Invoice(order, user);
    }

    /*
     * Function: retrieveInvoice()
     * Returns a copy of this.invoice
     */
    public Invoice retrieveInvoice() {
        return new Invoice(this.invoice);
    }

    /*
     * Function: setPickUpDateTime()
     * Input: a LocalDateTime representing when the order is completed and can be
     * picked up
     */
    public void setPickUpDateTime(LocalDateTime pickUpDateTime) {
        this.invoice.setPickUpDateTime(pickUpDateTime);
    }
}
