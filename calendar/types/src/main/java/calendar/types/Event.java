package calendar.types;

import java.util.Date;
import java.util.UUID;

import javafx.scene.paint.Color;

public class Event {
    private UUID id;
    private EventType type;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private Color color;

    public Event(String title, String description, Date startTime, Date endTime) {
        this(title, description, startTime, endTime, EventType.EVENT_TYPE1, Color.BLUE, UUID.randomUUID());
    }

    public Event(String title, String description, Date startTime, Date endTime, EventType type, Color color, UUID id) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public EventType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }
}
