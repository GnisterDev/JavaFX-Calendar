package calendar.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import calendar.types.User;
import calendar.types.UserStore;

public class CoreTest {
    private User user1 = new User("user1", "password1");
    private User user2 = new User("user2", "password2");


    @BeforeEach
    public void initialize() {

        Core.userStore = new UserStore(new HashMap<>(), new HashMap<>());
        Core.userStore.addUser(user1);
        Core.userStore.addUser(user2);
    }

    @Test
    public void testCredentials() {
        assertTrue(Core.correctCredentials("user1", "password1"));
        assertFalse(Core.correctCredentials("user1", "password2"));
        assertTrue(Core.correctCredentials("user2", "password2"));
        assertFalse(Core.correctCredentials("user2", "password1"));
        assertFalse(Core.correctCredentials("user3", "password3"));
    }

    @Test
    public void testRegister() {
        assertTrue(Core.registerUser("user3", "password3"));
        assertTrue(Core.correctCredentials("user3", "password3"));
        assertFalse(Core.correctCredentials("user3", "password2"));

        assertFalse(Core.registerUser("user1", ""));
    }

    @Test
    public void testLogin() {
        assertThrows(NoSuchElementException.class, () -> Core.logInAsUser("user3"));

        Core.logInAsUser("user1");
        Optional<CalendarApp> calApp = Core.getCalendarApp();
        assertTrue(calApp.isPresent());
        assertEquals(user1, calApp.get().user);

        Core.logInAsUser(user2.getUserId());
        calApp = Core.getCalendarApp();
        assertTrue(calApp.isPresent());
        assertEquals(user2, calApp.get().user);

    }
}
