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

    @Override
    public void start(Stage stage) throws Exception {
        mockEvent = mock(Event.class);
        mockCalendarController = mock(CalendarController.class);

        LocalDateTime startTime =
                LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0, 0));
        LocalDateTime endTime = startTime.plusHours(1);
        when(mockEvent.getTitle()).thenReturn("Test Event");
        when(mockEvent.getDescription()).thenReturn("Test description");
        when(mockEvent.getId()).thenReturn(UUID.randomUUID());
        when(mockEvent.getStartTime()).thenReturn(startTime);
        when(mockEvent.getEndTime()).thenReturn(endTime);
        when(mockEvent.getColor()).thenReturn(Color.RED);

        try (
                MockedStatic<RestHelper> mockedRestHelper =
                        mockStatic(RestHelper.class)) {
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

    @BeforeEach
    public void setUp() {
        // Initialize the popupController with test data
        LocalDateTime startTime =
                LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0, 0));
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
        verify(mockCalendarController).update();
    }

    // @Test
    // public void testHandleEdit_InvalidInputs() {
    //
    //     Platform.runLater(() -> {
    //         startDateSelect.setValue(LocalDate.now());
    //         endDateSelect.setValue(LocalDate.now().plusDays(1));
    //     });
    //
    //     clickOn(handleEdit);
    //
    //     // Verify that update() was never called on invalid input
    //     verify(mockCalendarController, never()).update();
    // }

    @Test
    public void testHandleDelete() {
        clickOn(handleDelete);

        verify(mockCalendarController).update();
    }

}
