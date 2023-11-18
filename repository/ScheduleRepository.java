package repository;

import java.time.LocalDateTime;

import pojo.Schedule;

public class ScheduleRepository {
    private Schedule schedule;

    /*
     * Constructor
     */
    public ScheduleRepository(){
        this.schedule = new Schedule();
    }

    /*
     * Function: getNextAvailableTime()
     * Returns the nextAvailableTime from Schedule
     */
    public LocalDateTime getNextAvailableTime() {
        return this.schedule.getNextAvailableTime();
    }

    /*
     * Function: setNextAvailableTime()
     * Input: a LocalDateTime representing when the printers will be available
     * Sets the nextAvailableTime in Schedule
     */
    public void setNextAvailableTime(LocalDateTime dateTime) {
        this.schedule.setNextAvailableTime(dateTime);
    }

}
