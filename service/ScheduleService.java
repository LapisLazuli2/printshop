package service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import repository.ScheduleRepository;

public class ScheduleService {

    private ScheduleRepository scheduleRepository;

    /*
     * Constructor
     */
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /*
     * Function: getNextAvailableTime()
     * Returns the nextAvailableTime from Schedule
     */
    public LocalDateTime getNextAvailableTime() {
        return this.scheduleRepository.getNextAvailableTime();
    }

    /*
     * Function: setNextAvailableTime()
     * Input: a LocalDateTime representing when the printers will be available
     * Sets the nextAvailableTime in Schedule
     */
    public void setNextAvailableTime(LocalDateTime dateTime) {
        this.scheduleRepository.setNextAvailableTime(dateTime);
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
        if (now.isAfter(nextAvailableTime))
            nextAvailableTime = now;

        while (!isValidWorkingDateTime(nextAvailableTime))
            nextAvailableTime = setValidWorkDateTime(nextAvailableTime);

        return nextAvailableTime;
    }

    public boolean isValidWorkingDateTime(LocalDateTime dateTime) {
        if (!isValidWorkingDate(dateTime.toLocalDate())
                || !isValidWorkingTime(dateTime.toLocalTime(), dateTime.getDayOfWeek())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidWorkingDate(LocalDate date) {
        // Within the OpeningHours csv days off are indicated by having a start time of
        // 00:00 and closing time of 00:00. Thus this function checks if the date giving
        // is a day off.

        // Get day of week for date
        DayOfWeek day = date.getDayOfWeek();

        // Get opening and closing times for that day
        LocalTime startTime = MenuService.openingTimesService.getStartTime(day);
        LocalTime closingTime = MenuService.openingTimesService.getClosingTime(day);

        // Check if that day is a day off
        if (startTime.getHour() == 0 && startTime.getMinute() == 0
                && closingTime.getHour() == 0 && closingTime.getMinute() == 0)
            return false;
        else
            return true;
    }

    public boolean isValidWorkingTime(LocalTime time, DayOfWeek day) {
        LocalTime startTime = MenuService.openingTimesService.getStartTime(day);
        LocalTime closingTime = MenuService.openingTimesService.getClosingTime(day);

        // invalid opening times are time < opening time or time >= closing time
        if (time.isBefore(startTime) || time.isAfter(closingTime) || time.equals(closingTime))
            return false;
        else
            return true;
    }

    public LocalDateTime setValidWorkDateTime(LocalDateTime dateTime) {
        // Input: a dateTime is that is not valid according to isValidWorkingDateTime()
        // Implication: the dateTime's time is either before the opening time of the
        // shop, after the closing time, or the date is on a day off
        // Cases: time before starting time -> move to starting time
        // time after end time -> move to next day
        // day off -> move to next day
        LocalTime startTime = MenuService.openingTimesService.getStartTime(dateTime.getDayOfWeek());
        if (dateTime.toLocalTime().isBefore(startTime)) {
            // dateTime with startime of current day
            dateTime = dateTime.withHour(startTime.getHour()).withMinute(startTime.getMinute());
            return dateTime;
        } else {
            // return datetime on next day and with start time of that day
            dateTime = dateTime.plusDays(1);
            startTime = MenuService.openingTimesService.getStartTime(dateTime.getDayOfWeek());
            dateTime = dateTime.withHour(startTime.getHour()).withMinute(startTime.getMinute());
            return dateTime;
        }
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
