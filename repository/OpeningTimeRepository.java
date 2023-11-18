package repository;

import java.time.DayOfWeek;
import java.time.LocalTime;

import pojo.OpeningTimes;

public class OpeningTimeRepository {
    private OpeningTimes openingTimes;

    public OpeningTimeRepository(){
        this.openingTimes = new OpeningTimes();
    }

    /*
     * Function: addOpeningTime()
     * Input: DayOfWeek, LocalTime, LocalTime representing the shop start time and
     * closing time of that day
     * Adds the day and shop start time and closing time to the openingTimes object
     */
    public void addOpeningTime(DayOfWeek day, LocalTime startTime, LocalTime closingTime) {
        this.openingTimes.addOpeningTime(day, startTime, closingTime);
    }

    /*
     * Function: getStartTime()
     * Input: DayOfWeek
     * Returns the shop opening time for the given day of the week
     */
    public LocalTime getStartTime(DayOfWeek day) {
        return this.openingTimes.getStartTime(day);
    }

    /*
     * Function: getClosingTime()
     * Input: DayOfWeek
     * Returns the shop closing time for the given day of the week
     */
    public LocalTime getClosingTime(DayOfWeek day) {
        return this.openingTimes.getClosingTime(day);
    }
}
