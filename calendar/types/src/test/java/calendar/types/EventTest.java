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

/**
 * Unit test class for testing the {@link Event} class.
 * <p>
 * This class includes tests for constructors, object equality, hash code
 * consistency,
 * and uniqueness of generated UUIDs, along with verifying getter methods.
 */
public class EventTest {

    private Event event;
    private String title = "Meeting";
    private String description = "Project discussion";
    private LocalDateTime startTime = LocalDateTime.of(2023, 11, 1, 10, 0);
    private LocalDateTime endTime = LocalDateTime.of(2023, 11, 1, 11, 0);
    private Color color = Color.GREEN;
    private EventType type = EventType.REGULAR;
    private UUID id = UUID.randomUUID();

    /**
     * Sets up a default event instance before each test.
     */
    @BeforeEach
    void setUp() {
        event = new Event(title, description, startTime, endTime, color, type, id);
    }

    /**
     * Tests the constructor with all fields to ensure all values are properly
     * assigned.
     */
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

    /**
     * Tests the default constructor, verifying default values for color, event
     * type,
     * and a unique identifier.
     */
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

    /**
     * Verifies the correctness of `equals` and `hashCode` methods.
     * <p>
     * Confirms that objects with identical field values are equal and have the same
     * hash code,
     * while different objects do not.
     */
    @Test
    void testEqualsAndHashCode() {
        Event sameEvent = new Event(title, description, startTime, endTime, color, type, id);
        Event differentEvent = new Event("Other Meeting", "Another description", startTime, endTime, Color.RED,
                EventType.REGULAR, UUID.randomUUID());

        assertEquals(event, sameEvent);
        assertNotEquals(event, differentEvent);
        assertEquals(event.hashCode(), sameEvent.hashCode());
    }

    /**
     * Tests the uniqueness of the UUID generated for each event.
     */
    @Test
    void testIdUniqueness() {
        Event anotherEvent = new Event(title, description, startTime, endTime);
        assertNotEquals(event.getId(), anotherEvent.getId());
    }

    /**
     * Verifies the getter methods for all fields in the {@link Event} class.
     */
    @Test
    void testGetters() {
        assertEquals(title, event.getTitle());
        assertEquals(description, event.getDescription());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
        assertEquals(type, event.getType());
        assertEquals(color, event.getColor());
    }

    /**
     * Tests additional constructors to confirm the presence of default values for
     * color,
     * type, and ID.
     */
    @Test
    void testOtherConstructors() {
        Event newEvent1 = new Event(title, description, startTime, endTime);
        assertNotNull(newEvent1.getId());
        assertNotNull(newEvent1.getColor());
        assertNotNull(newEvent1.getType());

        Event newEvent2 = new Event(title, description, startTime, endTime, Color.RED, EventType.REGULAR);
        assertNotNull(newEvent2.getId());
    }

    /**
     * Tests the `equals` method to confirm consistent behavior with different field
     * values,
     * including comparisons with null and unrelated types.
     */
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
