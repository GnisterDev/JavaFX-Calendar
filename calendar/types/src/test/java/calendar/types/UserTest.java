package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UserTest {
    private UUID id = UUID.randomUUID();
    private Calendar cal1 = new Calendar();
    private Calendar cal2 = new Calendar();
    private UserSettings set = new UserSettings(id);

    @Test
    public void testConstructors() {
        User user1 = new User("username", "password");

        assertEquals("username", user1.getUsername());
        assertEquals(1, user1.getCalendars().size());
        assertNotNull(user1.getSettings());
        assertEquals(1, user1.calendarCount());
        assertThrows(IndexOutOfBoundsException.class, () -> user1.getCalendar(1));

        User user2 = new User(id, "username", "password", List.of(cal1, cal2), set);

        assertEquals(id, user2.getUserId());
        assertEquals("username", user2.getUsername());
        assertEquals(set, user2.getSettings());
        assertEquals(2, user2.calendarCount());
        assertEquals(cal1, user2.getCalendar(0));

        assertNotEquals(user1.getUserId(), user2.getUserId());
    }

    @Test
    public void testPasswordCheck() {
        User user = new User("", "password");

        assertTrue(user.checkPassword("password"));
        assertFalse(user.checkPassword("drowssap"));
    }

    @Test
    public void testImmutability() {
        List<Calendar> calendars = new ArrayList<>(List.of(cal1, cal2));

        // Calendar list does not mutate User
        User user1 = new User(id, "username", "password", calendars, set);
        assertEquals(calendars, user1.getCalendars());
        calendars.remove(1);
        assertNotEquals(calendars, user1.getCalendars());

        // User does not mutate calendar list
        User user2 = new User(id, "username", "password", calendars, set);
        assertEquals(calendars, user2.getCalendars());
        user2.addCalendar(cal1);
        assertNotEquals(calendars, user2.getCalendars());
    }

    @Test
    public void testModifyCalendars() {
        User user = new User("username", "password");

        user.addCalendar(cal1);
        assertEquals(2, user.calendarCount());
        assertEquals(cal1, user.getCalendar(1));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(2));

        user.addCalendar(cal2);
        assertEquals(3, user.calendarCount());
        assertEquals(cal1, user.getCalendar(1));
        assertEquals(cal2, user.getCalendar(2));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(3));

        user.removeCalendar(1);
        assertEquals(2, user.calendarCount());
        assertEquals(cal2, user.getCalendar(1));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(2));

        user.removeCalendar(cal1);
        assertEquals(2, user.calendarCount());
        assertEquals(cal2, user.getCalendar(1));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(2));

        user.removeCalendar(cal2);
        assertEquals(1, user.calendarCount());
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(1));
    }
}
