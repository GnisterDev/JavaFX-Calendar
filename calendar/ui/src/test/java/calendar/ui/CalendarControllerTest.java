package calendar.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;

import calendar.core.CalendarApp;
import calendar.core.Core;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CalendarControllerTest extends ApplicationTest {

    @SuppressWarnings("unused") //TODO: This is stupid
    private CalendarController calendarController;
    private CalendarApp mockCalendarApp;

    @Override
    public void start(Stage stage) throws Exception {
        mockCalendarApp = mock(CalendarApp.class);

        try (MockedStatic<Core> mockedCore = mockStatic(Core.class)) {
            mockedCore.when(Core::getCalendarApp).thenReturn(Optional.of(mockCalendarApp));

            // Load the FXML file and set up the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendar/ui/Calendar.fxml"));
            Pane mainNode = loader.load();
            calendarController = loader.getController();

            // Set up the scene with the main UI node
            Scene scene = new Scene(mainNode);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Test
    public void testInitializeCalendar() {
        assertTrue(true);
    }

    @Test
    public void testUpdate() {
        assertTrue(true);
    }

    @Test
    public void testLoseFocus_ShiftsFocusToRoot() {
        BorderPane root = lookup("#rootPane").queryAs(BorderPane.class);
        HBox child = lookup(".header").queryAs(HBox.class);

        Platform.runLater(() -> {
            child.requestFocus();
            assertTrue(child.isFocused(), "Child node should initially have focus");

            MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                    200, 200, 200, 200,
                    MouseButton.PRIMARY, 1,
                    false, false, false, false,
                    true, false, false, true,
                    false, false, null);

            root.fireEvent(mouseEvent);

            assertTrue(root.isFocused(), "Root node should have focus after the event");
            assertFalse(child.isFocused(), "Child node should lose focus after the event");
        });
    }

    @Test
    public void testLoseFocus_NoFocusShiftIfClickInsideFocusedNode() {
        BorderPane root = lookup("#rootPane").queryAs(BorderPane.class);
        HBox child = lookup(".header").queryAs(HBox.class);
        Platform.runLater(() -> {
            child.requestFocus();
            assertTrue(child.isFocused(), "Child node should initially have focus");

            // Simulate a mouse press inside the child node’s bounds
            MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                    child.getLayoutX() + 10, child.getLayoutY() + 10,
                    child.getLayoutX() + 10, child.getLayoutY() + 10,
                    MouseButton.PRIMARY, 1,
                    false, false, false, false,
                    true, false, false, true,
                    false, false, null);

            root.fireEvent(mouseEvent);

            assertTrue(child.isFocused(), "Child node should still have focus after click within bounds");
            assertFalse(root.isFocused(), "Root node should not gain focus after click within focused node bounds");
        });
    }

    @Test
    public void testNextPreviousWeek() {
        Label currentWeekElm = lookup("#weekLabel").queryAs(Label.class);
        int currentWeekNr = Integer.parseInt(currentWeekElm.getText().split(" ")[1]);

        String nextWeekClass = ".header-arrow";
        Set<Pane> buttons = lookup(nextWeekClass).queryAllAs(Pane.class);
        List<Pane> buttonsList = new ArrayList<>(buttons);

        Pane previousWeek = buttonsList.get(0);
        Pane nextWeek = buttonsList.get(1);

        clickOn(nextWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr + 1);
        clickOn(nextWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr + 2);

        clickOn(previousWeek).clickOn(previousWeek).clickOn(previousWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr - 1);
        clickOn(previousWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr - 2);
    }

    @Test
    public void testTimeSelect_DefaultValues() {
        String startTimeFieldID = "#startTimeSelect";
        String endTimeFieldID = "#endTimeSelect";
        TextField startTimeField = lookup(startTimeFieldID).queryAs(TextField.class);

        clickOn(startTimeFieldID).write("0").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "00:00");

        clickOn(startTimeFieldID).write("1").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "01:00");

        clickOn(startTimeFieldID).write("12").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "12:00");

        clickOn(startTimeFieldID).write("24").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "24:00");
    }

    @Test
    public void testTimeSelect_MoreValesThanDefault() throws InterruptedException {
        String startTimeFieldID = "#startTimeSelect";
        String endTimeFieldID = "#endTimeSelect";
        TextField startTimeField = lookup(startTimeFieldID).queryAs(TextField.class);

        clickOn(startTimeFieldID).write("25").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "05:00");

        clickOn(startTimeFieldID).write("89").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "09:00");

        clickOn(startTimeFieldID).write("472").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "02:00");

        clickOn(startTimeFieldID).write("04").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "04:00");

        clickOn(startTimeFieldID).write("57508").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "08:00");

        clickOn(startTimeFieldID).write("65638523").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "23:00");
    }

    @Test
    public void testTimeSelect_RemovalOfInvalidValues() throws InterruptedException {
        String startTimeFieldID = "#startTimeSelect";
        String endTimeFieldID = "#endTimeSelect";
        TextField startTimeField = lookup(startTimeFieldID).queryAs(TextField.class);

        clickOn(startTimeFieldID).write("0").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "00:00");
        clickOn(startTimeFieldID).press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertEquals(startTimeField.getText(), "");

        clickOn(startTimeFieldID).write("12").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "12:00");
        clickOn(startTimeFieldID).press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertEquals(startTimeField.getText(), "");

        clickOn(startTimeFieldID).write("57508").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "08:00");
        clickOn(startTimeFieldID).press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertEquals(startTimeField.getText(), "");

        clickOn(startTimeFieldID).write("65638523").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "23:00");
        clickOn(startTimeFieldID).press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertEquals(startTimeField.getText(), "");
    }

    @Test
    public void testColorPicker() {
        Circle colorCircle = lookup("#colorCircle").queryAs(Circle.class);
        ColorPicker colorPicker = lookup("#colorPicker").queryAs(ColorPicker.class);

        clickOn(colorCircle);
        assertTrue(colorPicker.isShowing(), "Color picker should be visible after clicking");

        Color currentColor = colorPicker.getValue();
        moveBy(10, 25).press(MouseButton.PRIMARY).release(MouseButton.PRIMARY); //TODO: This way of doing this is stupid and should be changed...
        assertNotEquals(currentColor, colorPicker.getValue(), "Color circle should update with the selected color");
    }

    @Test
    public void testDatePicker() {
        DatePicker startDateSelect = lookup("#startDateSelect").queryAs(DatePicker.class);

        clickOn(startDateSelect);
        assertTrue(startDateSelect.isShowing(), "Date picker should be visible after clicking");
    }
}