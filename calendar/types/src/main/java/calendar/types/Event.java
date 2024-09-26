package calendar.types;

import java.util.UUID;

import javafx.scene.paint.Color;

enum EventType {
    EVENT_TYPE1,
    EVENT_TYPE2
}

public class Event {
    private UUID id;
    private EventType type;
    private String title;
    private String description;
    private Color color;

    Event(String title, String description) {
        this(title, description, EventType.EVENT_TYPE1, Color.BLUE, UUID.randomUUID());
    }

    Event(String title, String description, EventType type, Color color, UUID id) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.color = color;
        this.id = id;
    }

    UUID getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    EventType getType() {
        return type;
    }

    Color getColor() {
        return color;
    }
}
