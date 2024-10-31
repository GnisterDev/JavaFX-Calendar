package calendar.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import calendar.core.CalendarApp;
import calendar.types.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PopupController {
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
    private Color color = Color.web("#EA454C");

    private Event event;
    private CalendarApp calendarApp;
    private Runnable updateCallback;
    private Stage stage;

    public void initialize(Event event, CalendarApp calendarApp, Runnable updateCallback) {
        this.event = event;
        this.calendarApp = calendarApp;
        this.updateCallback = updateCallback;

        eventNameField.setText(event.getTitle());
        startDateSelect.setValue(event.getStartTime().toLocalDate());
        endDateSelect.setValue(event.getEndTime().toLocalDate());
        startTimeSelect.setText(String.valueOf(event.getStartTime().getHour()));
        endTimeSelect.setText(String.valueOf(event.getEndTime().getHour()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void colorPicker(javafx.event.Event event) {
        colorPicker.show();
        colorPicker.setOnAction(e -> {
            color = colorPicker.getValue();
            colorCircle.setFill(color);
        });
    }

    @FXML
    private void handleEdit() {
        // Handle the edit event logic here
        String newEventName = eventNameField.getText();
        LocalDate startDate = startDateSelect.getValue();
        LocalDate endDate = endDateSelect.getValue();
        int startTime = Integer.parseInt(startTimeSelect.getText());
        int endTime = Integer.parseInt(endTimeSelect.getText());
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(startTime, 0));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(endTime, 0));

        calendarApp.updateEvent(event, newEventName, "Not implemented", startDateTime, endDateTime)
                .ifPresentOrElse(msg -> messageLabel.setText(msg), () -> {
                    updateCallback.run();
                    stage.close();
                });
    }

    @FXML
    private void handleDelete() {
        // Handle the delete event logic here
        calendarApp.removeEvent(event);
        updateCallback.run();
        stage.close();
    }
}
