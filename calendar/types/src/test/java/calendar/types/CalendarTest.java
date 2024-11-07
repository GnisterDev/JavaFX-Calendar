package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class CalendarTest {
    private UUID uuid = UUID.randomUUID();

    @Test
    public void testConstructor() {
        Calendar cal = new Calendar(uuid, "name");

        assertEquals(uuid, cal.getCalendarId());
        assertEquals("name", cal.getName());
    }

    @Test
    public void testFromRestCalendar() {
        RestCalendar restCal = new RestCalendar();
        Calendar cal = new Calendar(restCal);

        assertEquals(restCal.getCalendarId(), cal.getCalendarId());
        assertEquals(restCal.getName(), cal.getName());
    }
}
