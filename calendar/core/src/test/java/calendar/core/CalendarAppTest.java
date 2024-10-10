package calendar.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import calendar.types.Event;
import calendar.types.User;

public class CalendarAppTest {
    private Event event1 = new Event("event1", "", LocalDateTime.of(2024, 10, 8, 11, 0),
            LocalDateTime.of(2024, 10, 8, 14, 30));
    private Event event2 = new Event("event2", "", LocalDateTime.of(2024, 10, 8, 13, 0),
            LocalDateTime.of(2024, 10, 10, 19, 0));
    private Event event3 = new Event("event3", "", LocalDateTime.of(2024, 10, 9, 10, 0),
            LocalDateTime.of(2024, 10, 11, 11, 0));

    @Test
    public void testContructor() {
        User user = new User("username", "password");
        CalendarApp calApp = new CalendarApp(user);

        assertEquals(user, calApp.user);
    }

    @Test
    public void testModifyEvents() {
        User user = new User("username", "password");
        CalendarApp calApp = new CalendarApp(user);

        assertTrue(user.getCalendar(0).getEvents().isEmpty());
        assertTrue(calApp.getEvents().isEmpty());

        calApp.addEvent(event1);

        assertEquals(1, user.getCalendar(0).getEvents().size());
        assertEquals(1, calApp.getEvents().size());
        assertTrue(calApp.getEvents().containsAll(user.getCalendar(0).getEvents()));
        assertTrue(calApp.getEvents().contains(event1));

        calApp.addEvent(event2);

        assertEquals(2, user.getCalendar(0).getEvents().size());
        assertEquals(2, calApp.getEvents().size());
        assertTrue(calApp.getEvents().containsAll(user.getCalendar(0).getEvents()));
        assertTrue(calApp.getEvents().containsAll(List.of(event1, event2)));

        calApp.removeEvent(event1);

        assertEquals(1, user.getCalendar(0).getEvents().size());
        assertEquals(1, calApp.getEvents().size());
        assertTrue(calApp.getEvents().containsAll(user.getCalendar(0).getEvents()));
        assertTrue(calApp.getEvents().contains(event2));

        calApp.addEvent(event3);

        assertEquals(2, user.getCalendar(0).getEvents().size());
        assertEquals(2, calApp.getEvents().size());
        assertTrue(calApp.getEvents().containsAll(user.getCalendar(0).getEvents()));
        assertTrue(calApp.getEvents().containsAll(List.of(event2, event3)));

    }

    @Test
    public void testEventsBetween() {
        User user = new User("username", "password");
        CalendarApp calApp = new CalendarApp(user);
        calApp.addEvent(event1);
        calApp.addEvent(event2);
        calApp.addEvent(event3);

        List<Event> events = calApp.getEventsBetween(LocalDateTime.of(2020, 10, 10, 10, 10),
                LocalDateTime.of(2021, 10, 10, 10, 10));

        assertTrue(events.isEmpty());

        events = calApp.getEventsBetween(LocalDateTime.of(2020, 10, 10, 10, 10), LocalDateTime.of(2024, 10, 8, 12, 0));

        assertEquals(1, events.size());
        assertTrue(events.contains(event1));

        events = calApp.getEventsBetween(LocalDateTime.of(2024, 10, 8, 19, 0), LocalDateTime.of(2024, 11, 1, 1, 1));

        assertEquals(2, events.size());
        assertTrue(events.containsAll(List.of(event2, event3)));

        events = calApp.getEventsBetween(LocalDateTime.of(2024, 10, 8, 13, 0), LocalDateTime.of(2024, 10, 9, 10, 1));

        assertEquals(3, events.size());
        assertTrue(events.containsAll(List.of(event1, event2, event3)));
    }
}
