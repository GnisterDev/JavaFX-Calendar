package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserStoreTest {
    private Map<String, UUID> nameToId = new HashMap<>();
    private Map<UUID, RestUser> idToUser = new HashMap<>();

    private RestUser user1 = new RestUser("user1", "passwd");
    private RestUser user2 = new RestUser("user2", "passwd");
    private RestUser user3 = new RestUser("user3", "passwd");

    @BeforeEach
    public void setup() {
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

        assertTrue(store.hasUser(user1));
        assertTrue(store.hasUser(user2));
        assertFalse(store.hasUser(user3));

        assertEquals(user1.getUserId(), store.getUserId(user1.getUsername()).get());
        assertEquals(user2.getUserId(), store.getUserId(user2.getUsername()).get());
        assertTrue(store.getUserId(user3.getUsername()).isEmpty());

        assertEquals(user1, store.getUser(user1.getUserId()).get());
        assertEquals(user2, store.getUser(user2.getUserId()).get());
        assertTrue(store.getUser(user3.getUserId()).isEmpty());
    }

    @Test
    public void testImmutability() {
        UserStore store = new UserStore(nameToId, idToUser);

        nameToId.remove(user1.getUsername());
        assertTrue(store.hasUsername(user1.getUsername()));

        idToUser.remove(user1.getUserId());
        assertTrue(store.hasUserId(user1.getUserId()));

        nameToId.put(user3.getUsername(), user3.getUserId());
        assertFalse(store.hasUsername(user3.getUsername()));

        idToUser.put(user3.getUserId(), user3);
        assertFalse(store.hasUserId(user3.getUserId()));
    }

    @Test
    public void testModifyUsers() {
        UserStore store = new UserStore(nameToId, idToUser);

        assertTrue(store.hasUser(user1));
        assertTrue(store.hasUser(user2));
        assertFalse(store.hasUser(user3));

        assertFalse(store.addUser(user1));
        assertTrue(store.hasUser(user1));
        assertTrue(store.hasUser(user2));
        assertFalse(store.hasUser(user3));

        assertTrue(store.addUser(user3));
        assertTrue(store.hasUser(user1));
        assertTrue(store.hasUser(user2));
        assertTrue(store.hasUser(user3));

        assertTrue(store.removeUser(user1.getUsername()));
        assertFalse(store.hasUser(user1));
        assertTrue(store.hasUser(user2));
        assertTrue(store.hasUser(user3));

        assertTrue(store.removeUser(user2.getUserId()));
        assertFalse(store.hasUser(user1));
        assertFalse(store.hasUser(user2));
        assertTrue(store.hasUser(user3));

        assertTrue(store.removeUser(user3));
        assertFalse(store.hasUser(user1));
        assertFalse(store.hasUser(user2));
        assertFalse(store.hasUser(user3));

        assertFalse(store.removeUser(user1.getUsername()));
        assertFalse(store.hasUser(user1));
        assertFalse(store.hasUser(user2));
        assertFalse(store.hasUser(user3));

        assertFalse(store.removeUser(user2.getUserId()));
        assertFalse(store.hasUser(user1));
        assertFalse(store.hasUser(user2));
        assertFalse(store.hasUser(user3));

        assertFalse(store.removeUser(user3));
        assertFalse(store.hasUser(user1));
        assertFalse(store.hasUser(user2));
        assertFalse(store.hasUser(user3));

        assertTrue(store.addUser(user2));
        assertFalse(store.hasUser(user1));
        assertTrue(store.hasUser(user2));
        assertFalse(store.hasUser(user3));
    }
}
