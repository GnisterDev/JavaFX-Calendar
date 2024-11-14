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
public class RestCalendar {

    /** The userid of the user that owns this calendar. */
    @JsonProperty
    private UUID calendarId;

    /** The name of the calendar. */
    @JsonProperty
    private String name;

    /** The evensts that are conected to this calendar. */
    @JsonProperty
    private List<Event> events;

    /**
     * Default constructor that creates a {@link RestCalendar} with a randomly
     * generated {@link UUID} for the calendar ID and initializes it with
     * default values: "Unnamed calendar" and an empty events list.
     */
    public RestCalendar() {
        this(UUID.randomUUID());
    }

    /**
     * Constructor that creates a {@link RestCalendar} with the provided
     * {@link UUID} for the calendar ID, a default name "Unnamed calendar", and
     * an empty events list.
     *
     * @param calendarId the {@link UUID} representing the calendar ID
     */
    public RestCalendar(final UUID calendarId) {
        this(calendarId, "Unnamed calendar", new ArrayList<>());
    }

    /**
     * Constructor that creates a {@link RestCalendar} with a randomly generated
     * {@link UUID} for the calendar ID, the specified name, and an empty events
     * list.
     *
     * @param name the name of the calendar
     */
    public RestCalendar(final String name) {
        this(UUID.randomUUID(), name, new ArrayList<>());
    }

    /**
     * Constructor that creates a {@link RestCalendar} with a randomly generated
     * {@link UUID} for the calendar ID, the default name "Unnamed calendar",
     * and the specified list of events.
     *
     * @param events the list of {@link Event} objects associated with the
     *               calendar
     */
    public RestCalendar(final List<Event> events) {
        this(UUID.randomUUID(), "Unnamed caledar", events);
    }

    /**
     * Constructor that creates a {@link RestCalendar} with the specified
     * calendar ID, name, and events. This constructor is used for
     * deserialization with JSON libraries.
     *
     * @param calendarId the {@link UUID} representing the calendar ID
     * @param name       the name of the calendar
     * @param events     the list of {@link Event} objects associated with the
     *                   calendar
     */
    @JsonCreator
    public RestCalendar(@JsonProperty("userId") final UUID calendarId,
            @JsonProperty("name") final String name,
            @JsonProperty("events") final List<Event> events) {
        this.events = new ArrayList<>(events);
        this.calendarId = calendarId;
        this.name = name;
    }

    /**
     * Gets the id of the calendar.
     *
     * @return the id asosiated with this calendar.
     */
    public UUID getCalendarId() {
        return calendarId;
    }

    /**
     * Gets the name of the calendar.
     *
     * @return the name asosiated with this calendar.
     */
    public String getName() {
        return name;
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
