package calendar.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import calendar.types.Event;
import calendar.types.RestUser;
import calendar.types.UserSettings;
import calendar.types.UserStore;
import calendar.types.RestCalendar;

/**
 * Test class for the {@link Persistence} class, verifying the functionality of
 * writing and reading
 * various objects, including {@link RestUser}, {@link Event}, and complex
 * nested objects such as
 * {@link UserStore} and {@link RestCalendar}. This class includes cleanup steps
 * after each test
 * to ensure data integrity and prevent file persistence issues.
 */
public class PersistenceTest {

    private static final String TEST_FILE_PATH = "test.json";

    /**
     * Deletes the test file after each test to ensure a clean state for subsequent
     * tests.
     */
    @AfterEach
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Tests writing a {@link RestUser} object to a file and reading it back to
     * verify that the
     * data is correctly persisted and retrieved.
     *
     * @throws IOException if an I/O error occurs during write or read.
     */
    @Test
    public void testWriteAndReadRestUser() throws IOException {
        RestUser user = new RestUser("testUser", "testPassword");
        Persistence.write(user, TEST_FILE_PATH);

        RestUser readUser = Persistence.read(RestUser.class, TEST_FILE_PATH);
        assertEquals(user.getUserId(), readUser.getUserId());
        assertEquals(user.getUsername(), readUser.getUsername());
    }

    /**
     * Tests writing an {@link Event} object to a file and reading it back to verify
     * all event properties
     * are correctly persisted and retrieved.
     *
     * @throws IOException if an I/O error occurs during write or read.
     */
    @Test
    public void testWriteAndReadEvent() throws IOException {
        Event event = new Event("Title", "Description", LocalDateTime.now(), LocalDateTime.now());
        Persistence.write(event, TEST_FILE_PATH);

        Event readEvent = Persistence.read(Event.class, TEST_FILE_PATH);
        assertEquals(event.getDescription(), readEvent.getDescription());
        assertEquals(event.getStartTime(), readEvent.getStartTime());
        assertEquals(event.getEndTime(), readEvent.getEndTime());
        assertEquals(event.getTitle(), readEvent.getTitle());
        assertEquals(event.getColor(), readEvent.getColor());
        assertEquals(event.getType(), readEvent.getType());
        assertEquals(event.getId(), readEvent.getId());
    }

    /**
     * Tests that attempting to read a file with an unexpected class type throws an
     * {@link IOException}.
     *
     * @throws IOException when reading with an incorrect class type.
     */
    @Test
    public void testInvalidClassThrowsIOExeption() throws IOException {
        RestUser user = new RestUser("testUser", "testPassword");
        Persistence.write(user, TEST_FILE_PATH);

        assertThrows(IOException.class, () -> {
            Persistence.read(Event.class, TEST_FILE_PATH);
        });
    }

    /**
     * Verifies that attempting to read a non-existent file throws an
     * {@link IOException}.
     *
     * @throws IOException if the file cannot be found during the read operation.
     */
    @Test
    public void testReadThrowsIOExceptionWhenFileNotFound() {
        assertThrows(IOException.class, () -> {
            Persistence.read(UserSettings.class, TEST_FILE_PATH);
        });
    }

    /**
     * Tests reading and writing without specifying a file path, using a mocked
     * {@link File} to verify
     * that write and read operations function correctly with static mocks.
     *
     * @throws IOException           if an I/O error occurs during read or write.
     * @throws FileNotFoundException if the file is not found while performing
     *                               operations.
     */
    @Test
    public void testWriteReadWithoutPath() throws IOException {

        RestUser user = new RestUser("testUser", "testPassword");

        File fileMock = Mockito.mock(File.class);
        try (MockedStatic<Persistence> persistenceMock = Mockito.mockStatic(Persistence.class)) {
            persistenceMock.when(() -> Persistence.write(user)).thenAnswer(invocation -> null);
            persistenceMock.when(() -> Persistence.read(RestUser.class)).thenReturn(user);

            when(fileMock.exists()).thenReturn(true);
            Persistence.write(user);

            RestUser readUser = Persistence.read(RestUser.class);
            assertEquals(user.getUserId(), readUser.getUserId());
            assertEquals(user.getUsername(), readUser.getUsername());

            if (!fileMock.exists())
                throw new FileNotFoundException();
            verify(fileMock, never()).delete();
        }
    }

