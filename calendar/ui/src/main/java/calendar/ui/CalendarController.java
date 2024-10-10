package calendar.ui;

import calendar.core.CalendarApp;
import calendar.core.Core;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CalendarController {

    private CalendarApp calendarApp;

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private ChoiceBox<TimeSlot> timeChoiceBox;

    @FXML
    public void initialize() {
        timeChoiceBox.setItems(
                FXCollections.observableArrayList(new TimeSlot("00.00-02.00", 0), new TimeSlot("02.00-04.00", 2)));
        calendarApp = Core.getCalendarApp().orElseThrow();

    }

    public void previousWeek(ActionEvent event) {
        messageLabel.setText("prevousMonth pressed");
    }

    public void nextWeek(ActionEvent event) {
        messageLabel.setText("nextMonth pressed");
    }

    public void addEvent(ActionEvent event) {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-border-color: red");
        vBox.getChildren().add(new Label("Event"));

        calendarGrid.add(vBox, 6, timeChoiceBox.getValue().getStartTime());
    }
}
