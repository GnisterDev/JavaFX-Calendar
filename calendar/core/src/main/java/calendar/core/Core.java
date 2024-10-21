package calendar.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import calendar.persistence.Persistence;
import calendar.types.User;
import calendar.types.UserStore;

public class Core {
    private static int MIN_PASSWORD_LENGTH = 6;

    public static UserStore userStore;
    private static Optional<CalendarApp> calendarApp = Optional.empty();

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

    public static void destroy() {
        try {
            Persistence.write(userStore);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean correctCredentials(String username, String password) {
        return userStore.getUserId(username).flatMap(userStore::getUser).map(user -> user.checkPassword(password))
                .orElse(false);
    }

    public static void logInAsUser(String username) {
        logInAsUser(userStore.getUserId(username)
                .orElseThrow());
    }

    public static void logInAsUser(UUID userId) {
        calendarApp = Optional.of(new CalendarApp(userStore.getUser(userId).orElseThrow()));
    }

    public static Optional<CalendarApp> getCalendarApp() {
        return calendarApp;
    }

    public static Optional<String> registerUser(String username, String password) {
        if (userStore.hasUsername(username))
            return Optional.of("Username already exists.");
        if (username.isBlank())
            return Optional.of("Username cannot be empty");
        if (password.isBlank())
            return Optional.of("Password cannot be empty.");
        if (password.length() < MIN_PASSWORD_LENGTH)
            return Optional.of("Password must be at least 6 characters long.");
        userStore.addUser(new User(username, password));
        return Optional.empty();
    }
}
