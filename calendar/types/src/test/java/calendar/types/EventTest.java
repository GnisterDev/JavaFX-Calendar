package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

public class EventTest {

    private Event event;
    private String title = "Meeting";
    private String description = "Project discussion";
    private LocalDateTime startTime = LocalDateTime.of(2023, 11, 1, 10, 0);
    private LocalDateTime endTime = LocalDateTime.of(2023, 11, 1, 11, 0);
    private Color color = Color.GREEN;
    private EventType type = EventType.REGULAR;
    private UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        event = new Event(title, description, startTime, endTime, color, type, id);
    }

    @Test
    void testConstructorWithAllFields() {
        assertEquals(title, event.getTitle());
        assertEquals(description, event.getDescription());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
        assertEquals(color, event.getColor());
        assertEquals(type, event.getType());
        assertEquals(id, event.getId());
    }

    @Test
    void testConstructorWithDefaults() {
        Event defaultEvent = new Event(title, description, startTime, endTime);

        assertEquals(title, defaultEvent.getTitle());
        assertEquals(description, defaultEvent.getDescription());
        assertEquals(startTime, defaultEvent.getStartTime());
        assertEquals(endTime, defaultEvent.getEndTime());
        assertEquals(EventType.REGULAR, defaultEvent.getType());
        assertEquals(Color.BLUE, defaultEvent.getColor());
        assertNotNull(defaultEvent.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        Event sameEvent = new Event(title, description, startTime, endTime, color, type, id);
        Event differentEvent = new Event("Other Meeting", "Another description", startTime, endTime, Color.RED,
                EventType.REGULAR, UUID.randomUUID());

        assertEquals(event, sameEvent);
        assertNotEquals(event, differentEvent);
        assertEquals(event.hashCode(), sameEvent.hashCode());
    }

    @Test
    void testIdUniqueness() {
        Event anotherEvent = new Event(title, description, startTime, endTime);
        assertNotEquals(event.getId(), anotherEvent.getId());
    }

    @Test
    void testGetters() {
        assertEquals(title, event.getTitle());
        assertEquals(description, event.getDescription());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
        assertEquals(type, event.getType());
        assertEquals(color, event.getColor());
    }

    @Test
    void testOtherConstructors() {
        Event newEvent1 = new Event(title, description, startTime, endTime);
        assertNotNull(newEvent1.getId());
        assertNotNull(newEvent1.getColor());
        assertNotNull(newEvent1.getType());

        Event newEvent2 = new Event(title, description, startTime, endTime, Color.RED, EventType.REGULAR);
        assertNotNull(newEvent2.getId());
    }

    @Test
    @SuppressWarnings("unlikely-arg-type")
    void testEquals() {
        assertTrue(event.equals(event));
        assertFalse(event.equals(null));
        assertFalse(event.equals("Some String"));

        Event differentTitleEvent = new Event("Different Title", description, startTime, endTime, color, type, id);
        assertFalse(event.equals(differentTitleEvent));

        Event differentDescriptionEvent = new Event(title, "Different Description", startTime, endTime, color, type,
                id);
        assertFalse(event.equals(differentDescriptionEvent));

        Event differentStartTimeEvent = new Event(title, description, startTime.plusHours(1), endTime, color, type, id);
        assertFalse(event.equals(differentStartTimeEvent));

        Event differentEndTimeEvent = new Event(title, description, startTime, endTime.plusHours(1), color, type, id);
        assertFalse(event.equals(differentEndTimeEvent));

        Event differentColorEvent = new Event(title, description, startTime, endTime, Color.RED, type, id);
        assertFalse(event.equals(differentColorEvent));

        Event differentTypeEvent = new Event(title, description, startTime, endTime, color, EventType.ALL_DAY, id);
        assertFalse(event.equals(differentTypeEvent));

        Event identicalEvent = new Event(title, description, startTime, endTime, color, type, id);
        assertTrue(event.equals(identicalEvent));
    }
}
