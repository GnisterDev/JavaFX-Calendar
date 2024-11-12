package calendar.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.ToggleSwitch;

import calendar.core.RestHelper;
import calendar.core.SceneCore;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import calendar.types.Calendar;
import calendar.types.Event;
import calendar.types.EventType;

/**
 * The {@code CalendarController} class is a JavaFX controller responsible for
 * managing the calendar view. It handles user interactions for navigating
 * between weeks, adding events, and displaying events within a weekly grid.
 */
public class CalendarController {
    /** Constant representing the number of hours in a day. */
    public static final int HOURS_IN_A_DAY = 24;

    /** Constant representing the number of days in a week. */
    public static final int DAYS_IN_A_WEEK = 7;

    /** The default classname for the event objects added to the calendar. */
    private static final String DEFAULT_EVENT_CLASS_NAME = "event";

    /** The classname used for the day today, to make it stand out more. */
    private static final String DATE_IS_TODAY_CLASS_NAME =
            "calendar-date-today";

    /** The default color of the color picker. */
    private static final String DEFAULT_EVENT_COLOR = "#EA454C";

    /**
     * A localdate to keep track of which week the user currently has displayed.
     */
    private LocalDate weekDate;

    /** The header pane in the ui. */
    @FXML
    private Pane header;

    /** The root pane in the ui. */
    @FXML
    private Pane rootPane;

    /** The label that provides the week number to the user. */
    @FXML
    private Label weekLabel;

    /** The text element that displays the currenty viewed month. */
    @FXML
    private Text monthLabel;

    /** The text element that displays the currenty viewed year. */
    @FXML
    private Text yearLabel;

    /** A dropdown to choose different calendars. */
    @FXML
    private ChoiceBox<Calendar> calendarSelect;

    /** The input for the name of a new event. */
    @FXML
    private TextField eventNameField;

    /** The input for the description of a new event. */
    @FXML
    private TextArea eventDescriptionField;

    /** The input for the start date of a new event. */
    @FXML
    private DatePicker startDateSelect;

    /** The input for the end date of a new event. */
    @FXML
    private DatePicker endDateSelect;

    /** The input for the start time of a new event. */
    @FXML
    private TextField startTimeSelect;

    /** The input for the end time of a new event. */
    @FXML
    private TextField endTimeSelect;

    /** The visual indication for which color has been chosen. */
    @FXML
    private Circle colorCircle;

    /** The input asosiated with the color that has been chosen. */
    @FXML
    private ColorPicker colorPicker;

    /** The togle for if the event takes place the whole day. */
    @FXML
    private ToggleSwitch allDaySwitch;

    /**
     * The label resposible for displaying if there has been an error in
     * creating the event.
     */
    @FXML
    private Label errorLabel;

    /** The section of the calendar dedicated to the timestaps. */
    @FXML
    private GridPane timeStampSection;

    /** The grid dedicated to the normal events. */
    @FXML
    private GridPane calendarGrid;

    /** The grid dedicated to the events that take place a whole day. */
    @FXML
    private GridPane allDayGrid;

    /**
     * The scroll pane responible for scrollng if there are more all day events
     * then can fit in the allDayGrid.
     */
    @FXML
    private ScrollPane allDayScrollPane;

    /** The field used to name new calendars. */
    @FXML
    private TextField calendarName;

    /** Error message label if error ocures during creation of new calendar. */
    @FXML
    private Label calendarErrorLabel;

    /**
     * The header responisble for containing all the dates above the calendar
     * grid.
     */
    @FXML
    private HBox dateHeader;

