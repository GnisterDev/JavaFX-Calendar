package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.TimeZone;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UserSettingsTest {
    private UUID id = UUID.randomUUID();
    private TimeZone timezone = TimeZone.getDefault();

    @Test
    public void testConstructor() {
        UserSettings set = new UserSettings(id, timezone, true, false);

        assertEquals(id, set.getUserId());
        assertEquals(timezone, set.getTimezone());
        assertTrue(set.getMilitaryTime());
        assertFalse(set.getShowweekNr());
    }
}
