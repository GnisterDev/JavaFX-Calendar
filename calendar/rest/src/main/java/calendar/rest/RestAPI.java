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

/**
 * The {@code RestAPI} class serves as a simple RESTful API server to manage
 * users, calendars, and events. The API provides endpoints to create, retrieve,
 * and delete users, calendars, and events, along with password-based access
 * control. The API uses JSON-based persistence to store user data across
 * sessions.
 *
 * <p>
 * This API includes three main contexts:
 * <ul>
 * <li><b>/users</b> - for managing users, including creation and password
 * validation</li>
 * <li><b>/calendar</b> - for managing user-specific calendars, supporting
 * calendar creation, retrieval, and deletion</li>
 * <li><b>/event</b> - for managing events within user calendars, supporting
 * creation, retrieval, modification, and deletion of events</li>
 * </ul>
 *
 * <p>
 * The API leverages the following HTTP response status codes:
 * <ul>
 * <li><b>200 OK</b> - for successful requests</li>
 * <li><b>201 Created</b> - for successful resource creation</li>
 * <li><b>400 Bad Request</b> - for requests with missing or invalid
 * parameters</li>
 * <li><b>401 Unauthorized</b> - for requests with incorrect credentials</li>
 * <li><b>404 Not Found</b> - for missing resources or unsupported
 * operations</li>
 * <li><b>409 Conflict</b> - for conflicting resources, such as duplicate
 * usernames</li>
 * </ul>
 *
 * <p>
 * The server listens on a static port and uses a simple JSON-based persistence
 * model for user data. User data is persisted in a JSON file, which is loaded
 * at server start-up and saved during shutdown.
 */
public final class RestAPI {
    private RestAPI() {

    }

    /** The class all the info from the json-file goes into. */
    private static volatile UserStore userStore;

    /** The portnumber the restAPI opens at. */
    private static final int PORT = 8000;

    /** Status code for {@code success}. */
    private static final int OK = 200;
    /** Status code for {@code request succeeded}. */
    private static final int CREATED = 201;

    /**
     * Status code for {@code server cannot or will not process the request}.
     */
    private static final int BAD_REQUEST = 400;
    /** Status code for {@code unauthenticated}. */
    private static final int UNAUTHORIZED = 401;
    /** Status code for {@code server cannot find the requested resource}. */
    private static final int NOT_FOUND = 404;
    /** Status code for {@code request conflict}. */
    private static final int CONFLICT = 409;

    /** Variable used for comparing path lengths. */
    private static final int THREE = 3;
    /** Variable used for comparing path lengths. */
    private static final int FOUR = 4;

