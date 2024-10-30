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
        colorPicker.setValue(Color.valueOf("#EA454C"));
        colorCircle.setFill(colorPicker.getValue());

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
        colorPicker.setOnAction(e -> colorCircle.setFill(colorPicker.getValue()));
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
        eventBox.setStyle("-fx-background-color: #" + event.getColor().toString().substring(2) + " ;");
        eventBox.getChildren().add(new Label(event.getTitle()));
        eventBox.setAlignment(Pos.TOP_CENTER);

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
        int startTime = Integer.parseInt(startTimeSelect.getText().substring(0, 2));
        int endTime = Integer.parseInt(endTimeSelect.getText().substring(0, 2));
        String eventName = eventNameField.getText();
        Color color = colorPicker.getValue();

        if (startDate == null)
            return;
        if (endDate == null)
            return;
        if (startTime < 0)
            return;
        if (endTime > 24)
            return;

        LocalDateTime dateOfMonday = LocalDateTime.of(startDate, LocalTime.of(startTime, 0));
        LocalDateTime dateOfSunday = LocalDateTime.of(endDate, LocalTime.of(endTime, 0));

        calendarApp
                .createEvent(eventName, eventName, dateOfMonday, dateOfSunday, color)
                .ifPresentOrElse(msg -> System.out.println(msg), this::update);
        // .ifPresentOrElse(msg -> messageLabel.setText(msg), this::update);
    }
}
