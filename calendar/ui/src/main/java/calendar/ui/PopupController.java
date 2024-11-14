package calendar.ui;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.controlsfx.control.ToggleSwitch;

import calendar.core.RestHelper;
import calendar.types.Event;
import calendar.types.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * The {@code PopupController} class is a JavaFX controller responsible for
 * managing the popUp view. It handles user interactions for editing events.
 */
public class PopupController {
    /** The width of the popUp */
    public static final int Width = 250;
    /** The height of the popUp */
    public static final int Height = 400;

    /** The root node of the scene. */
    @FXML
    private VBox rootVBox;

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
    private ToggleSwitch allDay;

    /**
     * The label resposible for displaying if there has been an error in
     * creating the event.
     */
    @FXML
    private Label errorLabel;

    /** The event that is beeing edited. */
    private Event event;

    /** The controller of the main scene. */
    private CalendarController calendarController;

    /** The stage of this popUp. */
    private Stage stage;

    /**
     * Initializes the popup with the specified event details and connects to
     * the main calendar controller.
     * <p>
     * This method sets up the popup's fields to display the details of the
     * provided {@link Event}, including the event's title, description, start
     * and end dates, times, and color. It configures input fields for
     * validation and focuses management. Additionally, it synchronizes the
     * popup's settings for an all-day event if applicable, and binds listeners
     * to manage visibility of time selection inputs based on the all-day toggle
     * switch.
     * </p>
     *
     * @param event              the {@link Event} to be displayed and edited in
     *                           the popup
     * @param calendarController the {@link CalendarController} managing the
     *                           main calendar interface
     */
    public void initialize(final Event event,
            final CalendarController calendarController) {
        final int extraZeroLength = 10;

        this.event = event;
        this.calendarController = calendarController;

        Stream.of(rootVBox).forEach(this::loseFocus);
        Stream.of(startDateSelect, endDateSelect).forEach(this::datePicker);
        Stream.of(startTimeSelect, endTimeSelect).forEach(l -> l
                .focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) timeSelectLoseFocus(l);
                }));

        eventNameField.setText(event.getTitle());
        eventDescriptionField.setText(event.getDescription());
        startDateSelect.setValue(event.getStartTime().toLocalDate());
        endDateSelect.setValue(event.getEndTime().toLocalDate());

        startTimeSelect
                .setText((event.getStartTime().getHour() < extraZeroLength
                        ? "0"
                        : "") + String.valueOf(event.getStartTime().getHour())
                        + ":00");
        endTimeSelect.setText((event.getEndTime().getHour() < extraZeroLength
                ? "0"
                : "") + String.valueOf(event.getEndTime().getHour()) + ":00");

        colorPicker.setValue(event.getColor());
        colorCircle.setFill(colorPicker.getValue());
        allDay.setSelected(event.getType() == EventType.ALL_DAY);
        setTimeSelectorsVisibility(!allDay.isSelected());
        allDay.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setTimeSelectorsVisibility(!newValue);
                });
    }

    private void setTimeSelectorsVisibility(final boolean isVisible) {
        Stream.of(startTimeSelect, endTimeSelect).forEach(node -> {
            node.setVisible(isVisible);
            node.setManaged(isVisible);
        });
    }

    /**
     * Sets the {@link Stage} for this popup window.
     *
     * @param stage the {@link Stage} instance to associate with this popup
     *              controller
     */
    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns the {@link Stage} associated with this popup.
     *
     * @return the {@link Stage} instance associated with this popup controller
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Displays the color picker dialog and updates the color preview when a
     * color is selected.
     */
    @FXML
    private void colorPicker() {
        colorPicker.show();
        colorPicker
                .setOnAction(e -> colorCircle.setFill(colorPicker.getValue()));
    }

    /**
     * Handles key events in the time input fields, delegating to the calendar
     * controller.
     *
     * @param event the {@link KeyEvent} triggered by the user's key press
     */
    @FXML
    private void timeSelectKey(final KeyEvent event) {
        calendarController.timeSelectKey(event);
    }

    /**
     * Removes focus from the specified root node, delegating to the calendar
     * controller.
     *
     * @param root the {@link Node} to lose focus
     */
    private void loseFocus(final Node root) {
        calendarController.loseFocus(root);
    }

    /**
     * Configures focus behavior for the specified date picker, delegating to
     * the calendar controller.
     *
     * @param datePicker the {@link DatePicker} to configure
     */
    private void datePicker(final DatePicker datePicker) {
        calendarController.datePicker(datePicker);
    }

    /**
     * Handles focus loss for the specified time input field, delegating to the
     * calendar controller.
     *
     * @param textField the {@link TextField} to lose focus
     */
    private void timeSelectLoseFocus(final TextField textField) {
        calendarController.timeSelectLoseFocus(textField);
    }

    @FXML
    private void handleEdit() {
        Optional<Integer> startTime = Optional.empty();
        Optional<Integer> endTime = Optional.empty();

        try {
            startTime = Optional.of(!startTimeSelect.getText().isBlank()
                    ? Integer
                            .parseInt(startTimeSelect.getText().substring(0, 2))
                    : 0);
            endTime = Optional.of(!endTimeSelect.getText().isBlank()
                    ? Integer.parseInt(endTimeSelect.getText().substring(0, 2))
                    : 1);
        } catch (Exception e) {

        }

        Optional<LocalDateTime> startDateTime = startTime
                .map(start -> LocalDateTime.of(startDateSelect.getValue(),
                                               LocalTime.of(start, 0)));

        Optional<LocalDateTime> endDateTime =
                endTime.map(end -> end == CalendarController.HOURS_IN_A_DAY
                        ? LocalDateTime.of(endDateSelect.getValue().plusDays(1),
                                           LocalTime.of(0, 0))
                        : LocalDateTime.of(endDateSelect.getValue(),
                                           LocalTime.of(end, 0)));

        RestHelper
                .editEvent(event.getId(),
                           Optional.of(eventNameField.getText()),
                           Optional.of(eventDescriptionField.getText()
                                   .replace("\n", " ")),
                           startDateTime,
                           endDateTime,
                           Optional.of(colorPicker.getValue()),
                           Optional.of(!allDay.isSelected()
                                   ? EventType.REGULAR
                                   : EventType.ALL_DAY))
                .consumeError(errorLabel::setText)
                .runIfSuccess(calendarController::update)
                .runIfSuccess(stage::close);
    }

    @FXML
    private void handleDelete() {
        RestHelper.removeEvent(event.getId()).consumeError(errorLabel::setText)
                .runIfSuccess(calendarController::update)
                .runIfSuccess(stage::close);
    }
}
