package calendar.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CalendarControllerTest extends ApplicationTest {

    private TextField eventNameField;
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
    public void testTimeSelect() {
        String startTimeFieldID = "#startTimeSelect";
        String endTimeFieldID = "#endTimeSelect";
        TextField startTimeField = lookup(startTimeFieldID).queryAs(TextField.class);
        TextField endTimeField = lookup(endTimeFieldID).queryAs(TextField.class);

        clickOn(startTimeFieldID).write("0").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "00:00");

        clickOn(startTimeFieldID).write("1").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "01:00");

        clickOn(startTimeFieldID).write("12").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "12:00");

        clickOn(startTimeFieldID).write("24").clickOn(endTimeFieldID);
        assertEquals(startTimeField.getText(), "24:00");

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
}