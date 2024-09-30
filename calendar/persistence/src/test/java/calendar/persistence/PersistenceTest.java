package calendar.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import calendar.types.Event;
import calendar.types.User;
import calendar.types.UserSettings;
import calendar.types.UserStore;
import calendar.types.Calendar;;

public class PersistenceTest {

    private static final String TEST_FILE_PATH = "test.json";

    @AfterEach
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testWriteAndReadUser() throws IOException {
        User user = new User("testUser", "testPassword");
        Persistence.write(user, TEST_FILE_PATH);

        User readUser = Persistence.read(User.class, TEST_FILE_PATH);
        assertEquals(user.getUserId(), readUser.getUserId());
        assertEquals(user.getUsername(), readUser.getUsername());
    }

    @Test
    public void testWriteAndReadEvent() throws IOException {
        Event event = new Event("Title", "Description", new Date(), new Date());
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

    @Test
    public void testInvalidClassThrowsIOExeption() throws IOException {
        User user = new User("testUser", "testPassword");
        Persistence.write(user, TEST_FILE_PATH);

        assertThrows(IOException.class, () -> {
            Persistence.read(Event.class, TEST_FILE_PATH);
        });
    }

    @Test
    public void testReadThrowsIOExceptionWhenFileNotFound() {
        assertThrows(IOException.class, () -> {
            Persistence.read(UserSettings.class, TEST_FILE_PATH);
        });
    }

    @Test
    public void testWriteReadWithoutPath() throws IOException {

        User user = new User("testUser", "testPassword");

        File fileMock = Mockito.mock(File.class);
        try (MockedStatic<Persistence> persistenceMock = Mockito.mockStatic(Persistence.class)) {
            persistenceMock.when(() -> Persistence.write(user)).thenAnswer(invocation -> null);
            persistenceMock.when(() -> Persistence.read(User.class)).thenReturn(user);

            when(fileMock.exists()).thenReturn(true);
            Persistence.write(user);

            User readUser = Persistence.read(User.class);
            assertEquals(user.getUserId(), readUser.getUserId());
            assertEquals(user.getUsername(), readUser.getUsername());

            if (!fileMock.exists())
                throw new FileNotFoundException();
            verify(fileMock, never()).delete();
        }
    }

    @Test
    public void testNestedClasses() throws IOException {
        String username1 = "A";
        String username2 = "B";
        String username3 = "C";
        String username4 = "D";

        ArrayList<Event> eventList1 = new ArrayList<>();
        eventList1.add(new Event("title1_1", "description1_1", new Date(), new Date()));
        eventList1.add(new Event("title1_2", "description1_2", new Date(), new Date()));
        ArrayList<Event> eventList2 = new ArrayList<>();
        eventList2.add(new Event("title2_1", "description2_1", new Date(), new Date()));
        eventList2.add(new Event("title2_2", "description2_2", new Date(), new Date()));
        eventList2.add(new Event("title2_3", "description2_3", new Date(), new Date()));

        ArrayList<Calendar> calendar = new ArrayList<>();
        calendar.add(new Calendar(eventList1));
        calendar.add(new Calendar(eventList2));

        UUID user1ID = UUID.randomUUID();
        UUID user2ID = UUID.randomUUID();
        UUID user3ID = UUID.randomUUID();
        UUID user4ID = UUID.randomUUID();

        User User1 = new User(user1ID, username1, "password1", calendar, new UserSettings(user1ID));
        User User2 = new User(user1ID, username2, "password2", new ArrayList<Calendar>(), new UserSettings(user1ID));
        User User3 = new User(user1ID, username3, "password3", new ArrayList<Calendar>(), new UserSettings(user1ID));
        User User4 = new User(user1ID, username4, "password4", new ArrayList<Calendar>(), new UserSettings(user1ID));

        HashMap<String, UUID> StU = new HashMap<>();
        StU.put(username1, user1ID);
        StU.put(username2, user2ID);
        StU.put(username3, user3ID);
        StU.put(username4, user4ID);

        HashMap<UUID, User> UtU = new HashMap<>();
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