    @FXML
    private void initialize() {
        weekDate = LocalDate.now();

        colorPicker.setValue(Color.valueOf(DEFAULT_EVENT_COLOR));
        colorPicker
                .setOnAction(e -> colorCircle.setFill(colorPicker.getValue()));
        colorCircle.setFill(colorPicker.getValue());

        calendarSelect.setConverter(new StringConverter<Calendar>() {
            public String toString(final Calendar cal) {
                if (cal == null) return "ERROR: NULL CALENDAR";
                return cal.getName();
            };

            public Calendar fromString(final String str) {
                return calendarSelect.getItems().stream()
                        .filter(cal -> cal.getName().equals(str)).findFirst()
                        .orElse(null);
            };
        });
        calendarSelect.setOnHidden((javafx.event.Event e) -> {
            if (calendarSelect.getValue() == null) return;
            RestHelper.setCaledarId(calendarSelect.getValue().getCalendarId());
            update();
        });
        calendarSelect.getItems().addAll(RestHelper.getUser()
                .map(user -> user.getCalendars()).orElse(new ArrayList<>()));
        calendarSelect.setValue(calendarSelect.getItems().stream().findFirst()
                .orElse(null));
        RestHelper.setCaledarId(calendarSelect.getValue().getCalendarId());

        Stream.of(rootPane).forEach(this::loseFocus);
        Stream.of(startDateSelect, endDateSelect).forEach(this::datePicker);
        Stream.of(startTimeSelect, endTimeSelect).forEach(l -> l
                .focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) timeSelectLoseFocus(l);
                }));

        IntStream.range(1, HOURS_IN_A_DAY).forEach(i -> {
            Text timeStamp = new Text(String.format("%02d:00", i));
            timeStampSection.add(timeStamp, 0, i);
            GridPane.setHalignment(timeStamp, HPos.RIGHT);
            GridPane.setValignment(timeStamp, VPos.CENTER);
        });

        allDaySwitch.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setTimeSelectorsVisibility(!newValue);
                });

        update();
    }

    private void setTimeSelectorsVisibility(final boolean isVisible) {
        Stream.of(startTimeSelect, endTimeSelect).forEach(node -> {
            node.setVisible(isVisible);
            node.setManaged(isVisible);
        });
    }

    @FXML
    private void colorPicker(final javafx.event.Event event) {
        colorPicker.show();
    }

    /**
     * Auto formats the input for the user in the inputs for the time.
     * <p>
     * Exaples:
     * <ul>
     * <li>4 -> 04:00</li>
     * <li>23 -> 23:00</li>
     * <li>47 -> 07:00</li>
     * <li>713 -> 13:00</li>
     * </ul>
     * <p>
     *
     * @param event the keyevent that trigerd this method.
     */
    @FXML
    protected void timeSelectKey(final KeyEvent event) {
        final int activeLength = 2;
        final int defaultLength = 5;

        TextField field = (TextField) event.getSource();
        field.setText(field.getText().replaceAll("\\D", ""));

        switch (field.getLength()) {
            case activeLength -> field.setText(field.getText() + ":00");
            case defaultLength -> field.setText(event.getCharacter());
            default -> field.setText(field.getText());
        }
        field.positionCaret(field.getLength() == defaultLength
                + 1 ? 1 : defaultLength);

        if (field.getLength() == 1
                && Integer.parseInt(field.getText()) >= activeLength
                        + 1) {
            field.setText("0" + field.getText() + ":00");
            return;
        }

        if (field.getLength() == 1 || field.getLength() == 0) return;
        if (Integer.parseInt(field.getText()
                .substring(0, activeLength)) > HOURS_IN_A_DAY)
            field.setText("0" + event.getCharacter() + ":00");
        if (field.getText().matches("^\\d$|^\\d{2}:\\d{2}$")) return;
        field.setText("");
    }

    /**
     * Handles focus loss for the specified time input field, ensuring the time
     * format is valid.
     * <p>
     * If the input text matches the format "HH:MM", the method does nothing.
     * Otherwise, it attempts to correct the input to a valid time format,
     * adding a leading zero or resetting the value.
     * </p>
     *
     * @param field the {@link TextField} representing the time input
     */
    protected void timeSelectLoseFocus(final TextField field) {
        if (field.getText().matches("^\\d{2}:\\d{2}$")) return;
        field.setText(field.getText().matches("^\\d$")
                ? "0" + field.getText() + ":00"
                : "");
    }

    /**
     * Adds a mouse event listener to the specified root node to ensure it
     * regains focus when the user clicks outside of it.
     *
     * @param root the {@link Node} that should regain focus when clicked
     *             outside of
     */
    protected void loseFocus(final Node root) {
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node focusedNode = root.getScene().getFocusOwner();

            if (focusedNode == null) return;
            if (focusedNode.equals(root)) return;
            if (focusedNode.getBoundsInParent().contains(event.getX(),
                                                         event.getY()))
                return;
            root.requestFocus();
        });
    }

    /**
     * Adds a listener to the specified {@link DatePicker} to automatically show
     * the date picker when the field gains focus.
     *
     * @param datePicker the {@link DatePicker} to configure
     */
    protected void datePicker(final DatePicker datePicker) {
        datePicker.focusedProperty()
                .addListener((obs, wasFocused, isNowFocused) -> {
                    if (isNowFocused) datePicker.show();
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

    @FXML
    private void addNewCalendar() {
        RestHelper.addCalendar(Optional.of(calendarName.getText()))
                .consumeError(calendarErrorLabel::setText);

        List<UUID> knownCalendars = calendarSelect.getItems().stream()
                .map(cal -> cal.getCalendarId()).toList();
        List<Calendar> cals = RestHelper.getUser()
                .map(user -> user.getCalendars()).orElse(new ArrayList<>())
                .stream()
                .filter(cal -> !knownCalendars.contains(cal.getCalendarId()))
                .toList();
        calendarSelect.getItems().addAll(cals);

        calendarName.setText("");
        update();
    }

    private void updateDates() {

        LocalDateTime startTime = LocalDateTime
                .of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime
                .of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);

        // Weeklabel
        weekLabel.setText("Week "
                + startTime.get(WeekFields.ISO.weekOfWeekBasedYear()));

        updateDates(startTime);
        updateMonth(startTime, endTime);
        updateYear(startTime, endTime);
    }

    private void updateMonth(final LocalDateTime startTime,
            final LocalDateTime endTime) {
        final int abbreviatedMonthLength = 3;

        boolean isSameMonth = startTime.getMonth().equals(endTime.getMonth());
        String abbreviatedStartMonth = StringUtils
                .capitalize(startTime.getMonth().toString().toLowerCase())
                .substring(0, abbreviatedMonthLength);
        String abbreviatedEndMonth = StringUtils
                .capitalize(endTime.getMonth().toString().toLowerCase())
                .substring(0, abbreviatedMonthLength);
        monthLabel.setText(isSameMonth
                ? StringUtils.capitalize(startTime.getMonth().toString()
                        .toLowerCase())
                : abbreviatedStartMonth + "-" + abbreviatedEndMonth);
    }

    private void updateYear(final LocalDateTime startTime,
            final LocalDateTime endTime) {
        boolean isSameYear = startTime.getYear() == endTime.getYear();
        String abbreviatedStartYear =
                Integer.toString(startTime.getYear()).substring(2);
        String abbreviatedEndYear =
                Integer.toString(endTime.getYear()).substring(2);
        yearLabel.setText(isSameYear
                ? Integer.toString(startTime.getYear())
                : abbreviatedStartYear + "-" + abbreviatedEndYear);
    }

    private void updateDates(final LocalDateTime startTime) {
        int daysInMonth = weekDate.with(DayOfWeek.MONDAY).lengthOfMonth();
        IntStream.range(0, DAYS_IN_A_WEEK).forEach(i -> {
            HBox outerHBox = (HBox) dateHeader.getChildrenUnmodifiable()
                    .filtered(node -> node instanceof HBox).get(i);
            outerHBox.getStyleClass()
                    .removeIf(style -> style.equals(DATE_IS_TODAY_CLASS_NAME));
            HBox innerHBox = (HBox) outerHBox.getChildrenUnmodifiable().get(0);
            Pane pane = (Pane) innerHBox.getChildrenUnmodifiable().get(0);
            Label label = (Label) pane.getChildren().stream()
                    .filter(node -> node instanceof Label).findFirst().get();
            label.setText((i
                    + startTime.getDayOfMonth()) % daysInMonth == 0
                            ? "" + daysInMonth
                            : "" + (i
                                    + startTime.getDayOfMonth()) % daysInMonth);

            if (weekDate.with(DayOfWeek.MONDAY).plusDays(i)
                    .equals(LocalDate.now()))
                outerHBox.getStyleClass().add(DATE_IS_TODAY_CLASS_NAME);
        });
    }

    private void clearCalendar() {
        calendarGrid.getChildren().removeIf(node -> node.getStyleClass()
                .contains(DEFAULT_EVENT_CLASS_NAME));
        allDayGrid.getChildren().removeIf(node -> node.getStyleClass()
                .contains(DEFAULT_EVENT_CLASS_NAME));
    }

    /**
     * Updates the ui.
     */
    protected void update() {
        errorLabel.setText("");
        clearCalendar();
        updateDates();

        LocalDateTime startTime = LocalDateTime
                .of(weekDate.with(DayOfWeek.MONDAY), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime
                .of(weekDate.with(DayOfWeek.SUNDAY), LocalTime.MAX);
        List<Event> events = RestHelper
                .getEvents(Optional.of(endTime), Optional.of(startTime))
                .orElse(new ArrayList<>());

        int allDayRow = 0;

        for (Event event : events) {

            LocalDateTime eventStartTime = event.getStartTime();
            LocalDateTime eventEndTime = event.getEndTime();

            if (eventStartTime.isBefore(startTime)) eventStartTime = startTime;
            if (eventEndTime.isAfter(endTime)) eventEndTime = endTime;

            int startDayIndex = eventStartTime.getDayOfWeek().getValue()
                    - 1;
            int endDayIndex = eventEndTime.getDayOfWeek().getValue()
                    - 1;

            if (event.getType().equals(EventType.ALL_DAY)) {
                createEventRect(event,
                                startDayIndex,
                                allDayRow++,
                                endDayIndex
                                        - startDayIndex
                                        + 1);
                continue;
            }

            int startRowIndex = eventStartTime.getHour();

            int endRowIndex = eventEndTime.equals(endTime)
                    ? HOURS_IN_A_DAY
                    : eventEndTime.getHour();

            // Single day Event
            if (eventStartTime.toLocalDate()
                    .equals(eventEndTime.toLocalDate())) {
                createEventRect(event,
                                startDayIndex,
                                startRowIndex,
                                endRowIndex
                                        - startRowIndex);
                continue;
            }

            // Multi day Event
            for (int dayIndex =
                    startDayIndex; dayIndex <= endDayIndex; dayIndex++) {
                if (dayIndex != startDayIndex && dayIndex != endDayIndex) {
                    createEventRect(event, dayIndex, 0, HOURS_IN_A_DAY);
                    continue;
                }
                boolean isStartDay = dayIndex == startDayIndex;
                createEventRect(event,
                                dayIndex,
                                isStartDay ? startRowIndex : 0,
                                !isStartDay
                                        ? endRowIndex
                                        : (HOURS_IN_A_DAY
                                                - startRowIndex));
            }
        }
        Platform.runLater(() -> allDayScrollPane.setVvalue(1D));
    }

    private void createEventRect(final Event event,
            final int columnIndex,
            final int rowIndex,
            final int length) {
        if (length == 0) return;
        VBox eventBox = new VBox();
        eventBox.getStyleClass().add(DEFAULT_EVENT_CLASS_NAME);
        eventBox.setStyle("-fx-background-color: #"
                + event.getColor().toString().substring(2)
                + " ;");
        eventBox.getChildren().add(new Label(event.getTitle()));
        eventBox.setAlignment(Pos.TOP_CENTER);
        eventBox.setOnMouseClicked(mouseEvent -> {
            popUpForm(event,
                      event.getTitle(),
                      event.getStartTime().toLocalDate(),
                      event.getEndTime().toLocalDate(),
                      event.getStartTime().getHour(),
                      event.getEndTime().getHour())
                    .show();
        });

        switch (event.getType()) {
            case EventType.REGULAR -> GridPane.setRowSpan(eventBox, length);
            case EventType.ALL_DAY -> GridPane.setColumnSpan(eventBox, length);
            default ->
                throw new IllegalStateException("Not a valid EventType.");
        }

        GridPane targetGrid = event.getType().equals(EventType.REGULAR)
                ? calendarGrid
                : allDayGrid;
        targetGrid.add(eventBox, columnIndex, rowIndex);
    }

    @FXML
    private void addEvent() {
        Optional<Integer> startTime = Optional.empty();
        Optional<Integer> endTime = Optional.empty();

        try {
            startTime = Optional.of(!allDaySwitch.isSelected()
                    ? Integer
                            .parseInt(startTimeSelect.getText().substring(0, 2))
                    : 0);
            endTime = Optional.of(!allDaySwitch.isSelected()
                    ? Integer.parseInt(endTimeSelect.getText().substring(0, 2))
                    : 1);
        } catch (Exception e) {
            errorLabel.setText("Time value(s) are not set correctly.");
            return;
        }

        Optional<LocalDateTime> startDateTime = startTime
                .map(start -> LocalDateTime.of(startDateSelect.getValue(),
                                               LocalTime.of(start, 0)));

        Optional<LocalDateTime> endDateTime =
                endTime.map(end -> end == HOURS_IN_A_DAY
                        ? LocalDateTime.of(endDateSelect.getValue().plusDays(1),
                                           LocalTime.of(0, 0))
                        : LocalDateTime.of(endDateSelect.getValue(),
                                           LocalTime.of(end, 0)));

        RestHelper
                .addEvent(Optional.of(eventNameField.getText()),
                          Optional.of(eventDescriptionField.getText()),
                          startDateTime,
                          endDateTime,
                          Optional.of(colorPicker.getValue()),
                          Optional.of(!allDaySwitch.isSelected()
                                  ? EventType.REGULAR
                                  : EventType.ALL_DAY))
                .consumeError(errorLabel::setText).runIfSuccess(this::update);
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
        errorLabel.setText("");
    }

    private Stage popUpForm(final Event event,
            final String eventName,
            final LocalDate startDate,
            final LocalDate endDate,
            final int startTime,
            final int endTime) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("Popup.fxml"));
            VBox vbox = loader.load();
            // vbox.setId("rootVBox");

            // Get the controller and initialize it with necessary data
            PopupController controller = loader.getController();

            Stage stage = new Stage();
            stage.setWidth(PopupController.WIDTH);
            stage.setHeight(PopupController.HEIGHT);
            stage.setX(SceneCore.getX());
            stage.setY(SceneCore.getY());
            stage.setScene(new Scene(vbox));
            stage.initModality(Modality.APPLICATION_MODAL);

            controller.setStage(stage);
            controller.initialize(event, this);

            stage.show();

            return stage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
