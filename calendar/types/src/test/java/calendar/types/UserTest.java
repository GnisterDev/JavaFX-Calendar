package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UserTest {
    private UUID uuid = UUID.randomUUID();
    private List<Calendar> calendars = new ArrayList<>();
    private UserSettings settings = new UserSettings(uuid);

    @Test
    public void testConstructor() {
        User user = new User(uuid, "username", calendars, settings);

        assertEquals(uuid, user.getUserId());
        assertEquals("username", user.getUsername());
        assertEquals(calendars, user.getCalendars());
        assertEquals(settings, user.getSettings());
    }

    @Test
    public void testFromRestUser() {
        RestUser restUser = new RestUser("username", "password");
        User user = new User(restUser);

        assertEquals(restUser.getUserId(), user.getUserId());
        assertEquals(restUser.getUsername(), user.getUsername());
        assertEquals(restUser.getCalendars(), user.getCalendars());
        assertEquals(restUser.getSettings(), user.getSettings());

    }
}
