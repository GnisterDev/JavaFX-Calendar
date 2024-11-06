package calendar.types;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.scene.paint.Color;

/**
 * The {@code Event} class represents an event with a title, description, start
 * and end times, type, color, and a unique ID.
 * 
 * <p>
 * Events are designed to be used in conjunction with a {@link Calendar}, and
 * can be serialized and deserialized
 * using the Jackson library with support for JSON properties through
 * annotations like {@link JsonProperty}
 * and {@link JsonCreator}.
 * </p>
 * 
 * <p>
 * Each event has a default type of {@link EventType#REGULAR}, a default color
 * of {@link Color#BLUE},
 * and a randomly generated {@link UUID} as its unique identifier if not
 * explicitly provided.
 * </p>
 */
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

    /**
     * Constructs a new {@code Event} with default values for type, color, and a
     * random ID.
     * 
     * <p>
     * This constructor initializes the event with the specified title, description,
     * start and end times,
     * while defaulting the type to {@link EventType#REGULAR}, the color to
     * {@link Color#BLUE},
     * and generating a random {@link UUID} for the event ID.
     * </p>
     * 
     * @param title       the title of the event
     * @param description a brief description of the event
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     */
    public Event(String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this(title, description, startTime, endTime, EventType.REGULAR, Color.BLUE, UUID.randomUUID());
    }

    public Event(String title, String description, LocalDateTime startTime, LocalDateTime endTime, Color color) {
        this(title, description, startTime, endTime, EventType.REGULAR, color, UUID.randomUUID());
    }

    public Event(
            String title,
            String description,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Color color,
            EventType type) {
        this(title, description, startTime, endTime, type, color, UUID.randomUUID());
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

    /**
     * Gets the unique identifier for the event.
     * 
     * @return the {@link UUID} representing the event's ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the title of the event.
     * 
     * @return the title of the event
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Gets the description of the event.
     * 
     * @return the description of the event
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the start time of the event.
     * 
     * @return the {@link LocalDateTime} representing the start time of the event
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime newStarTime) {
        this.startTime = newStarTime;
    }

    /**
     * Gets the end time of the event.
     * 
     * @return the {@link LocalDateTime} representing the end time of the event
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime newEndTime) {
        this.endTime = newEndTime;
    }

    /**
     * Gets the type of the event.
     * 
     * @return the {@link EventType} representing the type of the event
     */
    public EventType getType() {
        return type;
    }

    /**
     * Gets the color associated with the event.
     * 
     * @return the {@link Color} representing the color of the event
     */
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
