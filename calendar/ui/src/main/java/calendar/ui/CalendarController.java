package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import calendar.core.CalendarApp;
import calendar.core.Core;
import calendar.core.SceneCore;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import calendar.types.Event;

public class CalendarController {
    private static final String DEFAULT_EVENT_CLASS_NAME = "event";
    private static final String DATE_IS_TODAY_CLASS_NAME = "calendar-date-today";

    private CalendarApp calendarApp;
    private LocalDate weekDate;

    // Header section
    @FXML
    private Pane header;

    @FXML
    private Pane rootPane;

    @FXML
    private Label weekLabel;

    @FXML
    private Text monthLabel;

    @FXML
    private Text yearLabel;

    // Input section
    @FXML
    private TextField eventNameField;
    @FXML
    private TextArea eventDescriptionField;

    @FXML
    private DatePicker startDateSelect;
    @FXML
    private DatePicker endDateSelect;

    @FXML
    private TextField startTimeSelect;
    @FXML
    private TextField endTimeSelect;

    @FXML
    private Circle colorCircle;
    @FXML
    private ColorPicker colorPicker;
    private Color color = Color.web("#EA454C");

    // Calendar Section
    @FXML
    private GridPane timeStampSection;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private HBox dateHeader;

    @FXML
    private void initialize() {
        calendarApp = Core.getCalendarApp().orElseThrow();
        weekDate = LocalDate.now();
        colorCircle.setFill(color);

        Stream.of(rootPane).forEach(this::loseFocus);
        Stream.of(startDateSelect, endDateSelect).forEach(this::datePicker);

        IntStream.range(1, CalendarApp.HOURS_IN_A_DAY).forEach(i -> {
            Text timeStamp = new Text(String.format("%02d:00", i));
            timeStampSection.add(timeStamp, 0, i);
            GridPane.setHalignment(timeStamp, HPos.RIGHT);
            GridPane.setValignment(timeStamp, VPos.CENTER);
        });

        update();
    }

    @FXML
    private void colorPicker(javafx.event.Event event) {
        colorPicker.show();
        colorPicker.setOnAction(e -> {
            color = colorPicker.getValue();
            colorCircle.setFill(color);
        });
    }

