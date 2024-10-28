package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import calendar.core.CalendarApp;
import calendar.core.Core;
import calendar.core.SceneCore;
import calendar.types.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * The {@code CalendarController} class is a JavaFX controller responsible for managing the calendar view.
 * It handles user interactions for navigating between weeks, adding events, and displaying events within a weekly grid.
 */
public class CalendarController {
    private static String DEFAULT_EVENT_CLASS_NAME = "event";

    protected CalendarApp calendarApp;
    protected LocalDate weekDate;

    @FXML
    protected Label messageLabel;

    @FXML
    protected GridPane calendarGrid;

    @FXML
    protected DatePicker startDatePicker;

    @FXML
    protected DatePicker endDatePicker;

    @FXML
    protected TextField eventNameField;

    @FXML
    protected Spinner<Integer> startTimeSpinner;

    @FXML
    protected Spinner<Integer> endTimeSpinner;

    @FXML
    protected Label weekLabel;

    /**
     * Initializes the controller, sets up default values for the spinners, loads the calendar app,
     * sets the current date to the current week, and updates the calendar view.
     */
    @FXML
    public void initialize() {
        startTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        endTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        calendarApp = Core.getCalendarApp().orElseThrow();
        weekDate = LocalDate.now();
        updateWeekNr();
        update();
    }

    /**
     * Handles the action to navigate back to the login screen.
     *
     * @param event the action event triggered by clicking the "Back to Login" button
     */
    public void handleBackToLogin(ActionEvent event) {
        SceneCore.setScene("Login.fxml");
    }

    /**
     * Moves the calendar view to the previous week and updates the view.
     *
     * @param event the action event triggered by clicking the "Previous Week" button
     */
    public void previousWeek(ActionEvent event) {
        this.weekDate = this.weekDate.minusWeeks(1);
        updateWeekNr();
        update();
    }

    /**
     * Moves the calendar view to the next week and updates the view.
     *
     * @param event the action event triggered by clicking the "Next Week" button
     */
    public void nextWeek(ActionEvent event) {
        this.weekDate = this.weekDate.plusWeeks(1);
        updateWeekNr();
        update();
    }

    /**
     * Clears the calendar grid by removing all event nodes.
     */
    public void clearCalendar() {
        calendarGrid.getChildren().removeIf(node -> node.getStyleClass().contains(DEFAULT_EVENT_CLASS_NAME));
    }

    /**
     * Updates the label displaying the current week number based on the current date.
     */
    public void updateWeekNr() {
        weekLabel.setText("Week " + weekDate.get(WeekFields.ISO.weekOfWeekBasedYear()));

        // int startDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN).getDayOfMonth();
        // int daysInMonth = weekDate.with(DayOfWeek.MONDAY).lengthOfMonth();
        // IntStream.range(0, CalendarApp.DAYS_IN_A_WEEK)
        //         .forEach(i -> ((Label) calendarGrid
        //                 .getChildrenUnmodifiable()
        //                 .filtered(node -> node instanceof VBox)
        //                 .stream().collect(Collectors.toList())
        //                 .stream().map(d -> ((VBox) d).getChildrenUnmodifiable().getLast())
        //                 .toList().get(i))
        //                 .setText((i + startDateTime) % daysInMonth == 0
        //                         ? "" + daysInMonth
        //                         : "" + (i + startDateTime) % daysInMonth));
    }

    /**
     * Updates the calendar view by clearing the current events and re-adding events for the current week.
     */
    public void update() {
        clearCalendar();

        LocalDateTime startDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        List<Event> events = calendarApp.getEventsBetween(startDateTime, endDateTime);

        for (Event event : events) {

            LocalDateTime eventStartTime = event.getStartTime();
            LocalDateTime eventEndTime = event.getEndTime();

            if (eventStartTime.isBefore(startDateTime))
                eventStartTime = startDateTime;
            if (eventEndTime.isAfter(endDateTime))
                eventEndTime = startDateTime;

            int startDayIndex = eventStartTime.getDayOfWeek().getValue();
            int endDayIndex = eventEndTime.getDayOfWeek().getValue();

            int startRowIndex = eventStartTime.getHour();
            int endRowIndex = eventEndTime.getHour();

            // Single day Event
            if (eventStartTime.toLocalDate().equals(eventEndTime.toLocalDate())) {
                createEventRect(event, startDayIndex, startRowIndex + 1, endRowIndex - startRowIndex + 1);
                continue;
            }

            // Multi day Event
            for (int dayIndex = startDayIndex; dayIndex <= endDayIndex; dayIndex++) {
                if (dayIndex != startDayIndex && dayIndex != endDayIndex) {
                    createEventRect(event, dayIndex, 1, CalendarApp.HOURS_IN_A_DAY);
                    continue;
                }
                boolean isStartDay = dayIndex == startDayIndex;
                createEventRect(event, dayIndex,
                        isStartDay ? startRowIndex + 1 : 1,
                        !isStartDay ? endRowIndex + 1 : (CalendarApp.HOURS_IN_A_DAY - startRowIndex));

            }
        }
    }

    /**
     * Creates and adds a rectangle representing an event in the calendar grid.
     *
     * @param event         the event to be displayed
     * @param dayIndex      the column representing the day of the event
     * @param startTimeIndex the row representing the start time of the event
     * @param length        the number of rows the event spans
     */
    public void createEventRect(Event event, int dayIndex, int startTimeIndex, int length) {
        VBox eventBox = new VBox(10);
        eventBox.getStyleClass().add(DEFAULT_EVENT_CLASS_NAME);
        eventBox.getChildren().add(new Label(event.getTitle()));
        eventBox.setAlignment(Pos.TOP_CENTER);

        GridPane.setRowSpan(eventBox, length);
        calendarGrid.add(eventBox, dayIndex, startTimeIndex);
    }

    /**
     * Adds an event manually using the values from the input fields, such as event name, start time, and end time.
     */
    public void addEventManually() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        int startTime = startTimeSpinner.getValue();
        int endTime = endTimeSpinner.getValue();
        String eventName = eventNameField.getText();

        if (startDate == null)
            return;
        if (endDate == null)
            return;

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(startTime, 0));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(endTime, 0));

        calendarApp
                .createEvent(eventName, eventName, startDateTime, endDateTime)
                .ifPresentOrElse(msg -> messageLabel.setText(msg), this::update);
    }
}
