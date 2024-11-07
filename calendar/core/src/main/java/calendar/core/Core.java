package calendar.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import calendar.persistence.Persistence;
import calendar.types.RestUser;
import calendar.types.UserStore;

/**
 * The {@code Core} class manages user authentication, registration, and the
 * initialization and destruction of the application's user data. It also
 * provides access to the calendar application for logged-in users.
 */
public final class Core {
    private Core() {

    }

    /** The minimum number of charachters the password must be. */
    protected static final int MIN_PASSWORD_LENGTH = 6;

    /** A static reference to the UserStore, which manages user-related data. */
    public static UserStore userStore;
    /**
     * An optional instance of CalendarApp, representing the calendar
     * application component.
     */
    private static Optional<CalendarApp> calendarApp = Optional.empty();

    /**
     * Initializes the user data by reading from the persistence storage. If no
     * data is found, a new {@link UserStore} is created.
     *
     * @throws IllegalStateException if an error occurs while reading the data.
     */
    public static void initialize() {
        Path filepath = Path.of(Persistence.DEAFULT_FILE_PATH);
        try {
            if (Files.notExists(filepath) || Files.size(filepath) == 0)
                Files.write(filepath, "null".getBytes());
            userStore = Persistence.read(UserStore.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        if (userStore == null)
            userStore = new UserStore(new HashMap<>(), new HashMap<>());
    }

    /**
     * Saves the user data to the persistence storage.
     *
     * @throws IllegalStateException if an error occurs while writing the data.
     */
    public static void destroy() {
        try {
            Persistence.write(userStore);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Verifies whether the provided username and password match the credentials
     * of an existing user.
     *
     * @param  username the username of the user
     * @param  password the password of the user
     * @return          {@code true} if the credentials are correct,
     *                  {@code false} otherwise
     */
    public static boolean correctCredentials(final String username,
            final String password) {
        return userStore.getUserId(username).flatMap(userStore::getUser)
                .map(user -> user.checkPassword(password)).orElse(false);
    }

    /**
     * Logs in a user using their username and initializes the
     * {@link CalendarApp} for that user.
     *
     * @param  username               the username of the user to log in
     * @throws NoSuchElementException if the username does not exist in the
     *                                {@link UserStore}
     */
    public static void logInAsUser(final String username) {
        logInAsUser(userStore.getUserId(username).orElseThrow());
    }

    /**
     * Logs in a user using their user ID and initializes the
     * {@link CalendarApp} for that user.
     *
     * @param  userId                 the user ID of the user to log in
     * @throws NoSuchElementException if the user ID does not exist in the
     *                                {@link UserStore}
     */
    public static void logInAsUser(final UUID userId) {
        calendarApp = Optional
                .of(new CalendarApp(userStore.getUser(userId).orElseThrow()));
    }

    /**
     * Returns the {@link CalendarApp} instance for the currently logged-in
     * user.
     *
     * @return an {@code Optional} containing the {@link CalendarApp} if a user
     *         is logged in, or an empty {@code Optional} if no user is logged
     *         in.
     */
    public static Optional<CalendarApp> getCalendarApp() {
        return calendarApp;
    }

    /**
     * Registers a new user with the provided username and password, performing
     * validation on the inputs.
     *
     * @param  username the username for the new user
     * @param  password the password for the new user
     * @return          an {@code Optional} containing an error message if
     *                  registration fails, or an empty {@code Optional} if
     *                  registration succeeds.
     */
    public static Optional<String> registerUser(final String username,
            final String password) {
        if (userStore.hasUsername(username))
            return Optional.of(Error.SIGNUP_USERNAME_ALREADY_EXISTS);
        if (username.isBlank())
            return Optional.of(Error.SIGNUP_USERNAME_IS_EMPTY);
        if (password.isBlank())
            return Optional.of(Error.SIGNUP_PASSWORD_IS_EMPTY);
        if (password.length() < MIN_PASSWORD_LENGTH)
            return Optional.of(Error.SIGNUP_PASSWORD_TOO_SHORT);
        userStore.addUser(new RestUser(username, password));
        return Optional.empty();
    }
}
