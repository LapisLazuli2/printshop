package pojo;

import java.time.LocalDateTime;

public class Invoice {
    private User user;
    private Order order;
    private LocalDateTime completionDateTime; //when the order is placed
    private LocalDateTime pickUpDateTime; //when the order can be picked up

    /*
     * Constructor
     */
    public Invoice(Order order, User user){
        this.order = new Order(order);
        this.user = new User(user);
        this.completionDateTime = LocalDateTime.now();
    }

    /*
     * Copy constructor
     */
    public Invoice(Invoice source){
        this.order = source.order;
        this.user = source.user;
        this.completionDateTime = source.completionDateTime;
        this.pickUpDateTime = source.pickUpDateTime;
    }
    
    public Order getOrder(){
        return new Order(this.order);
    }

    public void setOrder(Order order){
        this.order = new Order(order);
    }
    
    public User getUser(){
        return new User(this.user);
    }

    public void setUser(User user){
        this.user = new User(user);
    }


    public LocalDateTime getCompletionTime(){
        return this.completionDateTime;
    }

    public void setCompletionDateTime(LocalDateTime completionDateTime){
        this.completionDateTime = completionDateTime;
    }

    public LocalDateTime getPickUpDateTime(){
        return this.pickUpDateTime;
    }

    public void setPickUpDateTime(LocalDateTime pickUpDateTime){
        this.pickUpDateTime = pickUpDateTime;
    }
    
}
