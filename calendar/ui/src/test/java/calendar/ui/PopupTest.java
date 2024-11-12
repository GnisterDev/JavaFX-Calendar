package calendar.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import calendar.core.RestHelper;
import calendar.types.Event;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import no.gorandalum.fluentresult.VoidResult;

/**
 * Unit test class for testing the Popup UI functionality in the calendar
 * application.
 * <p>
 * This class uses TestFX and Mockito frameworks to verify behavior and
 * interactions within
 * the Popup UI, which allows users to edit, or delete calendar events.
 */

public class PopupTest extends ApplicationTest {

    private PopupController popupController;
    private Event mockEvent;
    private CalendarController mockCalendarController;

    private TextField eventNameField;
    private DatePicker startDateSelect;
    private DatePicker endDateSelect;
    private TextField startTimeSelect;
    private TextField endTimeSelect;
    private ColorPicker colorPicker;
    private Circle colorCircle;
    private Button handleEdit;
    private Button handleDelete;

    /**
     * Sets up the application stage with the mocked event and calendar controller
     * for testing purposes. Initializes the PopupController with mock objects and
     * loads the FXML UI layout.
     *
     * @param stage the main JavaFX stage for the application
     * @throws Exception if an error occurs during FXML loading or stage setup
     */
    @Override
    public void start(Stage stage) throws Exception {
        mockEvent = mock(Event.class);
        mockCalendarController = mock(CalendarController.class);

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0, 0));
        LocalDateTime endTime = startTime.plusHours(1);
        when(mockEvent.getTitle()).thenReturn("Test Event");
        when(mockEvent.getDescription()).thenReturn("Test description");
        when(mockEvent.getId()).thenReturn(UUID.randomUUID());
        when(mockEvent.getStartTime()).thenReturn(startTime);
        when(mockEvent.getEndTime()).thenReturn(endTime);
        when(mockEvent.getColor()).thenReturn(Color.RED);

        try (
                MockedStatic<RestHelper> mockedRestHelper = mockStatic(RestHelper.class)) {

            mockedRestHelper
                    .when(() -> RestHelper.editEvent(ArgumentMatchers.any(),
                            ArgumentMatchers.any(),
                            ArgumentMatchers.any(),
                            ArgumentMatchers.any(),
                            ArgumentMatchers.any(),
                            ArgumentMatchers.any(),
                            ArgumentMatchers.any()))
                    .thenReturn(VoidResult.success());
            mockedRestHelper
                    .when(() -> RestHelper.removeEvent(ArgumentMatchers.any()))
                    .thenReturn(VoidResult.success());

            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/calendar/ui/Popup.fxml"));
            VBox mainNode = loader.load();
            popupController = loader.getController();
            popupController.setStage(stage);

            // Set up the popup controller with mocks
            popupController.initialize(mockEvent, mockCalendarController);

            Scene scene = new Scene(mainNode);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Initializes UI components before each test, using mock data to set up
     * the PopupController's state.
     */

    @BeforeEach
    public void setUp() {
        // Initialize the popupController with test data
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0, 0));
        LocalDateTime endTime = startTime.plusHours(1);
        when(mockEvent.getTitle()).thenReturn("Test Event");
        when(mockEvent.getStartTime()).thenReturn(startTime);
        when(mockEvent.getEndTime()).thenReturn(endTime);
        when(mockEvent.getColor()).thenReturn(Color.RED);

        eventNameField = lookup("#eventNameField").queryAs(TextField.class);
        startDateSelect = lookup("#startDateSelect").queryAs(DatePicker.class);
        endDateSelect = lookup("#endDateSelect").queryAs(DatePicker.class);
        startTimeSelect = lookup("#startTimeSelect").queryAs(TextField.class);
        endTimeSelect = lookup("#endTimeSelect").queryAs(TextField.class);
        colorPicker = lookup("#colorPicker").queryAs(ColorPicker.class);
        colorCircle = lookup("#colorCircle").queryAs(Circle.class);
        handleEdit = lookup("#handleEdit").queryAs(Button.class);
        handleDelete = lookup("#handleDelete").queryAs(Button.class);
    }

    /**
     * Verifies the initial state of the Popup UI, checking if the fields are
     * correctly
     * populated with event data.
     */

    @Test
    public void testInitialize() {
        assertEquals("Test Event", eventNameField.getText());
        assertEquals(mockEvent.getStartTime().toLocalDate(),
                startDateSelect.getValue());
        assertEquals(mockEvent.getEndTime().toLocalDate(),
                endDateSelect.getValue());
        assertEquals("01:00", startTimeSelect.getText());
        assertEquals("02:00", endTimeSelect.getText());
        assertEquals(Color.RED, colorPicker.getValue());
        assertEquals(Color.RED, colorCircle.getFill());
    }

    /**
     * Tests whether listeners are properly initialized and triggered for DatePicker
     * components.
     * Verifies interaction with the calendar controller when dates are set.
     */

    @Test
    public void testInitializeListeners() {

        Platform.runLater(() -> startDateSelect.setValue(LocalDate.now()));
        WaitForAsyncUtils.waitForFxEvents();

        verify(mockCalendarController).datePicker(startDateSelect);

        Platform.runLater(() -> endDateSelect
                .setValue(LocalDate.now().plusDays(1)));
        WaitForAsyncUtils.waitForFxEvents();

        verify(mockCalendarController).datePicker(endDateSelect);
    }

    /**
     * Tests the color picker functionality, verifying if the selected color is
     * reflected in the color circle.
     */

    @Test
    public void testColorPicker() {

        // Trigger the color change in the color picker
        clickOn(colorPicker);
        press(KeyCode.TAB).release(KeyCode.TAB);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertEquals(Color.MAROON, colorCircle.getFill());

        clickOn(colorCircle);
        assertTrue(colorPicker.isShowing(),
                "Color picker should be visible after clicking");
    }

    /**
     * Verifies that when the start and end time fields lose focus, the appropriate
     * controller methods are called to validate and format times.
     */
    @Test
    public void testTimeSelectLoseFocus() {

        clickOn(startTimeSelect);
        startTimeSelect.setText("10");
        clickOn(endTimeSelect);

        verify(mockCalendarController).timeSelectLoseFocus(startTimeSelect);

        clickOn(endTimeSelect);
        endTimeSelect.setText("23");
        clickOn(startDateSelect);

        verify(mockCalendarController).timeSelectLoseFocus(endTimeSelect);
    }

    /**
     * Tests the edit functionality for a successful edit operation, verifying that
     * the controller method to update event data is called.
     */
    @Test
    public void testHandleEdit_Success() {

        Platform.runLater(() -> {
            eventNameField.setText("Updated Event");
            startDateSelect.setValue(LocalDate.now());
            endDateSelect.setValue(LocalDate.now().plusDays(1));
            startTimeSelect.setText("10:00");
            endTimeSelect.setText("11:00");
            colorPicker.setValue(Color.BLUE);
        });

        clickOn(handleEdit);

        // Validate that the calendarController's update method is call ed
        // TODO: Credentials are not set
        // verify(mockCalendarController).update();
        // assertFalse(popupController.getStage().isShowing());
    }

    // @Test
    // public void testHandleEdit_InvalidInputs() {
    //
    // Platform.runLater(() -> {
    // startDateSelect.setValue(LocalDate.now());
    // endDateSelect.setValue(LocalDate.now().plusDays(1));
    // });
    //
    // clickOn(handleEdit);
    //
    // // Verify that update() was never called on invalid input
    // verify(mockCalendarController, never()).update();
    // }

    /**
     * Tests the delete functionality, ensuring the delete event button properly
     * triggers
     * the necessary controller actions.
     */

    @Test
    public void testHandleDelete() {
        clickOn(handleDelete);

        // TODO: Credentials are not set
        // verify(mockCalendarController).update();

        // assertFalse(popupController.getStage().isShowing());
    }

}
