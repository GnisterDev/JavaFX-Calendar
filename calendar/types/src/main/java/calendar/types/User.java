package calendar.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code User} class represents a user in the system, with properties such
 * as a unique user ID, username, password, calendars, and user settings.
 *
 * <p>
 * Each user is associated with one or more {@link Calendar} objects and has a
 * corresponding {@link UserSettings} object. Users can be constructed with
 * default values for their user ID, or with specific properties, and can be
 * serialized/deserialized using the Jackson library through annotations such as
 * {@link JsonCreator} and {@link JsonProperty}.
 * </p>
 */
public class User {

    /** The user id of the user. */
    @JsonProperty
    private UUID userId;

    /** The username of the user. */
    @JsonProperty
    private String username;

    /** The password of the user. */
    @JsonProperty
    private List<Calendar> calendars;

    /** The settings the user has. */
    @JsonProperty
    private UserSettings settings;

    /**
     * Constructs a new {@code User} instance from a {@code RestUser} object.
     * <p>
     * This constructor initializes a {@code User} by copying fields from a
     * {@code RestUser} instance. The {@code userId} and {@code username} are
     * directly copied, while each calendar in the {@code RestUser} is converted
     * to a {@code Calendar} instance and stored in a list. The {@code settings}
     * are also copied.
     * </p>
     *
     * @param user the {@code RestUser} object from which to initialize this
     *             {@code User}
     */
    public User(final RestUser user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.calendars =
                user.getCalendars().stream().map(c -> new Calendar(c)).toList();
        this.settings = user.getSettings();
    }

    /**
     * Full constructor for the {@code User} class, used for deserialization.
     *
     * <p>
     * This constructor is annotated with {@link JsonCreator} to allow
     * deserialization from JSON, using the {@link JsonProperty} annotations to
     * map JSON fields to class properties.
     * </p>
     *
     * @param userId    the unique identifier for the user
     * @param username  the username of the user
     * @param calendars the list of calendars associated with the user
     * @param settings  the settings associated with the user
     */
    @JsonCreator
    public User(@JsonProperty("userId") final UUID userId,
            @JsonProperty("username") final String username,
            @JsonProperty("calendars") final List<Calendar> calendars,
            @JsonProperty("settings") final UserSettings settings) {
        this.userId = userId;
        this.username = username;
        this.calendars = new ArrayList<>(calendars);
        this.settings = settings;
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return the {@link UUID} representing the user's ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets a list of calendars for the user.
     *
     * @return a list of calendars for the user.
     */
    public List<Calendar> getCalendars() {
        return new ArrayList<>(calendars);
    }

    /**
     * Gets the settings for the user.
     *
     * @return the {@link UserSettings} for the user.
     */
    public UserSettings getSettings() {
        return settings;
    }
}
