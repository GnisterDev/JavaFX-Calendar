package calendar.types;

import java.util.Date;
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
    private Date startTime;
    @JsonProperty
    private Date endTime;
    @JsonProperty
    private Color color;

    public Event(String title, String description, Date startTime, Date endTime) {
        this(title, description, startTime, endTime, EventType.EVENT_TYPE1, Color.BLUE, UUID.randomUUID());
    }

    @JsonCreator
    public Event(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("startTime") Date startTime,
            @JsonProperty("endTime") Date endTime,
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
