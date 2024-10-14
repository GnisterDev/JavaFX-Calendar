package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

public class CalendarController {

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

    @FXML
    public void initialize() {
        startTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        endTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        calendarApp = Core.getCalendarApp().orElseThrow();
        weekDate = LocalDate.now();
        syncUI();
    }

    public void handleBackToLogin(ActionEvent event) {
        SceneCore.setScene("Login.fxml");
    }

    public void previousWeek(ActionEvent event) {
        this.weekDate = this.weekDate.minusWeeks(1);
        syncUI();
        messageLabel.setText("previousWeek pressed");
    }

    public void nextWeek(ActionEvent event) {
        this.weekDate = this.weekDate.plusWeeks(1);
        syncUI();
        messageLabel.setText("nextWeek pressed");
    }

    public void clearCalendar() {
        calendarGrid.getChildren().removeIf(node -> node.getStyleClass().contains("eventBox"));
    }

    public void syncUI() {
        LocalDateTime startDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);

        List<Event> events = calendarApp.getEventsBetween(startDateTime, endDateTime);

        // Clearing the current calendar view before rendering new events
        clearCalendar();

        for (Event event : events) {
            VBox vBox = createVBox(event);

            // Get the actual start and end times, but constrain them within the current
            // week
            LocalDateTime eventStart = event.getStartTime();
            LocalDateTime eventEnd = event.getEndTime();

            if (eventStart.isBefore(startDateTime)) {
                eventStart = startDateTime; // constrain start to Monday of the current week
            }
            if (eventEnd.isAfter(endDateTime)) {
                eventEnd = endDateTime; // constrain end to Sunday of the current week
            }

            int startDayIndex = eventStart.getDayOfWeek().ordinal() + 1;
            int endDayIndex = eventEnd.getDayOfWeek().ordinal() + 1;

            int startRowIndex = eventStart.getHour() + 1;
            int endRowIndex = eventEnd.getHour() + 1;
            int rowSpan = endRowIndex - startRowIndex + 1;

            // Handle single-day events
            if (startDayIndex == endDayIndex) {
                GridPane.setRowSpan(vBox, rowSpan);
                calendarGrid.add(vBox, startDayIndex, startRowIndex);
            }
            // Handle multi-day events
            else {
                for (int dayIndex = startDayIndex; dayIndex <= endDayIndex; dayIndex++) {
                    VBox dayVBox = createVBox(event);

                    if (dayIndex == startDayIndex) {
                        // First day (partial if event starts mid-day)
                        GridPane.setRowSpan(dayVBox, 24 - startRowIndex + 1);
                        calendarGrid.add(dayVBox, dayIndex, startRowIndex);
                    } else if (dayIndex == endDayIndex) {
                        // Last day (partial if event ends mid-day)
                        GridPane.setRowSpan(dayVBox, endRowIndex);
                        calendarGrid.add(dayVBox, dayIndex, 1);
                    } else {
                        // Full day in the middle
                        GridPane.setRowSpan(dayVBox, 24);
                        calendarGrid.add(dayVBox, dayIndex, 1);
                    }
                }
            }
        }
    }

    public VBox createVBox(Event event) {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-border-color: red");
        vBox.getChildren().add(new Label(event.getTitle()));
        vBox.getStyleClass().add("eventBox");
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public void addEventManually() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        int startTime = startTimeSpinner.getValue();
        int endTime = endTimeSpinner.getValue();
        String eventName = eventNameField.getText();

        // Combine date and time into LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(startTime, 0));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(endTime, 0));

        // Check if the start time is after the end time
        if (startDateTime.isAfter(endDateTime)) {
            messageLabel.setText("Start date and time cannot be after end date and time.");
            return;
        }

        Event newEvent = new Event(eventName, "Temp description", startDateTime, endDateTime);
        calendarApp.addEvent(newEvent);
        syncUI();
    }

}
