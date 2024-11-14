package calendar.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import calendar.persistence.Persistence;
import calendar.types.Event;
import calendar.types.EventType;
import calendar.types.User;
import javafx.scene.paint.Color;
import no.gorandalum.fluentresult.Result;
import no.gorandalum.fluentresult.VoidResult;

/**
 * A utility class providing helper methods for interacting with a restAPI.
 * <p>
 * The {@code RestHelper} class is a final class that offers static methods to
 * manage user and calendar data, including CRUD operations on users, calendars,
 * and events. This class relies on HTTP communication with the server specified
 * by {@link #serverAddress}.
 * </p>
 *
 * @see java.net.http.HttpClient
 */
public final class RestHelper {

    /** The HTTP client used for sending requests to the server. */
    protected static HttpClient client = HttpClient.newHttpClient();

    /** The base address of the server where the API requests are sent. */
    protected static String serverAddress = "http://localhost:8000";

    /** The username for authenticating API requests. */
    protected static String username;

    /** The password for authenticating API requests. */
    protected static String password;

    /**
     * The calendar ID for identifying the specific calendar associated with
     * requests.
     */
    protected static UUID calendarId;

    /** Status code group for {@code Successful responses}. */
    private static final int SUCCESSFUL_RESPONSES = 200;

    /** Status code group for {@code Redirection messages}. */
    private static final int REDIRECTION_MESSAGES = 300;

    private RestHelper() {
    }

    /**
     * Sets the server address for all future requests.
     *
     * @param  address                  the server address to set; must not be
     *                                  {@code null}.
     * @throws IllegalArgumentException if {@code address} is {@code null}.
     */
    public static void setServerAddress(final String address) {
        if (address == null)
            throw new IllegalArgumentException("Server address can't be null");

        serverAddress = address;
    }

    /**
     * Sets the credentials for authenticating API requests.
     *
     * @param  username                 the username for authentication; must
     *                                  not be {@code null}.
     * @param  password                 the password for authentication; must
     *                                  not be {@code null}.
     * @throws IllegalArgumentException if either {@code username} or
     *                                  {@code password} is {@code null}.
     */
    public static void setCredentials(final String username,
            final String password) {
        if (username == null || password == null)
            throw new IllegalArgumentException("Credentials can't be null");

        RestHelper.username = username;
        RestHelper.password = password;
    }

    /**
     * Sets the calendar ID for use in calendar-related API requests.
     *
     * @param  calendarId               the calendar ID to set; must not be
     *                                  {@code null}.
     * @throws IllegalArgumentException if {@code calendarId} is {@code null}.
     */
    public static void setCaledarId(final UUID calendarId) {
        if (calendarId == null)
            throw new IllegalArgumentException("Calendar ID can't be null");

        RestHelper.calendarId = calendarId;
    }

    private static boolean hasCredentials() {
        return username != null && password != null;
    }

    private static boolean hasCalendarId() {
        return calendarId != null;
    }

    private static Result<String, String> fetch(final HttpRequest request) {
        HttpResponse<String> response;
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return Result.error("Could not reach server");
        }

        if (response.statusCode() < SUCCESSFUL_RESPONSES
                || response.statusCode() >= REDIRECTION_MESSAGES)
            return Result.error(response.body());

