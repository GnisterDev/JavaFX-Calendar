package calendar.types;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.scene.paint.Color;

public class Event {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private EventType type;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty
    private LocalDateTime startTime;
    @JsonProperty
    private LocalDateTime endTime;
    @JsonProperty
    private Color color;

    public Event(String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this(title, description, startTime, endTime, EventType.EVENT_TYPE1, Color.BLUE, UUID.randomUUID());
    }

    public Event(String title, String description, LocalDateTime startTime, LocalDateTime endTime, Color color) {
        this(title, description, startTime, endTime, EventType.EVENT_TYPE1, color, UUID.randomUUID());
    }

    @JsonCreator
    public Event(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("startTime") LocalDateTime startTime,
            @JsonProperty("endTime") LocalDateTime endTime,
            @JsonProperty("type") EventType type,
            @JsonProperty("color") Color color,
            @JsonProperty("id") UUID id) {
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public EventType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Event) {
            Event other = (Event) obj;
            return type.equals(other.getType())
                    && title.equals(other.getTitle())
                    && description.equals(other.getDescription())
                    && startTime.equals(other.getStartTime())
                    && endTime.equals(other.getEndTime())
                    && color.equals(other.getColor());
        } else
            return false;
    }
}
