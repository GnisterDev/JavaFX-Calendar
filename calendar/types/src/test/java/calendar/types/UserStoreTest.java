package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserStoreTest {
    private static Map<String, UUID> nameToId = new HashMap<>();
    private static Map<UUID, User> idToUser = new HashMap<>();

    private static User user1 = new User("user1", "passwd");
    private static User user2 = new User("user2", "passwd");
    private static User user3 = new User("user3", "passwd");

    @BeforeAll
    public static void setup() {
        nameToId.put(user1.getUsername(), user1.getUserId());
        nameToId.put(user2.getUsername(), user2.getUserId());
        idToUser.put(user1.getUserId(), user1);
        idToUser.put(user2.getUserId(), user2);
    }

    @Test
    public void testConstructor() {
        UserStore store = new UserStore(nameToId, idToUser);

        assertTrue(store.hasUsername(user1.getUsername()));
        assertTrue(store.hasUsername(user2.getUsername()));
        assertFalse(store.hasUsername(user3.getUsername()));

        assertTrue(store.hasUserId(user1.getUserId()));
        assertTrue(store.hasUserId(user2.getUserId()));
        assertFalse(store.hasUserId(user3.getUserId()));

        assertEquals(user1.getUserId(), store.getUserId(user1.getUsername()).get());
        assertEquals(user2.getUserId(), store.getUserId(user2.getUsername()).get());
        assertTrue(store.getUserId(user3.getUsername()).isEmpty());

        assertEquals(user1, store.getUser(user1.getUserId()).get());
        assertEquals(user2, store.getUser(user2.getUserId()).get());
        assertTrue(store.getUser(user3.getUserId()).isEmpty());

    }

}
