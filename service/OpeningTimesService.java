package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import repository.OpeningTimeRepository;

public class OpeningTimesService {

    private OpeningTimeRepository openingTimeRepository;

    /*
     * Constructor
     */
    public OpeningTimesService(OpeningTimeRepository openingTimeRepository) {
        this.openingTimeRepository = openingTimeRepository;
    }

    /*
     * Function: addOpeningTime()
     * Input: DayOfWeek, LocalTime, LocalTime representing the shop start time and
     * closing time of that day
     * Adds the day and shop start time and closing time to the openingTimes object
     */
    public void addOpeningTime(DayOfWeek day, LocalTime startTime, LocalTime closingTime) {
        this.openingTimeRepository.addOpeningTime(day, startTime, closingTime);
    }

    /*
     * Function: getStartTime()
     * Input: DayOfWeek
     * Returns the shop opening time for the given day of the week
     */
    public LocalTime getStartTime(DayOfWeek day) {
        return this.openingTimeRepository.getStartTime(day);
    }

    /*
     * Function: getClosingTime()
     * Input: DayOfWeek
     * Returns the shop closing time for the given day of the week
     */
    public LocalTime getClosingTime(DayOfWeek day) {
        return this.openingTimeRepository.getClosingTime(day);
    }

    /*
     * Function: loadOpeningTimes()
     * Input: a string with the relative file path pointing to a csv file with
     * opening times
     * Parses the specified csv file containing the opening times of the shop,
     * and adds the days, opening times and closing times to an OpeningTimes object
     * via OpeningTimesService
     */
    public void loadOpeningTimes(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Files.lines(path)
                .forEach(line -> {
                    String[] elements = line.split(";");
                    for (DayOfWeek day : DayOfWeek.values()) {
                        if (day.name().equals(elements[1].toUpperCase())) {
                            this.addOpeningTime(day, LocalTime.parse(elements[2], formatter),
                                    LocalTime.parse(elements[3], formatter));
                        }
                    }
                });
    }
}
