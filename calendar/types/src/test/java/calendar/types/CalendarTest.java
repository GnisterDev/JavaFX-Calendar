package calendar.types;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class CalendarTest {
    private Event event = new Event("title", "description", new Date(), new Date(123123));
    private UUID id = UUID.randomUUID();

    @Test
    public void testConstructors() {
        Calendar cal1 = new Calendar();
        Calendar cal2 = new Calendar(List.of(event));
        Calendar cal3 = new Calendar(id, List.of(event));

        assertTrue(cal1.getEvents().isEmpty());
        assertEquals(0, cal1.eventCount());
        assertThrows(IndexOutOfBoundsException.class, () -> cal1.getEvent(1));
        assertThrows(IndexOutOfBoundsException.class, () -> cal1.removeEvent(1));
        assertDoesNotThrow(() -> cal1.removeEvent(event));

        assertNotEquals(cal1.getUserId(), cal2.getUserId());

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
        eventList.add(new Event("title2", "description2", new Date(), new Date(123123)));

        // Eventlist does not mutate calendar
        Calendar cal1 = new Calendar(eventList);
        assertEquals(eventList, cal1.getEvents());
        assertEquals(eventList.size(), cal1.eventCount());
        eventList.add(new Event("title3", "description3", new Date(), new Date(123123)));
        assertNotEquals(eventList, cal1.getEvents());
        assertNotEquals(eventList.size(), cal1.eventCount());

        // Calendar does not mutate eventlist
        Calendar cal2 = new Calendar(eventList);
        assertEquals(eventList, cal2.getEvents());
        assertEquals(eventList.size(), cal2.eventCount());
        cal2.addEvent(event);
        assertNotEquals(eventList, cal2.getEvents());
        assertNotEquals(eventList.size(), cal2.eventCount());
    }
}
