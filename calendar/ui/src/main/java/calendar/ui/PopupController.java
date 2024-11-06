package calendar.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import calendar.core.CalendarApp;
import calendar.types.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PopupController {

    @FXML
    private VBox rootVBox;
    @FXML
    private TextField eventNameField;
    @FXML
    private DatePicker startDateSelect;
    @FXML
    private DatePicker endDateSelect;
    @FXML
    private TextField startTimeSelect;
    @FXML
    private TextField endTimeSelect;
    @FXML
    private Label messageLabel;
    @FXML
    private Circle colorCircle;
    @FXML
    private ColorPicker colorPicker;

    private Event event;
    private CalendarApp calendarApp;
    private CalendarController calendarController;
    private Stage stage;

    public void initialize(Event event, CalendarApp calendarApp, CalendarController calendarController) {
        this.event = event;
        this.calendarApp = calendarApp;
        this.calendarController = calendarController;

        Stream.of(rootVBox).forEach(this::loseFocus);
        Stream.of(startDateSelect, endDateSelect).forEach(this::datePicker);
        Stream.of(startTimeSelect, endTimeSelect)
                .forEach(l -> l.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        timeSelectLoseFocus(l);
                }));

        eventNameField.setText(event.getTitle());
        startDateSelect.setValue(event.getStartTime().toLocalDate());
        endDateSelect.setValue(event.getEndTime().toLocalDate());
        if (event.getStartTime().getHour() < 10) {
            startTimeSelect.setText("0" + String.valueOf(event.getStartTime().getHour()) + ":00");
        } else {
            startTimeSelect.setText(String.valueOf(event.getStartTime().getHour()) + ":00");
        }
        if (event.getEndTime().getHour() < 10) {
            endTimeSelect.setText("0" + String.valueOf(event.getEndTime().getHour()) + ":00");
        } else {
            endTimeSelect.setText(String.valueOf(event.getEndTime().getHour()) + ":00");
        }
        colorPicker.setValue(event.getColor());
        colorCircle.setFill(colorPicker.getValue());

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    @FXML
    protected void colorPicker() {
        colorPicker.show();
        colorPicker.setOnAction(e -> colorCircle.setFill(colorPicker.getValue()));
    }

    @FXML
    private void timeSelectKey(KeyEvent event) {
        calendarController.timeSelectKey(event);
    }

    protected void loseFocus(Node root) {
        calendarController.loseFocus(root);
    }

    private void datePicker(DatePicker datePicker) {
        calendarController.datePicker(datePicker);
    }

    private void timeSelectLoseFocus(TextField textField) {
        calendarController.timeSelectLoseFocus(textField);
    }

    @FXML
    private void handleEdit() {
        // Handle the edit event logic here
        String newEventName = eventNameField.getText();
        LocalDate startDate = startDateSelect.getValue();
        LocalDate endDate = endDateSelect.getValue();
        int startTime = Integer.parseInt(startTimeSelect.getText().substring(0, 2));
        int endTime = Integer.parseInt(endTimeSelect.getText().substring(0, 2));
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(startTime, 0));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(endTime, 0));

        calendarApp
                .updateEvent(event, newEventName, "Not implemented", startDateTime, endDateTime, colorPicker.getValue())
                .ifPresentOrElse(msg -> System.out.println(msg), () -> {
                    calendarController.update();
                    stage.close();
                });

    }

    @FXML
    private void handleDelete() {
        // Handle the delete event logic here
        calendarApp.removeEvent(event);
        calendarController.update();
        stage.close();
    }
}
