package calendar.types;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.scene.paint.Color;

/**
 * The {@code Event} class represents an event with a title, description, start and end times, type, color, and a unique ID.
 * 
 * <p>Events are designed to be used in conjunction with a {@link Calendar}, and can be serialized and deserialized 
 * using the Jackson library with support for JSON properties through annotations like {@link JsonProperty} 
 * and {@link JsonCreator}.</p>
 * 
 * <p>Each event has a default type of {@link EventType#EVENT_TYPE1}, a default color of {@link Color#BLUE}, 
 * and a randomly generated {@link UUID} as its unique identifier if not explicitly provided.</p>
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
     * Constructs a new {@code Event} with default values for type, color, and a random ID.
     * 
     * <p>This constructor initializes the event with the specified title, description, start and end times, 
     * while defaulting the type to {@link EventType#EVENT_TYPE1}, the color to {@link Color#BLUE}, 
     * and generating a random {@link UUID} for the event ID.</p>
     * 
     * @param title       the title of the event
     * @param description a brief description of the event
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     */
    public Event(String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this(title, description, startTime, endTime, EventType.EVENT_TYPE1, Color.BLUE, UUID.randomUUID());
    }

    /**
     * Full constructor for the {@code Event} class.
     * 
     * <p>This constructor is annotated with {@link JsonCreator} to allow deserialization from JSON, 
     * using the {@link JsonProperty} annotations to map JSON fields to class properties.</p>
     * 
     * @param title       the title of the event
     * @param description a brief description of the event
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     * @param type        the type of the event
     * @param color       the color associated with the event
     * @param id          the unique identifier for the event
     */
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

    /**
     * Gets the end time of the event.
     * 
     * @return the {@link LocalDateTime} representing the end time of the event
     */
    public LocalDateTime getEndTime() {
        return endTime;
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
}
