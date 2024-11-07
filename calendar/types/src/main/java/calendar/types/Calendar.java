package calendar.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code Calendar} class represents a user's calendar, which contains a
 * list of events. Each calendar is associated with a unique {@link UUID}
 * representing the user and a list of {@link Event} objects.
 *
 * <p>
 * This class is designed to be serialized and deserialized using the Jackson
 * library, with support for JSON properties through annotations like
 * {@link JsonProperty} and {@link JsonCreator}. It provides methods for adding,
 * removing, and retrieving events, as well as getting the count of events.
 * </p>
 *
 * <p>
 * Instances of this class can be constructed with a user ID and/or a list of
 * events. If no user ID is provided, a random {@link UUID} is generated.
 * </p>
 */
public class Calendar {

    /** The userid of the user that owns this calendar. */
    @JsonProperty
    private UUID userId;

    /** The evensts that are conected to this calendar. */
    @JsonProperty
    private List<Event> events;

    /**
     * Default constructor. Creates a new calendar with a randomly generated
     * {@link UUID} for the user ID and an empty list of events.
     */
    public Calendar() {
        this(UUID.randomUUID());
    }

    /**
     * Creates a new calendar with the specified user ID and an empty list of
     * events.
     *
     * @param userId the unique identifier for the user associated with this
     *               calendar
     */
    public Calendar(final UUID userId) {
        this(userId, new ArrayList<>());
    }

    /**
     * Creates a new calendar with a randomly generated {@link UUID} for the
     * user ID and the specified list of events.
     *
     * @param events the list of events to initialize this calendar with
     */
    public Calendar(final List<Event> events) {
        this(UUID.randomUUID(), events);
    }

    /**
     * Full constructor for the {@code Calendar} class.
     *
     * <p>
     * This constructor is annotated with {@link JsonCreator} to allow
     * deserialization from JSON, using the {@link JsonProperty} annotations to
     * map JSON fields to class properties.
     * </p>
     *
     * @param userId the unique identifier for the user associated with this
     *               calendar
     * @param events the list of events to initialize this calendar with
     */
    @JsonCreator
    public Calendar(@JsonProperty("userId") final UUID userId,
            @JsonProperty("events") final List<Event> events) {
        this.events = new ArrayList<>(events);
        this.userId = userId;
    }

    /**
     * Gets the user ID associated with this calendar.
     *
     * @return the {@link UUID} representing the user ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets a copy of the list of events in this calendar.
     *
     * @return a new {@link ArrayList} containing the events
     */
    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Gets the event at the specified index in the event list.
     *
     * @param  index                     the index of the event to retrieve
     * @return                           The {@link Event} at the specified
     *                                   index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Event getEvent(final int index) {
        return events.get(index);
    }

    /**
     * Removes the specified event from the calendar.
     *
     * @param event the {@link Event} to be removed
     */
    public void removeEvent(final Event event) {
        events.remove(event);
    }

    /**
     * Removes the event at the specified index in the event list.
     *
     * @param  index                     the index of the event to be removed
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void removeEvent(final int index) {
        events.remove(index);
    }

    /**
     * Adds a new event to the calendar.
     *
     * @param event the {@link Event} to be added
     */
    public void addEvent(final Event event) {
        events.add(event);
    }

    /**
     * Gets the number of events in this calendar.
     *
     * @return the number of events
     */
    public int eventCount() {
        return events.size();
    }
}
