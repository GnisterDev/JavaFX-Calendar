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
import org.controlsfx.control.ToggleSwitch;

import calendar.core.CalendarApp;
import calendar.core.Core;
import calendar.core.Error;
import calendar.core.SceneCore;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import calendar.types.Event;

/**
 * The {@code CalendarController} class is a JavaFX controller responsible for managing the calendar view.
 * It handles user interactions for navigating between weeks, adding events, and displaying events within a weekly grid.
 */
public class CalendarController {
    private static final String DEFAULT_EVENT_CLASS_NAME = "event";
    private static final String DATE_IS_TODAY_CLASS_NAME = "calendar-date-today";
    private static final String DEFAULT_EVENT_COLOR = "#EA454C";

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

    @FXML
    private ToggleSwitch allDaySwitch;

    @FXML
    private Label errorLabel;

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

        colorPicker.setValue(Color.valueOf(DEFAULT_EVENT_COLOR));
        colorPicker.setOnAction(e -> colorCircle.setFill(colorPicker.getValue()));
        colorCircle.setFill(colorPicker.getValue());

        Stream.of(rootPane).forEach(this::loseFocus);
        Stream.of(startDateSelect, endDateSelect).forEach(this::datePicker);
        Stream.of(startTimeSelect, endTimeSelect)
                .forEach(l -> l.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        timeSelectLoseFocus(l);
                }));

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
    }

    @FXML
    private void timeSelectKey(KeyEvent event) {
        TextField field = (TextField) event.getSource();
        field.setText(field.getText().replaceAll("\\D", ""));

        switch (field.getLength()) {
            case 2 -> field.setText(field.getText() + ":00");
            case 5 -> field.setText(event.getCharacter());
        }
        field.positionCaret(field.getLength() == 6 ? 1 : 5);

        if (field.getLength() == 1 && Integer.parseInt(field.getText()) >= 3) {
            field.setText("0" + field.getText() + ":00");
            return;
        }

        if (field.getLength() == 1 || field.getLength() == 0)
            return;
        if (Integer.parseInt(field.getText().substring(0, 2)) > 24)
            field.setText("0" + event.getCharacter() + ":00");
        if (field.getText().matches("^\\d$|^\\d{2}:\\d{2}$"))
            return;
        field.setText("");
    }

    private void timeSelectLoseFocus(TextField field) {
        if (field.getText().matches("^\\d{2}:\\d{2}$"))
            return;
        field.setText(field.getText().matches("^\\d$") ? "0" + field.getText() + ":00" : "");
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
        LocalDateTime startTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);

        // Weeklabel
        weekLabel.setText("Week " + startTime.get(WeekFields.ISO.weekOfWeekBasedYear()));

        updateDates(startTime);
        updateMonth(startTime, endTime);
        updateYear(startTime, endTime);
    }

    private void updateMonth(LocalDateTime startTime, LocalDateTime endTime) {
        boolean isSameMonth = startTime.getMonth().equals(endTime.getMonth());
        String abbreviatedStartMonth = StringUtils
                .capitalize(startTime.getMonth().toString().toLowerCase())
                .substring(0, 3);
        String abbreviatedEndMonth = StringUtils
                .capitalize(endTime.getMonth().toString().toLowerCase())
                .substring(0, 3);
        monthLabel.setText(isSameMonth
                ? StringUtils.capitalize(startTime.getMonth().toString().toLowerCase())
                : abbreviatedStartMonth + "-" + abbreviatedEndMonth);
    }

    private void updateYear(LocalDateTime startTime, LocalDateTime endTime) {
        boolean isSameYear = startTime.getYear() == endTime.getYear();
        String abbreviatedStartYear = Integer.toString(startTime.getYear()).substring(2);
        String abbreviatedEndYear = Integer.toString(endTime.getYear()).substring(2);
        yearLabel.setText(isSameYear
                ? Integer.toString(startTime.getYear())
                : abbreviatedStartYear + "-" + abbreviatedEndYear);
    }

    private void updateDates(LocalDateTime startTime) {
        int daysInMonth = weekDate.with(DayOfWeek.MONDAY).lengthOfMonth();
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
            label.setText((i + startTime.getDayOfMonth()) % daysInMonth == 0
                    ? "" + daysInMonth
                    : "" + (i + startTime.getDayOfMonth()) % daysInMonth);

            if (weekDate.with(DayOfWeek.MONDAY).plusDays(i).equals(LocalDate.now()))
                outerHBox.getStyleClass().add(DATE_IS_TODAY_CLASS_NAME);
        });
    }

    private void clearCalendar() {
        calendarGrid.getChildren().removeIf(node -> node.getStyleClass().contains(DEFAULT_EVENT_CLASS_NAME));
    }

    private void update() {
        errorLabel.setText("");
        clearCalendar();
        updateDates();

        LocalDateTime startTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        List<Event> events = calendarApp.getEventsBetween(startTime, endTime);

        for (Event event : events) {

            LocalDateTime eventStartTime = event.getStartTime();
            LocalDateTime eventEndTime = event.getEndTime();

            if (eventStartTime.isBefore(startTime))
                eventStartTime = startTime;
            if (eventEndTime.isAfter(endTime))
                eventEndTime = endTime;

            int startDayIndex = eventStartTime.getDayOfWeek().getValue() - 1;
            int endDayIndex = eventEndTime.getDayOfWeek().getValue() - 1;

            int startRowIndex = eventStartTime.getHour();

            int endRowIndex = eventEndTime.equals(endTime) ? CalendarApp.HOURS_IN_A_DAY : eventEndTime.getHour();

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
        eventBox.setStyle("-fx-background-color: #" + event.getColor().toString().substring(2) + " ;");
        eventBox.getChildren().add(new Label(event.getTitle()));
        eventBox.setAlignment(Pos.TOP_CENTER);

        GridPane.setRowSpan(eventBox, length);
        calendarGrid.add(eventBox, dayIndex, startTimeIndex);
    }

    @FXML
    private void addEvent() {
        int startTime;
        int endTime;

        try {
            startTime = Integer.parseInt(startTimeSelect.getText().substring(0, 2));
            endTime = Integer.parseInt(endTimeSelect.getText().substring(0, 2));
        } catch (Exception e) {
            errorLabel.setText(Error.EVENT_START_END_TIME_NOT_SELECTED);
            return;
        }

        calendarApp.createEvent(
                eventNameField.getText(),
                eventDescriptionField.getText(),
                LocalDateTime.of(startDateSelect.getValue(), LocalTime.of(startTime, 0)),
                LocalDateTime.of(endDateSelect.getValue(), LocalTime.of(endTime, 0)),
                colorPicker.getValue())
                .ifPresentOrElse(msg -> errorLabel.setText(msg), this::update);
    }

    @FXML
    private void cancelEvent() {
        startDateSelect.setValue(null);
        endDateSelect.setValue(null);
        startTimeSelect.setText("");
        endTimeSelect.setText("");
        eventNameField.setText("");
        eventDescriptionField.setText("");
        colorPicker.setValue(Color.web(DEFAULT_EVENT_COLOR));
        colorCircle.setFill(colorPicker.getValue());
    }
}
