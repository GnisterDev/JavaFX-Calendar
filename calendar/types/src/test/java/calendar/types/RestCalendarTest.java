package calendar.types;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class RestCalendarTest {
    private Event event = new Event("title", "description", LocalDateTime.of(2024, 10, 7, 10, 30),
            LocalDateTime.of(2024, 10, 10, 14, 50));
    private UUID id = UUID.randomUUID();

    @Test
    public void testConstructors() {
        RestCalendar cal1 = new RestCalendar();
        RestCalendar cal2 = new RestCalendar(List.of(event));
        RestCalendar cal3 = new RestCalendar(id, "name", List.of(event));

        assertTrue(cal1.getEvents().isEmpty());
        assertEquals(0, cal1.eventCount());
        assertThrows(IndexOutOfBoundsException.class, () -> cal1.getEvent(1));
        assertThrows(IndexOutOfBoundsException.class, () -> cal1.removeEvent(1));
        assertDoesNotThrow(() -> cal1.removeEvent(event));

        assertNotEquals(cal1.getCalendarId(), cal2.getCalendarId());

        assertFalse(cal2.getEvents().isEmpty());
        assertEquals(1, cal2.eventCount());
        assertEquals(event, cal2.getEvent(0));
        assertDoesNotThrow(() -> cal2.removeEvent(0));
        assertTrue(cal2.getEvents().isEmpty());
        assertEquals(0, cal2.eventCount());

        assertFalse(cal3.getEvents().isEmpty());
        assertEquals(1, cal3.eventCount());
        assertEquals(event, cal3.getEvent(0));
        assertDoesNotThrow(() -> cal3.removeEvent(event));
        assertTrue(cal3.getEvents().isEmpty());
        assertEquals(0, cal3.eventCount());
    }

    @Test
    public void testImmutability() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        eventList.add(new Event("title2", "description2", LocalDateTime.of(2024, 10, 7, 10, 30),
                LocalDateTime.of(2024, 10, 10, 14, 50)));

        // Eventlist does not mutate calendar
        RestCalendar cal1 = new RestCalendar(eventList);
        assertEquals(eventList, cal1.getEvents());
        assertEquals(eventList.size(), cal1.eventCount());
        eventList.add(new Event("title3", "description3", LocalDateTime.of(2024, 10, 7, 10, 30),
                LocalDateTime.of(2024, 10, 10, 14, 50)));
        assertNotEquals(eventList, cal1.getEvents());
        assertNotEquals(eventList.size(), cal1.eventCount());

        // Calendar does not mutate eventlist
        RestCalendar cal2 = new RestCalendar(eventList);
        assertEquals(eventList, cal2.getEvents());
        assertEquals(eventList.size(), cal2.eventCount());
        cal2.addEvent(event);
        assertNotEquals(eventList, cal2.getEvents());
        assertNotEquals(eventList.size(), cal2.eventCount());
    }
}
