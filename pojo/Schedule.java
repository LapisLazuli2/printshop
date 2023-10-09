package pojo;

import java.time.LocalDateTime;

public class Schedule {
    private LocalDateTime nextAvailableTime;

    /*
     * Constructor
     */
    public Schedule(){
    }

    public LocalDateTime getNextAvailableTime(){
        return this.nextAvailableTime;
    }

    public void setNextAvailableTime(LocalDateTime dateTime){
        this.nextAvailableTime = dateTime;
    }
}
