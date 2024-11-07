package calendar.types;

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
    private UUID calendarId;

    /** The evensts that are conected to this calendar. */
    @JsonProperty
    private String name;

    public Calendar(RestCalendar calendar) {
        this.calendarId = calendar.getCalendarId();
        this.name = calendar.getName();
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
    public Calendar(@JsonProperty("calendarId") UUID calendarId, @JsonProperty("name") String name) {
        this.calendarId = calendarId;
        this.name = name;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public String getName() {
        return name;
    }
}
