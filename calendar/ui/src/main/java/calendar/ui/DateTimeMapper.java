package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateTimeMapper {
    public static int[] mapToGrid(LocalDate date, int timeIndex) {

        DayOfWeek firstDayOfWeek = DayOfWeek.MONDAY;

        DayOfWeek inputDayOfWeek = date.getDayOfWeek();

        int rowIndex = (inputDayOfWeek.getValue() - firstDayOfWeek.getValue() + 7) % 7;

        int columnIndex = timeIndex;

        return new int[] { rowIndex + 1, columnIndex + 1 };
    }

    public static void main(String[] args) {
        LocalDate inputDate = LocalDate.of(2024, 9, 30);
        int timeIndex = 3;

        int[] gridPosition = mapToGrid(inputDate, timeIndex);

        System.out.println("Date: " + inputDate + "row: " + gridPosition[0] + ", column: " + gridPosition[1]);
    }
}
