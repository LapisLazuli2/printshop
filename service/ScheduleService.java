package service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import pojo.Schedule;

public class ScheduleService {
    private Schedule schedule;

    /*
     * Constructor
     */
    public ScheduleService() {
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

    /*
     * Function: isValidDateNextDay()
     * Input: LocalDateTime
     * Returns true if adding +1 day to the LocalDateTime produces a valid date,
     * else false
     * E.g. September 30 + 1 = September 31 -> false
     * September 29 + 1 = September 30 -> true
     */
    public boolean isValidDateNextDay(LocalDateTime localDateTime) {
        try {
            localDateTime.withDayOfMonth(localDateTime.getDayOfMonth() + 1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Function: isValidDateNextMonth()
     * Input: LocalDateTime
     * Returns true if adding +1 month to the LocalDateTime produces a valid date,
     * else false
     * E.g. 12 (December) + 1 month = 13 (non-existant month) -> false
     * 11 (November) + 1 month = 12 (December) -> true
     */
    public boolean isValidDateNextMonth(LocalDateTime localDateTime) {
        try {
            localDateTime.withMonth(localDateTime.getMonthValue() + 1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Function: writeNextAvailableTimeToFile()
     * Input: a String representing a relative file path to a csv contaning
     * the next LocalDateTime for when the printer will be available
     * The function gets the nextAvailableTime and writes it to the
     * specified file
     */
    public void writeNextAvailableTimeToFile(String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.append(this.getNextAvailableTime().toString());
        writer.close();
    }

    /*
     * Function: loadSchedule()
     * Input: a string with the relative file path pointing to a csv file specifying
     * the LocalDateTime for
     * when the printers are available again
     * Parses the specified csv file and loads it into a Schedule object via the
     * SchduleService
     */
    public void loadSchedule(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        Files.lines(path)
                .forEach(line -> {
                    this.setNextAvailableTime(LocalDateTime.parse(line, formatter));
                });
    }

    /*
     * Function: updateNextAvailableTime()
     * Input: a LocalDateTime representing the previously registered next available
     * time
     * This function takes a nextAvailableTime and based on the time right now and
     * the store's opening hours checks if the nextAvailableTime needs to be
     * updated to a new time.
     * E.g. nextAvailableTime was 1 Sep 10:31AM, but now is 2 Sep 15:01, so
     * updateNextAvailable time to 2 Sep 15:01
     */
    public LocalDateTime updateNextAvailableTime(LocalDateTime nextAvailableTime) {
        // Calculate time now
        LocalDateTime now = LocalDateTime.now();

        // Compare if the next available time is in the future or is right now
        // If now is after the next available time, update the next available time to be
        // now
        // Otherwise the next available time is in the future, so leave it as is
        if (now.isAfter(nextAvailableTime)) {
            nextAvailableTime = now;
        }

        // Get day of week for nextAvailableTime
        DayOfWeek day = nextAvailableTime.getDayOfWeek();

        // Get opening and closing times for that day
        LocalTime startTime = MenuService.openingTimesService.getStartTime(day);
        LocalTime closingTime = MenuService.openingTimesService.getClosingTime(day);

        // Check if the hour of nextAvailableTime is past the hour of that working day's
        // start time
        // Examples: assume start time is 9:30 and closing time is 17:30
        // if the hour of next available time is 8:00 -> less than starttime -> update
        // nexttime to starttime
        // 9:01 -> equal to starttime hour but less than starttime minute -> update the
        // minutes of nexttime
        // 9:31 -> equal to starttime hour but more than starttime minute -> leave as is
        // 10:00 -> more than starttime -> this is fine, leave as is
        // 17:00 -> more than starrtime, equal to endtime hour but less than endtime
        // minute -> leave as is
        // 17:31 -> more than starttime, equal to endtime hour and greater than endtime
        // minute -> update nexttime to next working day with that day's starttime
        // 20:00 -> more than starttime and more than endtime -> update nexttime to next
        // working day with that day's starttime
        if (nextAvailableTime.getHour() < startTime.getHour()) {
            // The time now is before the opening time, so update the next available time to
            // be at the opening hour
            nextAvailableTime = nextAvailableTime.withHour(startTime.getHour());
            nextAvailableTime = nextAvailableTime.withMinute(startTime.getMinute());
        } else if ((nextAvailableTime.getHour() == startTime.getHour()
                && nextAvailableTime.getMinute() < startTime.getMinute())) {
            // The time now is the same hour as opening time but the minutes are before
            // opening time,
            // so update the minutes of available time to the minutes of the opening time
            nextAvailableTime = nextAvailableTime.withMinute(startTime.getMinute());
        } else if (nextAvailableTime.getHour() == closingTime.getHour()
                && nextAvailableTime.getMinute() >= closingTime.getMinute()) {
            // Change next available time to next day
            // Check if next day is valid date
            // Check if adding +1 day to the nextAvailable time produces an invalid day
            // (September 31)
            // or invalid month (Month #13), then update nextAvailable time to a valid day
            // and month
            if (MenuService.scheduleService.isValidDateNextDay(nextAvailableTime)) {
                nextAvailableTime = nextAvailableTime.withDayOfMonth(nextAvailableTime.getDayOfMonth() + 1);
            } else {
                if (MenuService.scheduleService.isValidDateNextMonth(nextAvailableTime)) {
                    nextAvailableTime = nextAvailableTime.withMonth(nextAvailableTime.getMonthValue() + 1);
                    nextAvailableTime = nextAvailableTime.withDayOfMonth(1);
                } else {
                    nextAvailableTime = nextAvailableTime.withYear(nextAvailableTime.getYear() + 1);
                    nextAvailableTime = nextAvailableTime.withMonth(1);
                    nextAvailableTime = nextAvailableTime.withDayOfMonth(1);
                }
            }

            // nextAvailableTime =
            // nextAvailableTime.withDayOfMonth(nextAvailableTime.getDayOfMonth() + 1);
            // Change next available time hours and minutes to the starting time of of next
            // day
            day = nextAvailableTime.getDayOfWeek();
            startTime = MenuService.openingTimesService.getStartTime(day);
            nextAvailableTime = nextAvailableTime.withHour(startTime.getHour());
            nextAvailableTime = nextAvailableTime.withMinute(startTime.getMinute());
        } else if (nextAvailableTime.getHour() > closingTime.getHour()) {
            // Change next available time to next day
            // Check if next day is valid date
            // Check if adding +1 day to the nextAvailable time produces an invalid day
            // (September 31)
            // or invalid month (Month #13), then update nextAvailable time to a valid day
            // and month
            if (MenuService.scheduleService.isValidDateNextDay(nextAvailableTime)) {
                nextAvailableTime = nextAvailableTime.withDayOfMonth(nextAvailableTime.getDayOfMonth() + 1);
            } else {
                if (MenuService.scheduleService.isValidDateNextMonth(nextAvailableTime)) {
                    nextAvailableTime = nextAvailableTime.withMonth(nextAvailableTime.getMonthValue() + 1);
                    nextAvailableTime = nextAvailableTime.withDayOfMonth(1);
                } else {
                    nextAvailableTime = nextAvailableTime.withYear(nextAvailableTime.getYear() + 1);
                    nextAvailableTime = nextAvailableTime.withMonth(1);
                    nextAvailableTime = nextAvailableTime.withDayOfMonth(1);
                }
            }

            // Change next available time hours and minutes to the starting time of of next
            // day
            day = nextAvailableTime.getDayOfWeek();
            startTime = MenuService.openingTimesService.getStartTime(day);
            nextAvailableTime = nextAvailableTime.withHour(startTime.getHour());
            nextAvailableTime = nextAvailableTime.withMinute(startTime.getMinute());
        }

        // Within the csv days off are indicated by having a start time of 00:00 and
        // closing time of 00:00. So check if the next available time was updated
        // to a day off, and then updates it to the next day if so

        // Get day of week for nextAvailableTime
        day = nextAvailableTime.getDayOfWeek();

        // Get opening and closing times for that day
        startTime = MenuService.openingTimesService.getStartTime(day);
        closingTime = MenuService.openingTimesService.getClosingTime(day);

        // Check if that day is a day off
        if (nextAvailableTime.getHour() == startTime.getHour()
                && nextAvailableTime.getHour() == closingTime.getHour()
                && nextAvailableTime.getMinute() == startTime.getMinute()
                && nextAvailableTime.getMinute() == closingTime.getMinute()) {
            // Change next available time to next day
            // Check if next day is valid date
            // Check if adding +1 day to the nextAvailable time produces an invalid day
            // (September 31)
            // or invalid month (Month #13), then update nextAvailable time to a valid day
            // and month
            if (MenuService.scheduleService.isValidDateNextDay(nextAvailableTime)) {
                nextAvailableTime = nextAvailableTime.withDayOfMonth(nextAvailableTime.getDayOfMonth() + 1);
            } else {
                if (MenuService.scheduleService.isValidDateNextMonth(nextAvailableTime)) {
                    nextAvailableTime = nextAvailableTime.withMonth(nextAvailableTime.getMonthValue() + 1);
                    nextAvailableTime = nextAvailableTime.withDayOfMonth(1);
                } else {
                    nextAvailableTime = nextAvailableTime.withYear(nextAvailableTime.getYear() + 1);
                    nextAvailableTime = nextAvailableTime.withMonth(1);
                    nextAvailableTime = nextAvailableTime.withDayOfMonth(1);
                }
            }

            // Change next available time hours and minutes to the starting time of of next
            // day
            day = nextAvailableTime.getDayOfWeek();
            startTime = MenuService.openingTimesService.getStartTime(day);
            nextAvailableTime = nextAvailableTime.withHour(startTime.getHour());
            nextAvailableTime = nextAvailableTime.withMinute(startTime.getMinute());
        }

        return nextAvailableTime;
    }

    /*
     * Function: calculatePickUpTime()
     * Input: an int representing how many minutes it takes to produce an order
     * Returns a LocalDateTime representing when the user can pick up the order
     * based on the printer's availability
     * and how long the production time of the order is
     */
    public LocalDateTime calculatePickUpTime(int productionTimeMinutes) {
        // Get the date time for when the printers are available to work
        LocalDateTime productionStartTime = MenuService.scheduleService.getNextAvailableTime();

        // Get the closing time for that date
        DayOfWeek workingDay = productionStartTime.getDayOfWeek();
        LocalTime workingDayClosingTime = MenuService.openingTimesService.getClosingTime(workingDay);

        // While the productionStartTime hour and minutes are less than the closing time
        // hour and minutes
        // Subtract minutes from the production time and add them to the
        // productionStartTime
        while (productionTimeMinutes > 0
                && (productionStartTime.getHour() < workingDayClosingTime.getHour()
                        || (productionStartTime.getHour() == workingDayClosingTime.getHour()
                                && productionStartTime.getMinute() < workingDayClosingTime.getMinute()))) {
            productionTimeMinutes -= 1;
            productionStartTime = productionStartTime.plusMinutes(1);
        }

        // Update the printer's next available time to the next valid working day
        MenuService.scheduleService.setNextAvailableTime(updateNextAvailableTime(productionStartTime));

        // Return the pick up time when the production time reaches 0
        // Else run the function again until production time reaches 0
        if (productionTimeMinutes < 1) {
            return productionStartTime;
        } else {
            return calculatePickUpTime(productionTimeMinutes);
        }
    }
}