        return Result.success(response.body());
    }

    private static <T> Function<String, Result<T, String>> fromJSON(
            final Class<T> objectType) {
        return json -> {
            try {
                return Result.success(Persistence.fromJSON(objectType, json));
            } catch (IOException e) {
                return Result.error(e.getMessage());
            }
        };
    }

    /**
     * Fetches the details of the current user from the server.
     *
     * @return a {@link Result} containing the {@link User} object on success,
     *         or an error message if the operation fails.
     */
    public static Result<User, String> getUser() {
        if (!hasCredentials()) return Result.error("Credentials are not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/users/" + username)).GET()
                .header("password", password).build();

        return fetch(request).flatMap(fromJSON(User.class));
    }

    /**
     * Adds a new user to the server using the set credentials.
     *
     * @return a {@link VoidResult} indicating success or an error message.
     */
    public static VoidResult<String> addUser() {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/users/" + username))
                .POST(BodyPublishers.noBody()).header("password", password)
                .build();

        return fetch(request).toVoidResult();
    }

    /**
     * Adds a new calendar to the server, optionally with a name.
     *
     * @param  name the optional name of the calendar to be added.
     * @return      a {@link VoidResult} indicating success or an error message.
     */
    public static VoidResult<String> addCalendar(final Optional<String> name) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/calendar"))
                .POST(BodyPublishers.noBody()).header("username", username)
                .header("password", password);

        name.map(calName -> requestBuilder.header("name", calName));

        return fetch(requestBuilder.build()).toVoidResult();
    }

    /**
     * Removes the calendar identified by the set {@code calendarId} from the
     * server.
     *
     * @return a {@link VoidResult} indicating success or an error message.
     */
    public static VoidResult<String> removeCalendar() {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId()) return VoidResult.error("Calendar ID is not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/calendar"
                        + calendarId.toString()))
                .DELETE().header("username", username)
                .header("password", password).build();

        return fetch(request).toVoidResult();
    }

    /**
     * Retrieves the events for the calendar, filtered by date.
     *
     * @param  before an optional date to filter events that occur before this
     *                date.
     * @param  after  an optional date to filter events that occur after this
     *                date.
     * @return        a {@link Result} containing a list of {@link Event}
     *                objects on success, or an error message if the operation
     *                fails.
     */
    public static Result<List<Event>, String> getEvents(
            final Optional<LocalDateTime> before,
            final Optional<LocalDateTime> after) {
        if (!hasCredentials()) return Result.error("Credentials are not set");
        if (!hasCalendarId()) return Result.error("Calendar ID is not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/calendar/"
                        + calendarId.toString()))
                .GET().header("username", username)
                .header("password", password);

        before.map(date -> requestBuilder.header("before", date.toString()));
        after.map(date -> requestBuilder.header("after", date.toString()));

        return fetch(requestBuilder.build()).flatMap(fromJSON(Event[].class))
                .map(List::of);
    }

    /**
     * Adds an event to the calendar with optional details.
     *
     * @param  title       the optional title of the event.
     * @param  description the optional description of the event.
     * @param  startTime   the optional start time of the event.
     * @param  endTime     the optional end time of the event.
     * @param  color       the optional color of the event.
     * @param  type        the optional type of the event.
     * @return             a {@code VoidResult} indicating success or an error
     *                     message.
     */
    public static VoidResult<String> addEvent(final Optional<String> title,
            final Optional<String> description,
            final Optional<LocalDateTime> startTime,
            final Optional<LocalDateTime> endTime,
            final Optional<Color> color,
            final Optional<EventType> type) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId()) return VoidResult.error("Calendar ID is not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/event/"
                        + calendarId.toString()))
                .POST(BodyPublishers.noBody()).header("username", username)
                .header("password", password);

        title.map(t -> requestBuilder.header("title", t));
        description.map(d -> requestBuilder.header("description", d));
        startTime
                .map(start -> requestBuilder.header("start", start.toString()));
        endTime.map(end -> requestBuilder.header("end", end.toString()));
        color.map(c -> requestBuilder.header("color", c.toString()));
        type.map(t -> requestBuilder.header("type", t.toString()));

        return fetch(requestBuilder.build()).toVoidResult();
    }

    /**
     * Removes an event from the calendar by event ID.
     *
     * @param  eventId the ID of the event to be removed.
     * @return         a {@code VoidResult} indicating success or an error
     *                 message.
     */
    public static VoidResult<String> removeEvent(final UUID eventId) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId()) return VoidResult.error("Calendar ID is not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/event/"
                        + calendarId.toString()
                        + "/"
                        + eventId.toString()))
                .DELETE().header("username", username)
                .header("password", password).build();

        return fetch(request).toVoidResult();
    }

    /**
     * Edits an existing event's details by event ID, with optional new details.
     *
     * @param  eventId     the ID of the event to be edited.
     * @param  title       an optional new title for the event.
     * @param  description an optional new description for the event.
     * @param  startTime   an optional new start time for the event.
     * @param  endTime     an optional new end time for the event.
     * @param  color       an optional new color for the event.
     * @param  type        an optional new type for the event.
     *
     * @return             a {@code VoidResult} indicating success or an error
     *                     message if the operation fails.
     */
    public static VoidResult<String> editEvent(final UUID eventId,
            final Optional<String> title,
            final Optional<String> description,
            final Optional<LocalDateTime> startTime,
            final Optional<LocalDateTime> endTime,
            final Optional<Color> color,
            final Optional<EventType> type) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId()) return VoidResult.error("Calendar ID is not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/event/"
                        + calendarId.toString()
                        + "/"
                        + eventId.toString()))
                .method("PATCH", BodyPublishers.noBody())
                .header("username", username).header("password", password);

        title.map(t -> requestBuilder.header("title", t));
        description.map(d -> requestBuilder.header("description", d));
        startTime
                .map(start -> requestBuilder.header("start", start.toString()));
        endTime.map(end -> requestBuilder.header("end", end.toString()));
        color.map(c -> requestBuilder.header("color", c.toString()));
        type.map(t -> requestBuilder.header("type", t.toString()));

        return fetch(requestBuilder.build()).toVoidResult();
    }
}
