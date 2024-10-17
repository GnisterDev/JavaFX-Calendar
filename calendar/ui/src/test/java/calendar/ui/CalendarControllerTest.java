package calendar.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import calendar.core.CalendarApp;
import calendar.core.Core;
import calendar.types.Event;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;
import java.time.LocalDate;
import java.util.Optional;

public class CalendarControllerTest extends ApplicationTest {
    private CalendarController controller;
    private CalendarApp mockCalendarApp;
    private MockedStatic<Core> coreMock; // Static mock for Core

    @BeforeEach
    public void setUp() {
        // Initialize the controller and UI components
        controller = spy(new CalendarController());
        GridPane copyGridPane = new GridPane();
        copyGridPane.setId("calendarGrid");
        controller.calendarGrid = copyGridPane;
        controller.messageLabel = new Label();
        controller.startDatePicker = new DatePicker();
        controller.endDatePicker = new DatePicker();
        controller.eventNameField = new TextField();
        controller.startTimeSpinner = new Spinner<>();
        controller.endTimeSpinner = new Spinner<>();
        controller.weekLabel = new Label();

        // Mock CalendarApp and Core.getCalendarApp() static method
        mockCalendarApp = mock(CalendarApp.class);
        coreMock = mockStatic(Core.class); // Create static mock for Core
        coreMock.when(Core::getCalendarApp).thenReturn(Optional.of(mockCalendarApp));

        controller.initialize();
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock after each test to avoid conflicts
        coreMock.close();
    }

    @Test
    public void testInitialize() {
        // Test Spinner initialization
        assertNotNull(controller.startTimeSpinner.getValueFactory(), "Start timespinner should be initialized.");
        assertNotNull(controller.endTimeSpinner.getValueFactory(), "End time spinner should be initialized.");
        assertEquals(8, controller.startTimeSpinner.getValueFactory().getValue(), "Start time should default to 8.");
        assertEquals(12, controller.endTimeSpinner.getValueFactory().getValue(), "Endtime should default to 12.");

        // Test CalendarApp initialization
        assertNotNull(controller.calendarApp, "calendarApp should be initialized.");
    }

    @Test
    public void testPreviousWeek() {
        // Set the weekDate to a known value
        LocalDate initialDate = LocalDate.of(2024, 1, 1);
        controller.weekDate = initialDate;

        // Call the method
        controller.previousWeek(mock(ActionEvent.class));

        // Check if weekDate was moved one week back
        assertEquals(initialDate.minusWeeks(1), controller.weekDate, "The weekDate should be one week earlier.");
        assertEquals("previousWeek pressed", controller.messageLabel.getText(), "Message label should be updated.");
    }

    @Test
    public void testNextWeek() {
        LocalDate initialDate = LocalDate.of(2024, 1, 1);
        controller.weekDate = initialDate;

        controller.nextWeek(mock(ActionEvent.class));

        assertEquals(initialDate.plusWeeks(1), controller.weekDate, "The weekDate should be one week later.");
        assertEquals("nextWeek pressed", controller.messageLabel.getText(), "Messagelabel should be updated.");

    }

    // Test if add event works

    @Test
    public void testAddEvent() {
        // Setup mock event data
        controller.eventNameField.setText("Test Event");
        controller.startDatePicker.setValue(LocalDate.of(2024, 1, 1));
        controller.endDatePicker.setValue(LocalDate.of(2024, 1, 1));
        controller.startTimeSpinner.getValueFactory().setValue(10);
        controller.endTimeSpinner.getValueFactory().setValue(11);

        int initialEventsAmount = mockCalendarApp.getEvents().size();

        // Call the method to add an event
        controller.addEventManually();

        verify(mockCalendarApp, times(1)).addEvent(any(Event.class));

        // Check if the event count increased
        assertEquals(initialEventsAmount + 1, mockCalendarApp.getEvents().size());
        // Check that the event was added to the UI
        // ObservableList<Node> children = controller.calendarGrid.getChildren();
        // assertTrue(children.size() > 0, "Event should be added to the grid.");
        // assertTrue(children.get(0) instanceof VBox, "Added node should be a VBox.");
        // VBox eventBox = (VBox) children.get(0);
        // assertTrue(eventBox.getChildren().get(0) instanceof Label, "VBox should
        // contain a Label.");
        // assertEquals("Test Event", ((Label) eventBox.getChildren().get(0)).getText(),
        // "Event name should match.");
    }

    @Test
    public void testClearCalendar() {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-border-color: red");
        vBox.getChildren().add(new Label("Test event"));
        vBox.getStyleClass().add("eventBox");
        vBox.setAlignment(Pos.CENTER);

        controller.calendarGrid.add(vBox, 0, 0);
        controller.clearCalendar();

        assertEquals(0, controller.calendarGrid.getChildren().size());
    }

    // test syncui

    // test handle back to login

}
