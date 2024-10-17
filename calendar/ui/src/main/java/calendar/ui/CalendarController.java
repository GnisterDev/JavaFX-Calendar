package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
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

    private CalendarApp calendarApp;
    private LocalDate weekDate;

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
        updateWeekNr();
        update();
    }

    public void handleBackToLogin(ActionEvent event) {
        SceneCore.setScene("Login.fxml");
    }

    public void previousWeek(ActionEvent event) {
        this.weekDate = this.weekDate.minusWeeks(1);
        updateWeekNr();
        update();
    }

    public void nextWeek(ActionEvent event) {
        this.weekDate = this.weekDate.plusWeeks(1);
        updateWeekNr();
        update();
    }

    public void clearCalendar() {
        calendarGrid.getChildren().removeIf(node -> node.getStyleClass().contains("eventBox"));
    }

    public void updateWeekNr() {
        weekLabel.setText("Week " + weekDate.get(WeekFields.ISO.weekOfWeekBasedYear()));
    }

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

    public void createEventRect(Event event, int dayIndex, int startTimeIndex, int length) {
        VBox eventBox = new VBox(10);
        eventBox.setStyle("-fx-border-color: red");
        eventBox.getChildren().add(new Label(event.getTitle()));
        eventBox.getStyleClass().add("eventBox");
        eventBox.setAlignment(Pos.TOP_CENTER);

        GridPane.setRowSpan(eventBox, length);
        calendarGrid.add(eventBox, dayIndex, startTimeIndex);
    }

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