    private void loseFocus(Node root) {
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node focusedNode = root.getScene().getFocusOwner();

            if (focusedNode == null)
                return;
            if (focusedNode.equals(root))
                return;
            if (focusedNode.getBoundsInParent().contains(event.getX(), event.getY()))
                return;
            root.requestFocus();
        });
    }

    private void datePicker(DatePicker datePicker) {
        datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused)
                datePicker.show();
        });
    }

    @FXML
    private void previousWeek() {
        this.weekDate = this.weekDate.minusWeeks(1);
        update();
    }

    @FXML
    private void nextWeek() {
        this.weekDate = this.weekDate.plusWeeks(1);
        update();
    }

    @FXML
    private void today() {
        this.weekDate = LocalDate.now();
        update();
    }

    @FXML
    private void signOut() {
        SceneCore.setScene("Login.fxml");
    }

    private void updateDates() {
        LocalDateTime dateOfMonday = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime dateOfSunday = LocalDateTime.of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        int daysInMonth = weekDate.with(DayOfWeek.MONDAY).lengthOfMonth();

        // Weeklabel
        weekLabel.setText("Week " + dateOfMonday.get(WeekFields.ISO.weekOfWeekBasedYear()));

        // Monthlabel
        boolean isSameMonth = dateOfMonday.getMonth().equals(dateOfSunday.getMonth());
        String abbreviatedStartMonth = StringUtils
                .capitalize(dateOfMonday.getMonth().toString().toLowerCase())
                .substring(0, 3);
        String abbreviatedEndMonth = StringUtils
                .capitalize(dateOfSunday.getMonth().toString().toLowerCase())
                .substring(0, 3);
        monthLabel.setText(isSameMonth
                ? StringUtils.capitalize(dateOfMonday.getMonth().toString().toLowerCase())
                : abbreviatedStartMonth + "-" + abbreviatedEndMonth);

        // Yearlabel
        boolean isSameYear = dateOfMonday.getYear() == dateOfSunday.getYear();
        String abbreviatedStartYear = Integer.toString(dateOfMonday.getYear()).substring(2);
        String abbreviatedEndYear = Integer.toString(dateOfSunday.getYear()).substring(2);
        yearLabel.setText(isSameYear
                ? Integer.toString(dateOfMonday.getYear())
                : abbreviatedStartYear + "-" + abbreviatedEndYear);

        // Dates
        IntStream.range(0, CalendarApp.DAYS_IN_A_WEEK).forEach(i -> {
            HBox outerHBox = (HBox) dateHeader
                    .getChildrenUnmodifiable()
                    .filtered(node -> node instanceof HBox)
                    .get(i);
            outerHBox.getStyleClass().removeIf(style -> style.equals(DATE_IS_TODAY_CLASS_NAME));
            HBox innerHBox = (HBox) outerHBox
                    .getChildrenUnmodifiable()
                    .get(0);
            Pane pane = (Pane) innerHBox
                    .getChildrenUnmodifiable()
                    .get(0);
            Label label = (Label) pane.getChildren().stream()
                    .filter(node -> node instanceof Label)
                    .findFirst().get();
            label.setText((i + dateOfMonday.getDayOfMonth()) % daysInMonth == 0
                    ? "" + daysInMonth
                    : "" + (i + dateOfMonday.getDayOfMonth()) % daysInMonth);

            if (weekDate.with(DayOfWeek.MONDAY).plusDays(i).equals(LocalDate.now()))
                outerHBox.getStyleClass().add(DATE_IS_TODAY_CLASS_NAME);
        });
    }

    private void clearCalendar() {
        calendarGrid.getChildren().removeIf(node -> node.getStyleClass().contains(DEFAULT_EVENT_CLASS_NAME));
    }

    private void update() {
        clearCalendar();
        updateDates();

        LocalDateTime dateOfMonday = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime dateOfSunday = LocalDateTime.of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        List<Event> events = calendarApp.getEventsBetween(dateOfMonday, dateOfSunday);

        for (Event event : events) {

            LocalDateTime eventStartTime = event.getStartTime();
            LocalDateTime eventEndTime = event.getEndTime();

            if (eventStartTime.isBefore(dateOfMonday))
                eventStartTime = dateOfMonday;
            if (eventEndTime.isAfter(dateOfSunday))
                eventEndTime = dateOfSunday;

            int startDayIndex = eventStartTime.getDayOfWeek().getValue() - 1;
            int endDayIndex = eventEndTime.getDayOfWeek().getValue() - 1;

            int startRowIndex = eventStartTime.getHour();

            int endRowIndex = eventEndTime.equals(dateOfSunday) ? CalendarApp.HOURS_IN_A_DAY : eventEndTime.getHour();

            // Single day Event
            if (eventStartTime.toLocalDate().equals(eventEndTime.toLocalDate())) {
                createEventRect(event, startDayIndex, startRowIndex, endRowIndex - startRowIndex);
                continue;
            }

            // Multi day Event
            for (int dayIndex = startDayIndex; dayIndex <= endDayIndex; dayIndex++) {
                if (dayIndex != startDayIndex && dayIndex != endDayIndex) {
                    createEventRect(event, dayIndex, 0, CalendarApp.HOURS_IN_A_DAY);
                    continue;
                }
                boolean isStartDay = dayIndex == startDayIndex;
                createEventRect(event, dayIndex,
                        isStartDay ? startRowIndex : 0,
                        !isStartDay ? endRowIndex : (CalendarApp.HOURS_IN_A_DAY - startRowIndex));

            }
        }
    }

    private void createEventRect(Event event, int dayIndex, int startTimeIndex, int length) {
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

    @FXML
    private void addEvent() {
        if (startTimeSelect.getText().isBlank())
            return;
        if (endTimeSelect.getText().isBlank())
            return;
        LocalDate startDate = startDateSelect.getValue();
        LocalDate endDate = endDateSelect.getValue();
        int startTime = Integer.parseInt(startTimeSelect.getText());
        int endTime = Integer.parseInt(endTimeSelect.getText());
        String eventName = eventNameField.getText();

        if (startDate == null)
            return;
        if (endDate == null)
            return;

        LocalDateTime dateOfMonday = LocalDateTime.of(startDate, LocalTime.of(startTime, 0));
        LocalDateTime dateOfSunday = LocalDateTime.of(endDate, LocalTime.of(endTime, 0));

        calendarApp
                .createEvent(eventName, eventName, dateOfMonday, dateOfSunday)
                .ifPresentOrElse(msg -> System.out.println(msg), this::update);
        // .ifPresentOrElse(msg -> messageLabel.setText(msg), this::update);
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
