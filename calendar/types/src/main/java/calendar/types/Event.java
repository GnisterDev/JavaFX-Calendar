package calendar.types;

import java.time.LocalDateTime;
import java.util.Objects;
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
 * can be serialized and deserialized using the Jackson library with support for
 * JSON properties through annotations like {@link JsonProperty} and
 * {@link JsonCreator}.
 * </p>
 *
 * <p>
 * Each event has a default type of {@link EventType#REGULAR}, a default color
 * of {@link Color#BLUE}, and a randomly generated {@link UUID} as its unique
 * identifier if not explicitly provided.
 * </p>
 */
public class Event {
    /** The id of the event. */
    @JsonProperty
    private UUID id;
    /** The EventType of the event. */
    @JsonProperty
    private EventType type;
    /** The title of the event. */
    @JsonProperty
    private String title;
    /** The description of the event. */
    @JsonProperty
    private String description;
    /** The start time of the event. */
    @JsonProperty
    private LocalDateTime startTime;
    /** The end time of the event. */
    @JsonProperty
    private LocalDateTime endTime;
    /** The color of the event, as is shows in the GUI. */
    @JsonProperty
    private Color color;

    /**
     * Constructs a new {@code Event} with default values for type, color, and a
     * random ID.
     *
     * <p>
     * This constructor initializes the event with the specified title,
     * description, start and end times, while defaulting the type to
     * {@link EventType#REGULAR}, the color to {@link Color#BLUE}, and
     * generating a random {@link UUID} for the event ID.
     * </p>
     *
     * @param title       the title of the event
     * @param description a brief description of the event
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     */
    public Event(final String title,
            final String description,
            final LocalDateTime startTime,
            final LocalDateTime endTime) {
        this(title,
             description,
             startTime,
             endTime,
             Color.BLUE,
             EventType.REGULAR,
             UUID.randomUUID());
    }

    /**
     * Constructs a new {@code Event} with default values for type, color, and a
     * random ID.
     *
     * <p>
     * This constructor initializes the event with the specified title,
     * description, start and end times, while defaulting the type to
     * {@link EventType#REGULAR}, the color to {@link Color#BLUE}, and
     * generating a random {@link UUID} for the event ID.
     * </p>
     *
     * @param title       the title of the event
     * @param description a brief description of the event
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     * @param color       the color of the event
     * @param type        the EventType the event should be
     */
    public Event(final String title,
            final String description,
            final LocalDateTime startTime,
            final LocalDateTime endTime,
            final Color color,
            final EventType type) {
        this(title,
             description,
             startTime,
             endTime,
             color,
             type,
             UUID.randomUUID());
    }

    /**
     * Constructs a new {@code Event} with default values for type, color, and a
     * random ID.
     *
     * <p>
     * This constructor initializes the event with the specified title,
     * description, start and end times, while defaulting the type to
     * {@link EventType#REGULAR}, the color to {@link Color#BLUE}, and
     * generating a random {@link UUID} for the event ID.
     * </p>
     *
     * @param title       the title of the event
     * @param description a brief description of the event
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     * @param color       the color of the event
     * @param type        the EventType the event should be
     * @param id          the uniqe id of the event
     */
    @JsonCreator
    public Event(@JsonProperty("title") final String title,
            @JsonProperty("description") final String description,
            @JsonProperty("startTime") final LocalDateTime startTime,
            @JsonProperty("endTime") final LocalDateTime endTime,
            @JsonProperty("color") final Color color,
            @JsonProperty("type") final EventType type,
            @JsonProperty("id") final UUID id) {
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
     * @return the {@link LocalDateTime} representing the start time of the
     *         event
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

    /**
     * Returns a hash code value for the object. This method is supported for
     * the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     */
    @Override
    public int hashCode() {
        return Objects
                .hash(title, description, startTime, endTime, color, type, id);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj instanceof Event) {
            Event other = (Event) obj;
            return type.equals(other.getType())
                    && title.equals(other.getTitle())
                    && description.equals(other.getDescription())
                    && startTime.equals(other.getStartTime())
                    && endTime.equals(other.getEndTime())
                    && color.equals(other.getColor());
        } else return false;
    }
}
