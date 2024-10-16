package calendar.ui;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import calendar.core.CalendarApp;
import calendar.core.Core;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;
import java.time.LocalDate;
import java.util.Optional;

public class CalendarControllerTest extends ApplicationTest {
    private CalendarController controller;
    private MockedStatic<Core> coreMock; // Static mock for Core

    @BeforeEach
    public void setUp() {
        // Initialize the controller and UI components
        controller = new CalendarController();
        controller.calendarGrid = new GridPane();
        controller.messageLabel = new Label();
        controller.startDatePicker = new DatePicker();
        controller.endDatePicker = new DatePicker();
        controller.eventNameField = new TextField();
        controller.startTimeSpinner = new Spinner<>();
        controller.endTimeSpinner = new Spinner<>();
        controller.weekLabel = new Label();

        // Mock CalendarApp and Core.getCalendarApp() static method
        CalendarApp mockCalendarApp = mock(CalendarApp.class);
        coreMock = mockStatic(Core.class); // Create static mock for Core
        coreMock.when(Core::getCalendarApp).thenReturn(Optional.of(mockCalendarApp));

        // Manually call the initialize method to set up the calendarApp and other
        // fields
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

}
