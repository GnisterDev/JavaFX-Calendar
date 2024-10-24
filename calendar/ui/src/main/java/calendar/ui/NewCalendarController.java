package calendar.ui;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import calendar.core.CalendarApp;
import calendar.core.Core;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class NewCalendarController {
    private static final String DEFAULT_EVENT_CLASS_NAME = "event";

    protected CalendarApp calendarApp;

    @FXML
    private Pane header;

    @FXML
    private Pane rootPane;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Circle colorCircle;
    @FXML
    private ColorPicker colorPicker;
    private Color color = Color.BLUEVIOLET;

    @FXML
    private GridPane timeStampSection;

    @FXML
    private void initialize() {
        // calendarApp = Core.getCalendarApp().orElseThrow();
        colorCircle.setFill(color);

        // Lose focus when clicked of element
        rootPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node focusedNode = rootPane.getScene().getFocusOwner();

            if (focusedNode == null)
                return;
            if (focusedNode.equals(rootPane))
                return;
            if (focusedNode.getBoundsInParent().contains(event.getX(), event.getY()))
                return;
            rootPane.requestFocus();
        });

        // Input section
        Stream.of(startDate, endDate).forEach(this::datePicker);

        IntStream.range(1, CalendarApp.HOURS_IN_A_DAY).forEach(i -> {
            Text timeStamp = new Text(String.format("%02d:00", i));
            timeStampSection.add(timeStamp, 0, i);
            GridPane.setHalignment(timeStamp, HPos.RIGHT);
            GridPane.setValignment(timeStamp, VPos.CENTER);
        });
    }

    @FXML
    private void colorPicker(Event event) {
        colorPicker.show();
        colorPicker.setOnAction(e -> {
            color = colorPicker.getValue();
            colorCircle.setFill(color);
        });
    }

    private void datePicker(DatePicker datePicker) {
        datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused)
                datePicker.show();
        });
    }
}
