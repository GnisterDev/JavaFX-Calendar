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

public class RestHelper {
    private static HttpClient client = HttpClient.newHttpClient();
    private static String serverAddress = "http://localhost:8000";
    private static String username;
    private static String password;
    private static UUID calendarId;

    private RestHelper() {
    }

    public static void setServerAddress(String address) {
        serverAddress = address;
    }

    public static void setCredentials(String username, String password) {
        if (username == null || password == null)
            throw new IllegalArgumentException("Credentials can't be null");

        RestHelper.username = username;
        RestHelper.password = password;
    }

    public static void setCaledarId(UUID calendarId) {
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

    private static Result<String, String> fetch(HttpRequest request) {
        HttpResponse<String> response;
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return Result.error("Could not reach server");
        }

        if (response.statusCode() < 200 || response.statusCode() >= 300)
            return Result.error(response.body());

        return Result.success(response.body());
    }

    private static <T> Function<String, Result<T, String>> fromJSON(Class<T> objectType) {
        return json -> {
            try {
                return Result.success(Persistence.fromJSON(objectType, json));
            } catch (IOException e) {
                return Result.error(e.getMessage());
            }
        };
    }

    public static Result<User, String> getUser() {
        if (!hasCredentials())
            return Result.error("Credentials are not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/users/" + username))
                .GET()
                .header("password", password)
                .build();

        return fetch(request).flatMap(fromJSON(User.class));
    }

    public static VoidResult<String> addUser() {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/users/" + username))
                .POST(BodyPublishers.noBody())
                .header("password", password)
                .build();

        return fetch(request).toVoidResult();
    }

    public static VoidResult<String> addCalendar(Optional<String> name) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/calendar"))
                .POST(BodyPublishers.noBody())
                .header("username", username)
                .header("password", password);

        name.map(calName -> requestBuilder.header("name", calName));

        return fetch(requestBuilder.build()).toVoidResult();
    }

    public static VoidResult<String> removeCalendar() {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId())
            return VoidResult.error("Calendar ID is not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/calendar" + calendarId.toString()))
                .DELETE()
                .header("username", username)
                .header("password", password)
                .build();

        return fetch(request).toVoidResult();
    }

    public static Result<List<Event>, String> getEvents(
            Optional<LocalDateTime> before,
            Optional<LocalDateTime> after) {
        if (!hasCredentials())
            return Result.error("Credentials are not set");
        if (!hasCalendarId())
            return Result.error("Calendar ID is not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/calendar/" + calendarId.toString()))
                .GET()
                .header("username", username)
                .header("password", password);

        before.map(date -> requestBuilder.header("before", date.toString()));
        after.map(date -> requestBuilder.header("after", date.toString()));

        return fetch(requestBuilder.build()).flatMap(fromJSON(Event[].class)).map(List::of);
    }

    public static VoidResult<String> addEvent(
            Optional<String> title,
            Optional<String> description,
            Optional<LocalDateTime> startTime,
            Optional<LocalDateTime> endTime,
            Optional<Color> color,
            Optional<EventType> type) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId())
            return VoidResult.error("Calendar ID is not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/event/" + calendarId.toString()))
                .POST(BodyPublishers.noBody())
                .header("username", username)
                .header("password", password);

        title.map(t -> requestBuilder.header("title", t));
        description.map(d -> requestBuilder.header("description", d));
        startTime.map(start -> requestBuilder.header("start", start.toString()));
        endTime.map(end -> requestBuilder.header("end", end.toString()));
        color.map(c -> requestBuilder.header("color", c.toString()));
        type.map(t -> requestBuilder.header("color", t.toString()));

        return fetch(requestBuilder.build()).toVoidResult();
    }

    public static VoidResult<String> removeEvent(UUID eventId) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId())
            return VoidResult.error("Calendar ID is not set");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress
                        + "/event/"
                        + calendarId.toString()
                        + "/" + eventId.toString()))
                .DELETE()
                .header("username", username)
                .header("password", password)
                .build();

        return fetch(request).toVoidResult();
    }

    public static VoidResult<String> editEvent(
            Optional<String> title,
            Optional<String> description,
            Optional<LocalDateTime> startTime,
            Optional<LocalDateTime> endTime,
            Optional<Color> color,
            Optional<EventType> type) {
        if (!hasCredentials())
            return VoidResult.error("Credentials are not set");
        if (!hasCalendarId())
            return VoidResult.error("Calendar ID is not set");

        Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/event/" + calendarId.toString()))
                .method("PATCH", BodyPublishers.noBody())
                .header("username", username)
                .header("password", password);

        title.map(t -> requestBuilder.header("title", t));
        description.map(d -> requestBuilder.header("description", d));
        startTime.map(start -> requestBuilder.header("start", start.toString()));
        endTime.map(end -> requestBuilder.header("end", end.toString()));
        color.map(c -> requestBuilder.header("color", c.toString()));
        type.map(t -> requestBuilder.header("color", t.toString()));

        return fetch(requestBuilder.build()).toVoidResult();
    }
}
