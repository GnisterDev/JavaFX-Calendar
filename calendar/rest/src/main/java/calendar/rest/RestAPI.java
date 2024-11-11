package calendar.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import calendar.persistence.Persistence;
import calendar.types.RestUser;
import calendar.types.RestCalendar;
import calendar.types.User;
import calendar.types.UserStore;
import calendar.types.Event;
import calendar.types.EventType;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javafx.scene.paint.Color;

public class RestAPI {
    private static UserStore userStore;

    private static final int PORT = 8000;

    private static final int OK = 200;
    private static final int CREATED = 201;

    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int NOT_FOUND = 404;
    private static final int CONFLICT = 409;

    public static void main(String[] args) throws IOException {
        // Make sure file exists
        Path filepath = Path.of("rest/userdata.json");
        if (Files.notExists(filepath) || Files.size(filepath) == 0)
            Files.write(filepath, "null".getBytes());

        // Load database
        userStore = Persistence.read(UserStore.class, filepath.toString());
        if (userStore == null)
            userStore = new UserStore(new HashMap<>(), new HashMap<>());

        // Define server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // User endpoint
        server.createContext("/users", t -> {
            if (t.getRequestMethod().equals("PATCH")
                    || t.getRequestMethod().equals("DELETE")
                    || t.getRequestMethod().equals("PUT")) {
                sendResponse(t, NOT_FOUND, "Not found");
                return;
            }

            // Validate path
            String[] path = t.getRequestURI().getPath().toString().split("/");
            if (path.length != 3) {
                sendResponse(t, BAD_REQUEST, "Wrong number of arguments");
                return;
            }

            // Get username
            String username = path[2];

            // User creation
            if (t.getRequestMethod().equals("POST")) {
                // Username already exists
                if (userStore.hasUsername(username)) {
                    sendResponse(t,
                                 CONFLICT,
                                 "User '" + username + "' already exists");
                    return;
                }

                // Get password
                Optional<String> password = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("password"));

                // Password not provided
                if (password.isEmpty()) {
                    sendResponse(t, BAD_REQUEST, "Password is required");
                    return;
                }

                // Create user
                userStore.addUser(new RestUser(username, password.get()));
                sendResponse(t,
                             CREATED,
                             "User '" + username + "' created successfully");
            }

            // User retreival
            Optional<RestUser> user =
                    userStore.getUserId(username).flatMap(userStore::getUser);

            // User not found
            if (user.isEmpty()) {
                sendResponse(t,
                             NOT_FOUND,
                             "User: '" + username + "' not found");
                return;
            }

            // Validate password
            Optional<String> password = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("password"));
            if (password.isEmpty()
                    || !user.get().checkPassword(password.get())) {
                sendResponse(t, UNAUTHORIZED, "Wrong password");
                return;
            }

            // Serialze and send user data
            sendResponse(t, OK, Persistence.toJSON(new User(user.get())));
        });

        // Calendar endpoint
        server.createContext("/calendar", t -> {
            if (t.getRequestMethod().equals("PATCH")) {
                sendResponse(t, NOT_FOUND, "Not found");
                return;
            }

            // Validate path
            String[] path = t.getRequestURI().getPath().toString().split("/");
            if (!(path.length == 3 || (path.length == 2
                    && t.getRequestMethod().equals("POST")))) {
                sendResponse(t, BAD_REQUEST, "Wrong number of arguments");
                return;
            }

            // Get credentials
            Optional<String> password = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("password"));
            Optional<String> username = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("username"));

            // Credentials not provided
            if (password.isEmpty() || username.isEmpty()) {
                sendResponse(t, BAD_REQUEST, "Credentials required");
                return;
            }

            // Wrong credentials
            Optional<RestUser> user = userStore.getUserId(username.get())
                    .flatMap(userStore::getUser);
            if (!user.map(u -> u.checkPassword(password.get())).orElse(false)) {
                sendResponse(t, UNAUTHORIZED, "Wrong credentials");
                return;
            }

            // Calendar creation
            if (t.getRequestMethod().equals("POST")) {
                Optional<String> name = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("name"));
                RestCalendar calendar =
                        new RestCalendar(name.orElse("Unnamed calendar"));
                user.get().addCalendar(calendar);
                sendResponse(t,
                             CREATED,
                             "Calendar with id: '" + calendar.getCalendarId()
                                     + "' successfully created");
                return;
            }

            // Get Calendar
            UUID calendarId = UUID.fromString(path[2]);
            Optional<RestCalendar> calendar = user.get().getCalendars().stream()
                    .filter(c -> c.getCalendarId().equals(calendarId))
                    .findFirst();

            // Calendar does not exist
            if (calendar.isEmpty()) {
                sendResponse(t, NOT_FOUND, "Calendar not found");
            }

            // Calendar deletion
            if (t.getRequestMethod().equals("DELETE")) {
                user.get().removeCalendar(calendar.get());
                sendResponse(t,
                             OK,
                             "Calendar with id: '"
                                     + calendar.get().getCalendarId()
                                     + "' successfully deleted");
                return;
            }

            // Filtering options
            Optional<LocalDateTime> before, after;
            try {
                before = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("before"))
                        .map(s -> LocalDateTime.parse(s));
                after = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("after"))
                        .map(s -> LocalDateTime.parse(s));
            } catch (DateTimeParseException e) {
                sendResponse(t, BAD_REQUEST, "Wrong dates for before or after");
                return;
            }

            // Get events
            List<Event> events = calendar.get().getEvents().stream()
                    .filter(event -> before.map(event.getStartTime()::isBefore)
                            .orElse(true)
                            && after.map(event.getEndTime()::isAfter)
                                    .orElse(true))
                    .toList();

            sendResponse(t, OK, Persistence.toJSON(events));
        });

        // Event endpoint
        server.createContext("/event", t -> {
            if (t.getRequestMethod().equals("GET")) {
                sendResponse(t, NOT_FOUND, "Not found");
                return;
            }

            // Validate path
            String[] path = t.getRequestURI().getPath().toString().split("/");
            if (!(path.length == 4 || (t.getRequestMethod().equals("POST")
                    && path.length == 3))) {
                sendResponse(t, BAD_REQUEST, "Wrong number of arguments");
                return;
            }

            // Get credentials
            Optional<String> password = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("password"));
            Optional<String> username = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("username"));

            // Credentials not provided
            if (password.isEmpty() || username.isEmpty()) {
                sendResponse(t, BAD_REQUEST, "Credentials required");
                return;
            }

            // Wrong credentials
            Optional<RestUser> user = userStore.getUserId(username.get())
                    .flatMap(userStore::getUser);
            if (!user.map(u -> u.checkPassword(password.get())).orElse(false)) {
                sendResponse(t, UNAUTHORIZED, "Wrong credentials");
                return;
            }

            // Get Calendar
            UUID calendarId = UUID.fromString(path[2]);
            Optional<RestCalendar> calendar = user.get().getCalendars().stream()
                    .filter(c -> c.getCalendarId().equals(calendarId))
                    .findFirst();

            // Calendar does not exist
            if (calendar.isEmpty()) {
                sendResponse(t, NOT_FOUND, "Calendar not found");
            }

            // Options
            Optional<String> title = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("title"));
            Optional<String> description = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("description"));
            Optional<LocalDateTime> startTime = Optional.empty();
            Optional<LocalDateTime> endTime = Optional.empty();
            Optional<Color> color = Optional.empty();
            Optional<EventType> type = Optional.empty();
            try {
                startTime = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("start"))
                        .map(LocalDateTime::parse);
                endTime = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("end"))
                        .map(LocalDateTime::parse);
            } catch (DateTimeParseException e) {
                sendResponse(t, BAD_REQUEST, "Wrong dates for start or end");
                return;
            }
            try {
                color = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("color"))
                        .map(Color::web);
                type = Optional
                        .ofNullable(t.getRequestHeaders().getFirst("type"))
                        .map(EventType::valueOf);
            } catch (IllegalArgumentException e) {
                sendResponse(t, BAD_REQUEST, "Wrong format for color or type");
                return;
            }

            // Event creation
            if (t.getRequestMethod().equals("POST")) {

                if (startTime.isEmpty() || endTime.isEmpty()) {
                    sendResponse(t,
                                 BAD_REQUEST,
                                 "Event start and end time are required");
                    return;
                }

                if (startTime.get().isAfter(endTime.get())) {
                    sendResponse(t, 400, "Event end time can't be before start time");
                    return;
                }

                Event event = new Event(title.orElse("Untitled event"), description.orElse(""), startTime.get(),
                        endTime.get(), color.orElse(Color.BLUE), type.orElse(EventType.REGULAR));
                calendar.get().addEvent(event);
                sendResponse(t,
                             CREATED,
                             "Event with id: '" + event.getId()
                                     + "' successfully created");
                return;
            }

            // Get event
            UUID eventId = UUID.fromString(path[3]);
            Optional<Event> event = calendar.get().getEvents().stream()
                    .filter(e -> e.getId().equals(eventId)).findFirst();

            // Event does not exist
            if (event.isEmpty()) {
                sendResponse(t, NOT_FOUND, "Event not found");
            }

            // Event deletion
            if (t.getRequestMethod().equals("DELETE")) {
                calendar.get().removeEvent(event.get());
                sendResponse(t,
                             OK,
                             "Event with id: '" + event.get().getId()
                                     + "' successfully deleted");
                return;
            }

            // Event editing
            Event newEvent =
                    new Event(title.orElse(event.get().getTitle()),
                              description.orElse(event.get().getDescription()),
                              startTime.orElse(event.get().getStartTime()),
                              endTime.orElse(event.get().getEndTime()),
                              color.orElse(event.get().getColor()),
                              type.orElse(event.get().getType()),
                              event.get().getId());

            calendar.get().removeEvent(event.get());
            calendar.get().addEvent(newEvent);

            sendResponse(t,
                         OK,
                         "Event with id: '" + event.get().getId()
                                 + "' succesfully edited");
        });

        // Start server
        server.start();

        // Save database
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Persistence.write(userStore, filepath.toString());
            } catch (IOException e) {
                System.err
                        .println("Failed to save userStore with error message: "
                                + e.getMessage());
            }
        }));
    }

    protected static void sendResponse(final HttpExchange t,
            final int status,
            final String message) throws IOException {
        t.sendResponseHeaders(status, message.length());
        OutputStream o = t.getResponseBody();
        o.write(message.getBytes());
        o.close();
    }
}
