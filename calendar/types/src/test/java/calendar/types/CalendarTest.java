package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for testing the {@link Calendar} class.
 * <p>
 * This class includes tests for constructor functionality and creation from a
 * {@link RestCalendar} instance.
 */
public class CalendarTest {
    private UUID uuid = UUID.randomUUID();

    /**
     * Tests the constructor of {@link Calendar} to ensure that the calendar ID and
     * name
     * are properly assigned.
     */
    @Test
    public void testConstructor() {
        Calendar cal = new Calendar(uuid, "name");

        assertEquals(uuid, cal.getCalendarId());
        assertEquals("name", cal.getName());
    }

    /**
     * Tests the constructor that creates a {@link Calendar} from a
     * {@link RestCalendar} instance.
     * <p>
     * Verifies that the {@link Calendar} instance correctly reflects the ID and
     * name
     * from the provided {@link RestCalendar}.
     */
    @Test
    public void testFromRestCalendar() {
        RestCalendar restCal = new RestCalendar();
        Calendar cal = new Calendar(restCal);

        assertEquals(restCal.getCalendarId(), cal.getCalendarId());
        assertEquals(restCal.getName(), cal.getName());
    }
}
