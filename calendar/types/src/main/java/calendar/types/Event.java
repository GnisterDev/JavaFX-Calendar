package calendar.types;

import java.util.UUID;

import javafx.scene.paint.Color;

public class Event {
    private UUID id;
    private EventType type;
    private String title;
    private String description;
    private Color color;

    public Event(String title, String description) {
        this(title, description, EventType.EVENT_TYPE1, Color.BLUE, UUID.randomUUID());
    }

    public Event(String title, String description, EventType type, Color color, UUID id) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.color = color;
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public EventType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }
}
