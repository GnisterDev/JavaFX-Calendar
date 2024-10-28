package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import calendar.types.Event;

public class NewCalendarController {
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
        int startDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN).getDayOfMonth();
        int daysInMonth = weekDate.with(DayOfWeek.MONDAY).lengthOfMonth();

        // Weeklabel
        weekLabel.setText("Week " + weekDate.get(WeekFields.ISO.weekOfWeekBasedYear()));

        //Yearlabel
        yearLabel.setText("" + weekDate.getYear());

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
            label.setText((i + startDateTime) % daysInMonth == 0
                    ? "" + daysInMonth
                    : "" + (i + startDateTime) % daysInMonth);

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

        LocalDateTime startDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        List<Event> events = calendarApp.getEventsBetween(startDateTime, endDateTime);

        for (Event event : events) {

            LocalDateTime eventStartTime = event.getStartTime();
            LocalDateTime eventEndTime = event.getEndTime();

            if (eventStartTime.isBefore(startDateTime))
                eventStartTime = startDateTime;
            if (eventEndTime.isAfter(endDateTime))
                eventEndTime = endDateTime;

            int startDayIndex = eventStartTime.getDayOfWeek().getValue() - 1;
            int endDayIndex = eventEndTime.getDayOfWeek().getValue() - 1;

            int startRowIndex = eventStartTime.getHour();

            int endRowIndex = eventEndTime.equals(endDateTime) ? CalendarApp.HOURS_IN_A_DAY : eventEndTime.getHour();

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

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(startTime, 0));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(endTime, 0));

        calendarApp
                .createEvent(eventName, eventName, startDateTime, endDateTime)
                .ifPresentOrElse(msg -> System.out.println(msg), this::update);
        // .ifPresentOrElse(msg -> messageLabel.setText(msg), this::update);
    }
}
