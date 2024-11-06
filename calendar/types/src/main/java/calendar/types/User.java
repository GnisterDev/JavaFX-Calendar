package calendar.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code User} class represents a user in the system, with properties such as a unique user ID, 
 * username, password, calendars, and user settings.
 * 
 * <p>Each user is associated with one or more {@link Calendar} objects and has a corresponding {@link UserSettings} object. 
 * Users can be constructed with default values for their user ID, or with specific properties, and can be serialized/deserialized 
 * using the Jackson library through annotations such as {@link JsonCreator} and {@link JsonProperty}.</p>
 */
public class User {
    @JsonProperty
    private UUID userId;

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private List<Calendar> calendars;

    @JsonProperty
    private UserSettings settings;

    /**
     * Constructs a new {@code User} with a randomly generated user ID, a username, and a password.
     * A new {@link Calendar} is also created and added to the user's list of calendars.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this(UUID.randomUUID(), username, password);
    }

    /**
     * Constructs a new {@code User} with the specified user ID, username, and password.
     * A new {@link Calendar} associated with the user ID is added to the user's list of calendars.
     *
     * @param userId   the unique identifier for the user
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(UUID userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.calendars = new ArrayList<>();
        this.settings = new UserSettings(userId);

        this.addCalendar(new Calendar(userId));
    }

    /**
     * Full constructor for the {@code User} class, used for deserialization.
     * 
     * <p>This constructor is annotated with {@link JsonCreator} to allow deserialization from JSON,
     * using the {@link JsonProperty} annotations to map JSON fields to class properties.</p>
     *
     * @param userId    the unique identifier for the user
     * @param username  the username of the user
     * @param password  the password of the user
     * @param calendars the list of calendars associated with the user
     * @param settings  the settings associated with the user
     */
    @JsonCreator
    public User(
            @JsonProperty("userId") UUID userId,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("calendars") List<Calendar> calendars,
            @JsonProperty("settings") UserSettings settings) {
        this.userId = userId;
        this.username = username;
        this.password = password;
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
     * Gets the settings associated with the user.
     *
     * @return the {@link UserSettings} object associated with the user
     */
    public UserSettings getSettings() {
        return settings;
    }

    /**
     * Checks if the provided password matches the user's password.
     *
     * @param password the password to check
     * @return {@code true} if the password matches, {@code false} otherwise
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Gets a copy of the list of calendars associated with the user.
     *
     * @return a new {@link ArrayList} containing the user's calendars
     */
    public List<Calendar> getCalendars() {
        return new ArrayList<>(calendars);
    }

    /**
     * Gets the calendar at the specified index in the user's list of calendars.
     *
     * @param index the index of the calendar to retrieve
     * @return the {@link Calendar} at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Calendar getCalendar(int index) {
        return calendars.get(index);
    }

    /**
     * Removes the specified calendar from the user's list of calendars.
     *
     * @param calendar the {@link Calendar} to remove
     */
    public void removeCalendar(Calendar calendar) {
        calendars.remove(calendar);
    }

    /**
     * Removes the calendar at the specified index from the user's list of calendars.
     *
     * @param index the index of the calendar to remove
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void removeCalendar(int index) {
        calendars.remove(index);
    }

    /**
     * Adds a new calendar to the user's list of calendars.
     *
     * @param calendar the {@link Calendar} to add
     */
    public void addCalendar(Calendar calendar) {
        calendars.add(calendar);
    }

    /**
     * Gets the number of calendars associated with the user.
     *
     * @return the number of calendars
     */
    public int calendarCount() {
        return calendars.size();
    }
}
