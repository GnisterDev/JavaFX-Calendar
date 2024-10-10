package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DateTimeMapper {
    public static List<int[]> mapToGrid(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        // The first day of the week is Monday
        DayOfWeek firstDayOfWeek = DayOfWeek.MONDAY;

        // List to store the grid positions
        List<int[]> gridMappings = new ArrayList<>();

        // Calculate the total number of days the event spans
        long daysDifference = ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());

        // Iterate over each day the event spans
        for (int i = 0; i <= daysDifference; i++) {
            // Calculate the current date in the loop
            LocalDateTime currentDateTime = startDateTime.plusDays(i);

            // Get the day of the week for the current date
            DayOfWeek currentDayOfWeek = currentDateTime.getDayOfWeek();

            // Calculate the row index based on the current date's day of the week
            int rowIndex = (currentDayOfWeek.getValue() - firstDayOfWeek.getValue() + 7) % 7;

            // For the first day, use the provided start time. For other days, the event
            // starts at 00:00.
            int startHour = (i == 0) ? startDateTime.getHour() : 0;

            // For the last day, use the provided end time. For other days, the event ends
            // at 23:59.
            int endHour = (i == daysDifference) ? endDateTime.getHour() : 23;

            // Add the row and column indices for the current day
            gridMappings.add(new int[] { rowIndex, startHour, endHour });
        }

        return gridMappings;
    }
}

// public static void main(String[] args) {
// LocalDate inputDate = LocalDate.of(2024, 9, 30);
// int timeIndex = 3;

// int[] gridPosition = mapToGrid(inputDate, timeIndex);

// System.out.println("Date: " + inputDate + "row: " + gridPosition[0] + ",
// column: " + gridPosition[1]);
// }
