package calendar.types;

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

public class UserTest {
    private UUID id = UUID.randomUUID();
    private Calendar cal1 = new Calendar();
    private Calendar cal2 = new Calendar();

    @Test
    public void testConstructors() {
        User user1 = new User("username", "password");

        assertEquals("username", user1.getUsername());
        assertTrue(user1.getCalendars().isEmpty());
        assertEquals(0, user1.calendarCount());
        assertThrows(IndexOutOfBoundsException.class, () -> user1.getCalendar(1));

        User user2 = new User(id, "username", "password", List.of(cal1, cal2));

        assertEquals(id, user2.getUserId());
        assertEquals("username", user2.getUsername());
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
        User user1 = new User(id, "username", "password", calendars);
        assertEquals(calendars, user1.getCalendars());
        calendars.remove(1);
        assertNotEquals(calendars, user1.getCalendars());

        // User does not mutate calendar list
        User user2 = new User(id, "username", "password", calendars);
        assertEquals(calendars, user2.getCalendars());
        user2.addCalendar(cal1);
        assertNotEquals(calendars, user2.getCalendars());
    }
}
