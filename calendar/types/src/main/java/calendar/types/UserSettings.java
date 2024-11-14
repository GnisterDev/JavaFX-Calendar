package calendar.types;

import java.util.TimeZone;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code UserSettings} class represents the user-specific settings, such as
 * time zone, time format preferences, and the option to display week numbers in
 * the calendar.
 *
 * <p>
 * Each {@link User} has an associated {@code UserSettings} object that stores
 * their preferences.
 * </p>
 *
 * <p>
 * The class is designed for serialization and deserialization with the Jackson
 * library using annotations such as {@link JsonCreator} and
 * {@link JsonProperty}.
 * </p>
 */
public class UserSettings {

    /** The userid of the user that owns this user setting. */
    @JsonProperty
    private UUID userId;

    /** The timezone of the user. */
    @JsonProperty
    private TimeZone timezone;

    /** If the user uses the 12 or 24-hour clock. */
    @JsonProperty
    private boolean militaryTime;

    /** If the week number of the week is shown. final */
    @JsonProperty
    private boolean showWeekNr;

    /**
     * Full constructor for the {@code UserSettings} class.
     *
     * <p>
     * This constructor is annotated with {@link JsonCreator} to enable JSON
     * deserialization. Each parameter is mapped from a JSON property using the
     * {@link JsonProperty} annotation.
     * </p>
     *
     * @param userId       the unique identifier of the user
     * @param timezone     the user's preferred {@link TimeZone}
     * @param militaryTime {@code true} if the user prefers military (24-hour)
     *                     time format, {@code false} otherwise
     * @param showWeekNr   {@code true} if the user prefers to display the week
     *                     number, {@code false} otherwise
     */
    @JsonCreator
    public UserSettings(@JsonProperty("userId") final UUID userId,
            @JsonProperty("timezone") final TimeZone timezone,
            @JsonProperty("militaryTime") final boolean militaryTime,
            @JsonProperty("showWeekNr") final boolean showWeekNr) {
        this.userId = userId;
        this.timezone = (TimeZone) timezone.clone();
        this.militaryTime = militaryTime;
        this.showWeekNr = showWeekNr;
    }

    /**
     * Constructs a new {@code UserSettings} object with default settings.
     *
     * <p>
     * This constructor initializes the user settings with the system's default
     * time zone, enabling military (24-hour) time by default, and disabling the
     * display of week numbers.
     * </p>
     *
     * @param userId the unique identifier of the user
     */
    public UserSettings(final UUID userId) {
        this(userId, TimeZone.getDefault(), true, true);
    }

    public UUID getUserId() {
        return userId;
    }

    public TimeZone getTimezone() {
        return (TimeZone) timezone.clone();
    }

    /**
     * Gets the user's preference for military (24-hour) time format.
     *
     * @return {@code true} if the user prefers military (24-hour) time,
     *         {@code false} otherwise
     */
    public boolean getMilitaryTime() {
        return militaryTime;
    }

    /**
     * Gets the user's preference for displaying week numbers in the calendar.
     *
     * @return {@code true} if the user prefers to display the week number,
     *         {@code false} otherwise
     */
    public boolean getShowWeekNr() {
        return showWeekNr;
    }
}
