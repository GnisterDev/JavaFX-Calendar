package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.TimeZone;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for testing the {@link UserSettings} class.
 * <p>
 * This class verifies that the `UserSettings` object initializes correctly and
 * that
 * the getters for user preferences and settings return the expected values.
 */

public class UserSettingsTest {
    private UUID id = UUID.randomUUID();
    private TimeZone timezone = TimeZone.getDefault();

    /**
     * Tests the main constructor of the {@link UserSettings} class to ensure that
     * user settings are set correctly upon instantiation.
     * <p>
     * Verifies that each setting is initialized as expected, including:
     * <ul>
     * <li>User ID</li>
     * <li>Time zone</li>
     * <li>Military time setting</li>
     * <li>Show week number setting</li>
     * </ul>
     */
    @Test
    public void testConstructor() {
        UserSettings set = new UserSettings(id, timezone, true, false);

        assertEquals(id, set.getUserId());
        assertEquals(timezone, set.getTimezone());
        assertTrue(set.getMilitaryTime());
        assertFalse(set.getShowWeekNr());
    }
}
