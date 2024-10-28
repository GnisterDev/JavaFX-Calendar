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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        calendarGrid.getChildren().removeIf(node -> node.getStyleClass().contains(DEFAULT_EVENT_CLASS_NAME));
    }

    public void updateWeekNr() {
        weekLabel.setText("Week " + weekDate.get(WeekFields.ISO.weekOfWeekBasedYear()));

        // int startDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY),
        // LocalTime.MIN).getDayOfMonth();
        // int daysInMonth = weekDate.with(DayOfWeek.MONDAY).lengthOfMonth();
        // IntStream.range(0, CalendarApp.DAYS_IN_A_WEEK)
        // .forEach(i -> ((Label) calendarGrid
        // .getChildrenUnmodifiable()
        // .filtered(node -> node instanceof VBox)
        // .stream().collect(Collectors.toList())
        // .stream().map(d -> ((VBox) d).getChildrenUnmodifiable().getLast())
        // .toList().get(i))
        // .setText((i + startDateTime) % daysInMonth == 0
        // ? "" + daysInMonth
        // : "" + (i + startDateTime) % daysInMonth));
        // New test comment
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
        eventBox.getStyleClass().add(DEFAULT_EVENT_CLASS_NAME);
        eventBox.getChildren().add(new Label(event.getTitle()));
        eventBox.setAlignment(Pos.TOP_CENTER);
        eventBox.setOnMouseClicked(mouseEvent -> {
            popUpForm(event, event.getTitle(), event.getStartTime().toLocalDate(), event.getEndTime().toLocalDate(),
                    event.getStartTime().getHour(), event.getEndTime().getHour()).show();
        });

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

    // create a popup form for editing

    public Stage popUpForm(Event event, String eventName, LocalDate startDate, LocalDate endDate, int startTime,
            int endTime) {
        Stage stage = new Stage();
        stage.setWidth(250);
        stage.setHeight(400);
        stage.setX(50);
        stage.setY(10);

        // Create the VBox layout
        VBox vbox = new VBox(10);
        vbox.setPrefSize(250, 400);

        // Add a label
        Label formTitle = new Label("Edit event: " + eventName);
        formTitle.setStyle("-fx-font-size: 20px;");

        // Create a TextField for event name
        TextField eventNameField = new TextField(eventName);
        eventNameField.setPromptText("Enter new event name");
        eventNameField.setPrefSize(197, 30);
        eventNameField.setStyle("-fx-background-radius: 12;");

        // Create DatePickers for start and end date
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

        // Create Spinners for start and end time
        Spinner<Integer> startTimeSpinner = new Spinner<>(0, 23, startTime);
        Spinner<Integer> endTimeSpinner = new Spinner<>(0, 23, endTime);
        startTimeSpinner.setStyle("-fx-primary-color: #007acc;");
        endTimeSpinner.setStyle("-fx-primary-color: #007acc;");

        HBox buttonContainer = new HBox(10);
        buttonContainer.setPadding(new Insets(10, 10, 10, 10));

        // Create the 'Edit event' button
        Button editButton = new Button("Edit event");
        editButton.setPrefSize(107, 30);
        editButton.setStyle("-fx-background-radius: 12; -fx-background-color: #EA454C;");
        editButton.setTextFill(javafx.scene.paint.Color.WHITE);

        // Create messageLabel
        Label messageLabel = new Label();

        // Handle the editButton click event
        editButton.setOnAction(mouseEvent -> {
            String eventNameInput = eventNameField.getText();
            LocalDate startDateInput = startDatePicker.getValue();
            LocalDate endDateInput = endDatePicker.getValue();
            int startTimeInput = startTimeSpinner.getValue();
            int endTimeInput = endTimeSpinner.getValue();

            LocalDateTime startDateTime = LocalDateTime.of(startDateInput, LocalTime.of(startTimeInput, 0));
            LocalDateTime endDateTime = LocalDateTime.of(endDateInput, LocalTime.of(endTimeInput, 0));

            calendarApp.updateEvent(event, eventNameInput, eventName, startDateTime, endDateTime)
                    .ifPresentOrElse(msg -> messageLabel.setText(msg), () -> {
                        update();
                        stage.close();
                    });
        });

        // Create the 'Delete event' button
        Button deleteButton = new Button("Delete event");
        deleteButton.setPrefSize(107, 30);
        deleteButton.setStyle("-fx-background-radius: 12; -fx-background-color: #EA454C;");
        deleteButton.setTextFill(javafx.scene.paint.Color.WHITE);

        // Handle the deleteButton click event
        deleteButton.setOnAction(mouseEvent -> {
            calendarApp.removeEvent(event);
            update();
            stage.close();
        });

        buttonContainer.getChildren().addAll(editButton, deleteButton);

        // Add all components to the VBox layout
        vbox.getChildren().addAll(formTitle, eventNameField, startDatePicker, endDatePicker, startTimeSpinner,
                endTimeSpinner, buttonContainer, messageLabel);

        // Set the scene and show the stage
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        return stage;

    }
}