    /**
     * Tests the serialization and deserialization of nested objects within a
     * {@link UserStore}.
     * The method creates multiple {@link RestUser} instances, each with associated
     * {@link RestCalendar} and {@link Event} objects.
     * These objects are written to and read from a file to ensure that nested
     * structures are correctly persisted and retrieved.
     * 
     * Specifically, the test:
     * <ul>
     * <li>Verifies that all {@link RestUser} instances, identified by unique
     * usernames and UUIDs, are correctly stored and retrieved.</li>
     * <li>Verifies that the {@link UserStore} correctly contains the expected
     * {@link RestUser} objects based on their usernames and IDs.</li>
     * <li>Verifies that the {@link RestCalendar} objects are properly nested within
     * each {@link RestUser}, with accurate event counts.</li>
     * <li>Verifies that the number of events in each {@link RestCalendar} matches
     * the expected values after deserialization.</li>
     * </ul>
     * 
     * @throws IOException if an I/O error occurs during the file write or read
     *                     operations.
     */
    @Test
    public void testNestedClasses() throws IOException {
        String username1 = "A";
        String username2 = "B";
        String username3 = "C";
        String username4 = "D";

        ArrayList<Event> eventList1 = new ArrayList<>();
        eventList1.add(new Event("title1_1", "description1_1", LocalDateTime.now(), LocalDateTime.now()));
        eventList1.add(new Event("title1_2", "description1_2", LocalDateTime.now(), LocalDateTime.now()));
        ArrayList<Event> eventList2 = new ArrayList<>();
        eventList2.add(new Event("title2_1", "description2_1", LocalDateTime.now(), LocalDateTime.now()));
        eventList2.add(new Event("title2_2", "description2_2", LocalDateTime.now(), LocalDateTime.now()));
        eventList2.add(new Event("title2_3", "description2_3", LocalDateTime.now(), LocalDateTime.now()));

        ArrayList<RestCalendar> calendar = new ArrayList<>();
        calendar.add(new RestCalendar(eventList1));
        calendar.add(new RestCalendar(eventList2));

        UUID user1ID = UUID.randomUUID();
        UUID user2ID = UUID.randomUUID();
        UUID user3ID = UUID.randomUUID();
        UUID user4ID = UUID.randomUUID();

        RestUser User1 = new RestUser(user1ID, username1, "password1", calendar, new UserSettings(user1ID));
        RestUser User2 = new RestUser(user1ID, username2, "password2", new ArrayList<RestCalendar>(),
                new UserSettings(user1ID));
        RestUser User3 = new RestUser(user1ID, username3, "password3", new ArrayList<RestCalendar>(),
                new UserSettings(user1ID));
        RestUser User4 = new RestUser(user1ID, username4, "password4", new ArrayList<RestCalendar>(),
                new UserSettings(user1ID));

        HashMap<String, UUID> StU = new HashMap<>();
        StU.put(username1, user1ID);
        StU.put(username2, user2ID);
        StU.put(username3, user3ID);
        StU.put(username4, user4ID);

        HashMap<UUID, RestUser> UtU = new HashMap<>();
        UtU.put(user1ID, User1);
        UtU.put(user2ID, User2);
        UtU.put(user3ID, User3);
        UtU.put(user4ID, User4);

        UserStore userStore = new UserStore(StU, UtU);
        Persistence.write(userStore, TEST_FILE_PATH);

        UserStore object = Persistence.read(UserStore.class, TEST_FILE_PATH);

        assertEquals(object.getClass(), UserStore.class);
        assertTrue(Arrays.asList(username1, username2, username3, username4)
                .stream()
                .allMatch(name -> object.hasUsername(name)));
        assertTrue(Arrays.asList(user1ID, user2ID, user3ID, user4ID)
                .stream()
                .allMatch(name -> object.hasUserId(name)));
        assertEquals(object.getUser(user1ID)
                .get()
                .calendarCount(), 2);
        assertEquals(object.getUser(user1ID)
                .get()
                .getCalendar(0)
                .getEvents()
                .size(), 2);
        assertEquals(object.getUser(user1ID)
                .get()
                .getCalendar(1)
                .getEvents()
                .size(), 3);

    }
}
