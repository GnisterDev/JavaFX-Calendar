package calendar.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.internal.matchers.Any;
import org.mockito.ArgumentMatchers;
import org.testfx.framework.junit5.ApplicationTest;

import calendar.core.CalendarApp;
import calendar.core.Core;
import calendar.core.RestHelper;
import calendar.types.Calendar;
import calendar.types.User;
import calendar.types.UserSettings;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
import no.gorandalum.fluentresult.Result;

public class CalendarControllerTest extends ApplicationTest {

    private CalendarController calendarController;
    private UUID uuid = UUID.randomUUID();
    private UserSettings settings = new UserSettings(uuid);
    private List<Calendar> calendars = List.of(new Calendar(UUID.randomUUID(), "calendar"));
    private User user = new User(uuid, "username", calendars, settings);

    @Override
    public void start(Stage stage) throws Exception {

        try (MockedStatic<RestHelper> mockedRestHelper = mockStatic(RestHelper.class)) {
            mockedRestHelper.when(RestHelper::getUser).thenReturn(Result.success(user));

            mockedRestHelper
                    .when(() -> RestHelper.getEvents(ArgumentMatchers.any(),
                            ArgumentMatchers.any()))
                    .thenReturn(Result.success(new ArrayList<>()));

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

        Button todayButton = lookup("Today").queryAs(Button.class);

        String nextWeekClass = ".header-arrow";
        Set<Pane> buttons = lookup(nextWeekClass).queryAllAs(Pane.class);
        List<Pane> buttonsList = new ArrayList<>(buttons);

        Pane previousWeek = buttonsList.get(0);
        Pane nextWeek = buttonsList.get(1);

        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr);

        clickOn(nextWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr + 1);
        clickOn(nextWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr + 2);

        clickOn(previousWeek).clickOn(previousWeek).clickOn(previousWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr - 1);
        clickOn(previousWeek);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr - 2);

        clickOn(todayButton);
        assertEquals(Integer.parseInt(currentWeekElm.getText().split(" ")[1]), currentWeekNr);
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
        Random random = new Random();

        Circle colorCircle = lookup("#colorCircle").queryAs(Circle.class);
        ColorPicker colorPicker = lookup("#colorPicker").queryAs(ColorPicker.class);

        clickOn(colorCircle);
        assertTrue(colorPicker.isShowing(), "Color picker should be visible after clicking");

        Color currentColor = colorPicker.getValue();
        press(KeyCode.TAB).release(KeyCode.TAB);
        IntStream.range(0, random.nextInt(12)).forEach(i -> press(KeyCode.RIGHT).release(KeyCode.RIGHT));
        IntStream.range(0, random.nextInt(11)).forEach(i -> press(KeyCode.DOWN).release(KeyCode.DOWN));
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        assertNotEquals(currentColor, colorPicker.getValue(), "Color circle should update with the selected color");
    }

    @Test
    public void testDatePicker() {
        DatePicker startDateSelect = lookup("#startDateSelect").queryAs(DatePicker.class);

        clickOn(startDateSelect);
        assertTrue(startDateSelect.isShowing(), "Date picker should be visible after clicking");
    }

    private TextField eventTitleNode;
    private TextArea eventDescriptionNode;
    private DatePicker startDateNode;
    private TextField startTimeNode;
    private DatePicker endDateNode;
    private TextField endTimeNode;

    @BeforeEach
    public void addEvent_BeforeEach() {
        eventTitleNode = lookup("#eventNameField").queryAs(TextField.class);
        eventDescriptionNode = lookup("#eventDescriptionField").queryAs(TextArea.class);
        startDateNode = lookup("#startDateSelect").queryAs(DatePicker.class);
        startTimeNode = lookup("#startTimeSelect").queryAs(TextField.class);
        endDateNode = lookup("#endDateSelect").queryAs(DatePicker.class);
        endTimeNode = lookup("#endTimeSelect").queryAs(TextField.class);
    }

    private void writeEventInfo(
            String eventTitle,
            String eventDescription,
            boolean writeStartDate,
            boolean writeStartTime,
            boolean writeEndDate,
            boolean writeEndTime) {

        Random random = new Random();
        int eventDayLength = random.nextInt(0, 3);
        int startHour = random.nextInt(12);
        int eventLength = random.nextInt(12);

        clickOn(eventTitleNode).write(eventTitle);
        clickOn(eventDescriptionNode).write(eventDescription);

        if (writeStartDate)
            clickOn(startDateNode).press(KeyCode.ENTER).release(KeyCode.ENTER);
        if (writeStartTime)
            clickOn(startTimeNode).write(Integer.toString(startHour));

        if (writeEndDate) {
            clickOn(endDateNode);
            IntStream.range(0, eventDayLength).forEach(i -> press(KeyCode.RIGHT).release(KeyCode.RIGHT));
            press(KeyCode.ENTER).release(KeyCode.ENTER);
        }
        if (writeEndTime)
            clickOn(endTimeNode).write(Integer.toString(startHour + eventLength));
    }

    @Test
    public void testAddEvent_Valid() {
        String eventName = "testEvent";
        String eventDescription = "testDescription";
        writeEventInfo(eventName, eventDescription, true, true, true, true);

        Button addEventButton = (Button) lookup("Add Event")
                .queryAll().stream()
                .filter(node -> node instanceof Button)
                .findFirst().get();
        clickOn(addEventButton);

        assertEquals(eventName, eventTitleNode.getText());
        assertEquals(eventDescription, eventDescriptionNode.getText());
        assertNotNull(startDateNode.getValue());
        assertNotEquals(startTimeNode.getText(), "");
        assertNotNull(endDateNode.getValue());
        assertNotEquals(endTimeNode.getText(), "");
    }

    @Test
    public void testAddEvent_Invalid() throws InterruptedException {
        // Button addEventButton = (Button) lookup("Add Event")
        // .queryAll().stream()
        // .filter(node -> node instanceof Button)
        // .findFirst().get();
        // Label errorLabel = lookup("#errorLabel").queryAs(Label.class);

        // writeEventInfo("testEvent", "testDescription", false, true, true, true);
        // clickOn(addEventButton);
        // Thread.sleep(2000);
        // assertEquals("Start and end times must be selected.", errorLabel.getText());

        // writeEventInfo("testEvent", "testDescription", true, false, true, true);
        // clickOn(addEventButton);
        // assertEquals("Start and end times must be selected.", errorLabel.getText());

        // writeEventInfo("", "testDescription", true, true, true, true);
        // clickOn(addEventButton);
        // assertEquals("Title cannot be blank", errorLabel.getText());
    }

    @Test
    public void testAddEvent_Cancel() {
        String eventName = "testEvent";
        String eventDescription = "testDescription";
        writeEventInfo(eventName, eventDescription, true, true, true, true);

        Button cancelEventButton = (Button) lookup("Cancel")
                .queryAll().stream()
                .filter(node -> node instanceof Button)
                .findFirst().get();
        clickOn(cancelEventButton);

        assertEquals("", eventTitleNode.getText());
        assertEquals("", eventDescriptionNode.getText());
        assertNull(startDateNode.getValue());
        assertEquals(startTimeNode.getText(), "");
        assertNull(endDateNode.getValue());
        assertEquals(endTimeNode.getText(), "");
    }
}