    /**
     * The main entry point of the API server. It initializes the user data from
     * a JSON file, defines the RESTful endpoints, and starts the HTTP server.
     *
     * <p>
     * Endpoints supported by the API server:
     * <ul>
     * <li><b>/users</b>: POST (create user), GET (retrieve user info)</li>
     * <li><b>/calendar</b>: POST (create calendar), DELETE (remove calendar),
     * GET (retrieve calendar events)</li>
     * <li><b>/event</b>: POST (create event), DELETE (remove event), PUT
     * (update event)</li>
     * </ul>
     *
     * @param  args        command-line arguments (not used)
     * @throws IOException if there are issues initializing the JSON file or
     *                     starting the server
     */
    public static void main(final String[] args) throws IOException {
        // Make sure file exists
        Path filepath = Path.of("rest/userdata.json");
        if (Files.notExists(filepath) || Files.size(filepath) == 0)
            Files.write(filepath, "null".getBytes("UTF-8"));

        // Load database
        userStore = Persistence.read(UserStore.class, filepath.toString());
        if (userStore == null)
            userStore = new UserStore(new HashMap<>(), new HashMap<>());

        // Define server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Create endpoints
        server.createContext("/users", RestAPI::userContext);
        server.createContext("/calendar", RestAPI::calendarContext);
        server.createContext("/event", RestAPI::eventContext);

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

    /**
     * Handles HTTP requests for the "/users" endpoint, supporting user creation
     * and retrieval.
     * <p>
     * Supported operations:
     * <ul>
     * <li>POST - Create a new user with a specified username and password. If
     * the user already exists, responds with a conflict status.</li>
     * <li>GET - Retrieve user data for an existing user, if valid credentials
     * are provided.</li>
     * </ul>
     * Unsupported operations (PATCH, DELETE, PUT) respond with a "Not found"
     * status.
     *
     * <p>
     * Request Requirements:
     * <ul>
     * <li><b>Path Format</b>: The path must include exactly three segments,
     * e.g., "/users/{username}".</li>
     * <li><b>POST requests</b>:
     * <ul>
     * <li>Include a password in the request headers as "password".</li>
     * <li>If the user already exists, responds with HTTP 409 Conflict.</li>
     * </ul>
     * </li>
     * <li><b>GET requests</b>:
     * <ul>
     * <li>Include a password in the request headers to authenticate.</li>
     * <li>If the user does not exist, responds with HTTP 404 Not Found.</li>
     * <li>If the password is incorrect, responds with HTTP 401
     * Unauthorized.</li>
     * </ul>
     * </li>
     * </ul>
     *
     * <p>
     * Response Status Codes:
     * <ul>
     * <li><b>201 Created</b> - If a user is created successfully.</li>
     * <li><b>200 OK</b> - If a user is retrieved successfully.</li>
     * <li><b>400 Bad Request</b> - If required parameters, such as password,
     * are missing.</li>
     * <li><b>401 Unauthorized</b> - If the provided password is incorrect.</li>
     * <li><b>404 Not Found</b> - If the user does not exist or the path is
     * invalid.</li>
     * <li><b>409 Conflict</b> - If the username already exists.</li>
     * </ul>
     *
     * @param  t           the {@link HttpExchange} object representing the HTTP
     *                     request and response context
     * @throws IOException if an error occurs while sending a response
     */
    private static void userContext(final HttpExchange t) throws IOException {
        if (t.getRequestMethod().equals("PATCH")
                || t.getRequestMethod().equals("DELETE")
                || t.getRequestMethod().equals("PUT")) {
            sendResponse(t, NOT_FOUND, "Not found");
            return;
        }

        // Validate path
        String[] path = t.getRequestURI().getPath().split("/");
        if (path.length != THREE) {
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
            sendResponse(t, NOT_FOUND, "User: '" + username + "' not found");
            return;
        }

        // Validate password
        Optional<String> password =
                Optional.ofNullable(t.getRequestHeaders().getFirst("password"));
        if (password.isEmpty() || !user.get().checkPassword(password.get())) {
            sendResponse(t, UNAUTHORIZED, "Wrong password");
            return;
        }

        // Serialze and send user data
        sendResponse(t, OK, Persistence.toJSON(new User(user.get())));
    };

    /**
     * Handles HTTP requests for the "/calendar" endpoint, supporting calendar
     * creation, deletion, and event retrieval.
     * <p>
     * Supported operations:
     * <ul>
     * <li>POST - Create a new calendar for the authenticated user. If no name
     * is provided, defaults to "Unnamed calendar".</li>
     * <li>DELETE - Delete an existing calendar by its ID.</li>
     * <li>GET - Retrieve events within an existing calendar, optionally
     * filtered by a time range specified in the "before" and "after"
     * headers.</li>
     * </ul>
     * Unsupported operation (PATCH) responds with a "Not found" status.
     *
     * <p>
     * Request Requirements:
     * <ul>
     * <li><b>Path Format</b>:
     * <ul>
     * <li>POST requests should have a path format of "/calendar".</li>
     * <li>DELETE and GET requests should have a path format of
     * "/calendar/{calendarId}".</li>
     * </ul>
     * </li>
     * <li><b>Credentials</b>: Include "username" and "password" in the request
     * headers to authenticate.</li>
     * </ul>
     *
     * <p>
     * Response Status Codes:
     * <ul>
     * <li><b>201 Created</b> - If a calendar is created successfully.</li>
     * <li><b>200 OK</b> - If events are retrieved or a calendar is deleted
     * successfully.</li>
     * <li><b>400 Bad Request</b> - If required parameters, such as credentials
     * or dates, are missing or invalid.</li>
     * <li><b>401 Unauthorized</b> - If the provided credentials are
     * incorrect.</li>
     * <li><b>404 Not Found</b> - If the calendar does not exist or the path is
     * invalid.</li>
     * </ul>
     *
     * <p>
     * Filtering Options:
     * <ul>
     * <li><b>before</b> (Optional) - Specify a timestamp in the "before" header
     * to retrieve events occurring before this time.</li>
     * <li><b>after</b> (Optional) - Specify a timestamp in the "after" header
     * to retrieve events occurring after this time.</li>
     * </ul>
     *
     * @param  t           the {@link HttpExchange} object representing the HTTP
     *                     request and response context
     * @throws IOException if an error occurs while sending a response
     */
    private static void calendarContext(final HttpExchange t)
            throws IOException {
        if (t.getRequestMethod().equals("PATCH")) {
            sendResponse(t, NOT_FOUND, "Not found");
            return;
        }

        // Validate path
        String[] path = t.getRequestURI().getPath().split("/");
        if (!(path.length == THREE
                || (path.length == 2 && t.getRequestMethod().equals("POST")))) {
            sendResponse(t, BAD_REQUEST, "Wrong number of arguments");
            return;
        }

        // Get credentials
        Optional<String> password =
                Optional.ofNullable(t.getRequestHeaders().getFirst("password"));
        Optional<String> username =
                Optional.ofNullable(t.getRequestHeaders().getFirst("username"));

        // Credentials not provided
        if (password.isEmpty() || username.isEmpty()) {
            sendResponse(t, BAD_REQUEST, "Credentials required");
            return;
        }

        // Wrong credentials
        Optional<RestUser> user =
                userStore.getUserId(username.get()).flatMap(userStore::getUser);
        if (!user.map(u -> u.checkPassword(password.get())).orElse(false)) {
            sendResponse(t, UNAUTHORIZED, "Wrong credentials");
            return;
        }

        // Calendar creation
        if (t.getRequestMethod().equals("POST")) {
            Optional<String> name =
                    Optional.ofNullable(t.getRequestHeaders().getFirst("name"));
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
                .filter(c -> c.getCalendarId().equals(calendarId)).findFirst();

        // Calendar does not exist
        if (calendar.isEmpty()) {
            sendResponse(t, NOT_FOUND, "Calendar not found");
        }

        // Calendar deletion
        if (t.getRequestMethod().equals("DELETE")) {
            user.get().removeCalendar(calendar.get());
            sendResponse(t,
                         OK,
                         "Calendar with id: '" + calendar.get().getCalendarId()
                                 + "' successfully deleted");
            return;
        }

        // Filtering options
        Optional<LocalDateTime> before;
        Optional<LocalDateTime> after;
        try {
            before = Optional
                    .ofNullable(t.getRequestHeaders().getFirst("before"))
                    .map(s -> LocalDateTime.parse(s));
            after = Optional.ofNullable(t.getRequestHeaders().getFirst("after"))
                    .map(s -> LocalDateTime.parse(s));
        } catch (DateTimeParseException e) {
            sendResponse(t, BAD_REQUEST, "Wrong dates for before or after");
            return;
        }

        // Get events
        List<Event> events = calendar.get().getEvents().stream()
                .filter(event -> before.map(event.getStartTime()::isBefore)
                        .orElse(true)
                        && after.map(event.getEndTime()::isAfter).orElse(true))
                .toList();

        sendResponse(t, OK, Persistence.toJSON(events));
    };

    /**
     * Handles HTTP requests for the "/event" endpoint, providing operations for
     * event creation, deletion, and editing.
     * <p>
     * Supported operations:
     * <ul>
     * <li>POST - Create a new event in a specified calendar for the
     * authenticated user. Requires start and end time.</li>
     * <li>DELETE - Delete an existing event by its ID within a specified
     * calendar.</li>
     * <li>PUT - Edit details of an existing event by its ID, allowing
     * modifications to title, description, times, color, and type.</li>
     * </ul>
     * Unsupported operation (GET) responds with a "Not found" status.
     *
     * <p>
     * Request Requirements:
     * <ul>
     * <li><b>Path Format</b>:
     * <ul>
     * <li>POST requests should have a path format of
     * "/calendar/{calendarId}/event".</li>
     * <li>DELETE and PUT requests should have a path format of
     * "/calendar/{calendarId}/event/{eventId}".</li>
     * </ul>
     * </li>
     * <li><b>Credentials</b>: Include "username" and "password" in the request
     * headers to authenticate.</li>
     * <li><b>Event Properties</b> (for POST and PUT):
     * <ul>
     * <li><b>start</b> and <b>end</b> (required for POST, optional for PUT) -
     * Specify event start and end times in ISO-8601 format.</li>
     * <li><b>title</b> (Optional) - Title of the event; defaults to "Untitled
     * event" if not provided.</li>
     * <li><b>description</b> (Optional) - Description of the event.</li>
     * <li><b>color</b> (Optional) - Event color in web format; defaults to blue
     * if not provided.</li>
     * <li><b>type</b> (Optional) - Event type; defaults to REGULAR if not
     * specified.</li>
     * </ul>
     * </li>
     * </ul>
     *
     * <p>
     * Response Status Codes:
     * <ul>
     * <li><b>201 Created</b> - If an event is created successfully.</li>
     * <li><b>200 OK</b> - If an event is edited or deleted successfully.</li>
     * <li><b>400 Bad Request</b> - If required parameters, such as credentials
     * or event times, are missing or invalid.</li>
     * <li><b>401 Unauthorized</b> - If the provided credentials are
     * incorrect.</li>
     * <li><b>404 Not Found</b> - If the calendar or event does not exist or the
     * path is invalid.</li>
     * </ul>
     *
     * <p>
     * Validation:
     * <ul>
     * <li>Event start and end times are required for event creation. End time
     * must be after start time.</li>
     * <li>Color must be a valid web color format, and type must match defined
     * event types.</li>
     * </ul>
     *
     * @param  t           the {@link HttpExchange} object representing the HTTP
     *                     request and response context
     * @throws IOException if an error occurs while sending a response
     */
    private static void eventContext(final HttpExchange t) throws IOException {
        if (t.getRequestMethod().equals("GET")) {
            sendResponse(t, NOT_FOUND, "Not found");
            return;
        }

        // Validate path
        String[] path = t.getRequestURI().getPath().split("/");
        if (!(path.length == FOUR || (t.getRequestMethod().equals("POST")
                && path.length == THREE))) {
            sendResponse(t, BAD_REQUEST, "Wrong number of arguments");
            return;
        }

        // Get credentials
        Optional<String> password =
                Optional.ofNullable(t.getRequestHeaders().getFirst("password"));
        Optional<String> username =
                Optional.ofNullable(t.getRequestHeaders().getFirst("username"));

        // Credentials not provided
        if (password.isEmpty() || username.isEmpty()) {
            sendResponse(t, BAD_REQUEST, "Credentials required");
            return;
        }

        // Wrong credentials
        Optional<RestUser> user =
                userStore.getUserId(username.get()).flatMap(userStore::getUser);
        if (!user.map(u -> u.checkPassword(password.get())).orElse(false)) {
            sendResponse(t, UNAUTHORIZED, "Wrong credentials");
            return;
        }

        // Get Calendar
        UUID calendarId = UUID.fromString(path[2]);
        Optional<RestCalendar> calendar = user.get().getCalendars().stream()
                .filter(c -> c.getCalendarId().equals(calendarId)).findFirst();

        // Calendar does not exist
        if (calendar.isEmpty()) {
            sendResponse(t, NOT_FOUND, "Calendar not found");
        }

        // Options
        Optional<String> title =
                Optional.ofNullable(t.getRequestHeaders().getFirst("title"));
        Optional<String> description = Optional
                .ofNullable(t.getRequestHeaders().getFirst("description"));

        Optional<LocalDateTime> startTime;
        Optional<LocalDateTime> endTime;
        Optional<EventType> type;
        Optional<Color> color;
        try {
            startTime =
                    Optional.ofNullable(t.getRequestHeaders().getFirst("start"))
                            .map(LocalDateTime::parse);
            endTime = Optional.ofNullable(t.getRequestHeaders().getFirst("end"))
                    .map(LocalDateTime::parse);
        } catch (DateTimeParseException e) {
            sendResponse(t, BAD_REQUEST, "Wrong dates for start or end");
            return;
        }
        try {
            color = Optional.ofNullable(t.getRequestHeaders().getFirst("color"))
                    .map(Color::web);
            type = Optional.ofNullable(t.getRequestHeaders().getFirst("type"))
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
                sendResponse(t,
                             BAD_REQUEST,
                             "Event end time can't be before start time");
                return;
            }

            Event event = new Event(title.orElse("Untitled event"),
                                    description.orElse(""),
                                    startTime.get(),
                                    endTime.get(),
                                    color.orElse(Color.BLUE),
                                    type.orElse(EventType.REGULAR));
            calendar.get().addEvent(event);
            sendResponse(t,
                         CREATED,
                         "Event with id: '" + event.getId()
                                 + "' successfully created");
            return;
        }

        // Get event
        UUID eventId = UUID.fromString(path[THREE]);
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
    };

    /**
     * Sends an HTTP response with the specified status and message.
     *
     * @param  t           the HttpExchange object representing the HTTP request
     *                     and response context
     * @param  status      the HTTP status code to send in the response
     * @param  message     the message body to send in the response
     * @throws IOException if an I/O error occurs while sending the response
     */
    protected static void sendResponse(final HttpExchange t,
            final int status,
            final String message) throws IOException {
        t.sendResponseHeaders(status, message.length());
        OutputStream o = t.getResponseBody();
        o.write(message.getBytes("UFT-8"));
        o.close();
    }
}
