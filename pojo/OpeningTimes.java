package pojo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class OpeningTimes {
    private Map<DayOfWeek, LocalTime[]> openingTimes;

    /*
     * Constructor
     */
    public OpeningTimes(){
        this.openingTimes = new HashMap<>();
    }

    public void addOpeningTime(DayOfWeek day, LocalTime startTime, LocalTime closingTime){
        LocalTime[] times = {startTime, closingTime};
        this.openingTimes.put(day, times);
    }

    public LocalTime getStartTime(DayOfWeek day){
        return this.openingTimes.get(day)[0];
    }

    public LocalTime getClosingTime(DayOfWeek day){
        return this.openingTimes.get(day)[1];
    }
}
