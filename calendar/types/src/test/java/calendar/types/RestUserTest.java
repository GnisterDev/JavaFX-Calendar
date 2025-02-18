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

/**
 * Unit test class for testing the {@link RestUser} class.
 * <p>
 * This class tests the functionality of the `RestUser` class, focusing on
 * object construction, password verification, immutability, and calendar
 * modification.
 */

public class RestUserTest {
    private UUID id = UUID.randomUUID();
    private RestCalendar cal1 = new RestCalendar();
    private RestCalendar cal2 = new RestCalendar();
    private UserSettings set = new UserSettings(id);

    /**
     * Tests the constructors of the {@link RestUser} class.
     * <p>
     * Verifies correct initialization of username, password, associated settings,
     * and calendars. Also checks that the unique IDs are correctly generated and
     * that
     * an exception is thrown when attempting to access a non-existent calendar.
     */
    @Test
    public void testConstructors() {
        RestUser user1 = new RestUser("username", "password");

        assertEquals("username", user1.getUsername());
        assertTrue(user1.getCalendars().isEmpty());
        assertNotNull(user1.getSettings());
        assertEquals(0, user1.calendarCount());
        assertThrows(IndexOutOfBoundsException.class, () -> user1.getCalendar(1));

        RestUser user2 = new RestUser(id, "username", "password", List.of(cal1, cal2), set);

        assertEquals(id, user2.getUserId());
        assertEquals("username", user2.getUsername());
        assertEquals(set, user2.getSettings());
        assertEquals(2, user2.calendarCount());
        assertEquals(cal1, user2.getCalendar(0));

        assertNotEquals(user1.getUserId(), user2.getUserId());
    }

    /**
     * Tests the password verification feature of {@link RestUser}.
     * <p>
     * Ensures that the correct password returns `true`, while an incorrect
     * password returns `false`.
     */
    @Test
    public void testPasswordCheck() {
        RestUser user = new RestUser("", "password");

        assertTrue(user.checkPassword("password"));
        assertFalse(user.checkPassword("drowssap"));
    }

    /**
     * Tests the immutability of the calendar list within {@link RestUser}.
     * <p>
     * Ensures that external modifications to the list of calendars do not
     * impact the internal list of {@link RestUser}. Also verifies that internal
     * modifications within `RestUser` do not alter external list references.
     */
    @Test
    public void testImmutability() {
        List<RestCalendar> calendars = new ArrayList<>(List.of(cal1, cal2));

        // Calendar list does not mutate User
        RestUser user1 = new RestUser(id, "username", "password", calendars, set);
        assertEquals(calendars, user1.getCalendars());
        calendars.remove(1);
        assertNotEquals(calendars, user1.getCalendars());

        // User does not mutate calendar list
        RestUser user2 = new RestUser(id, "username", "password", calendars, set);
        assertEquals(calendars, user2.getCalendars());
        user2.addCalendar(cal1);
        assertNotEquals(calendars, user2.getCalendars());
    }

    /**
     * Tests the modification methods for calendars in {@link RestUser}.
     * <p>
     * Verifies adding, removing, and retrieving calendars and checks for
     * exceptions when accessing non-existent indexes.
     */
    @Test
    public void testModifyCalendars() {
        RestUser user = new RestUser("username", "password");

        user.addCalendar(cal1);
        assertEquals(1, user.calendarCount());
        assertEquals(cal1, user.getCalendar(0));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(2));

        user.addCalendar(cal2);
        assertEquals(2, user.calendarCount());
        assertEquals(cal1, user.getCalendar(0));
        assertEquals(cal2, user.getCalendar(1));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(3));

        user.removeCalendar(0);
        assertEquals(1, user.calendarCount());
        assertEquals(cal2, user.getCalendar(0));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(2));

        user.removeCalendar(cal1);
        assertEquals(1, user.calendarCount());
        assertEquals(cal2, user.getCalendar(0));
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(2));

        user.removeCalendar(cal2);
        assertEquals(0, user.calendarCount());
        assertThrows(IndexOutOfBoundsException.class, () -> user.getCalendar(1));
    }
}
