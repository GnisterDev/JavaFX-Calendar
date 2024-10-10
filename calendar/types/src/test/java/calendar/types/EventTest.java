package calendar.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

public class EventTest {
    private LocalDateTime date1 = LocalDateTime.of(2024, 10, 7, 10, 30);
    private LocalDateTime date2 = LocalDateTime.of(2024, 10, 10, 14, 50);

    @Test
    public void testConstructors() {
        Event event1 = new Event("title", "description", date1, date2);
        Event event2 = new Event("title", "description", date1, date2, EventType.EVENT_TYPE1, Color.BLUE,
                UUID.randomUUID());

        assertEquals("title", event1.getTitle());
        assertEquals("description", event1.getDescription());
        assertEquals(date1, event1.getStartTime());
        assertEquals(date2, event1.getEndTime());

        assertNotEquals(event1, event2);
        assertNotEquals(event1.getId(), event2.getId());

        assertEquals(event1.getTitle(), event2.getTitle());
        assertEquals(event1.getDescription(), event2.getDescription());
        assertEquals(event1.getStartTime(), event2.getStartTime());
        assertEquals(event1.getEndTime(), event2.getEndTime());
        assertEquals(event1.getType(), event2.getType());
        assertEquals(event1.getColor(), event2.getColor());
    }
}
