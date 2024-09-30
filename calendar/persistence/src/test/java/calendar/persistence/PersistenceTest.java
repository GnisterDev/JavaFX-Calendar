package calendar.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.UUID;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import calendar.types.Calendar;
import calendar.types.Event;
import calendar.types.User;

public class PersistenceTest {

    private static final String TEST_FILE_PATH = "test.json";
    private Persistence persistence;

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
        Persistence.write(user);

        User readUser = Persistence.read(User.class);
        assertEquals(user.getUserId(), readUser.getUserId());
        assertEquals(user.getUsername(), readUser.getUsername());
    }

    @Test
    public void testWriteAndReadEvent() throws IOException {
        Event event = new Event("Title", "Description", new Date(), new Date());
        Persistence.write(event);

        Event readEvent = Persistence.read(Event.class);
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
        Persistence.write(user);

        assertThrows(IOException.class, () -> {
            Persistence.read(Event.class);
        });
    }

    @Test
    public void testReadThrowsIOExceptionWhenFileNotFound() {
        assertThrows(IOException.class, () -> {
            Persistence.read(User.class);
        });
    }
}
