package calendar.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import calendar.persistence.Persistence;
import calendar.types.User;
import calendar.types.UserStore;

public class Core {
    protected static UserStore userStore;
    private static Optional<CalendarApp> calendarApp = Optional.empty();

    public static void initialize() throws IOException {
        userStore = Persistence.read(UserStore.class);
        if (userStore == null)
            userStore = new UserStore(new HashMap<>(), new HashMap<>());
    }

    public static void destroy() throws IOException {
        Persistence.write(userStore);
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

    public static boolean registerUser(String username, String password) {
        if (userStore.hasUsername(username))
            return false;
        userStore.addUser(new User(username, password));
        return true;
    }
}
