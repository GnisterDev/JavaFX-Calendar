package calendar.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.net.ssl.SSLSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import calendar.persistence.Persistence;
import calendar.types.Calendar;
import calendar.types.Event;
import calendar.types.User;
import calendar.types.UserSettings;
import calendar.types.EventType;
import javafx.scene.paint.Color;
import no.gorandalum.fluentresult.Result;
import no.gorandalum.fluentresult.VoidResult;

public class RestHelperTest {

    private Event event1 = new Event("event1", "description 1", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
    private Event event2 = new Event("event2", "description 2", LocalDateTime.now().plusHours(3),
            LocalDateTime.now().plusHours(6));
    private Calendar cal1 = new Calendar(UUID.randomUUID(), "cal1");
    private Calendar cal2 = new Calendar(UUID.randomUUID(), "cal2");
    private UUID uuid = UUID.randomUUID();
    private UserSettings settings = new UserSettings(uuid);
    private User user = new User(uuid, "username", List.of(cal1, cal2), settings);

    private <S, E> void assertError(Result<S, E> result) {
        assertError(result.toVoidResult());
    }

    private <E> void assertError(VoidResult<E> result) {
        assertThrowsExactly(
                Exception.class,
                () -> result.orElseThrow(s -> new Exception()));
    }

    @BeforeEach
    public void beforeEach() {
        RestHelper.username = null;
        RestHelper.password = null;
        RestHelper.calendarId = null;
        RestHelper.client = mock(HttpClient.class);
    }

    @Test
    public void testSetters() {
        assertThrowsExactly(IllegalArgumentException.class, () -> RestHelper.setServerAddress(null));
        assertThrowsExactly(IllegalArgumentException.class, () -> RestHelper.setCredentials(null, null));
        assertThrowsExactly(IllegalArgumentException.class, () -> RestHelper.setCaledarId(null));

        RestHelper.setServerAddress("address");
        assertEquals("address", RestHelper.serverAddress);

        RestHelper.setCredentials("username", "password");
        assertEquals("username", RestHelper.username);
        assertEquals("password", RestHelper.password);

        UUID uuid = UUID.randomUUID();
        RestHelper.setCaledarId(uuid);
        assertEquals(uuid, RestHelper.calendarId);
    }

    @Test
    public void testCredentialsNotSet() {
        assertError(RestHelper.getUser());
        assertError(RestHelper.addUser());
        assertError(RestHelper.addCalendar(Optional.empty()));
        assertError(RestHelper.removeCalendar());
        assertError(RestHelper.getEvents(Optional.empty(), Optional.empty()));
        assertError(
                RestHelper.addEvent(Optional.of("name"), Optional.of("description"), Optional.of(LocalDateTime.now()),
                        Optional.of(LocalDateTime.now().plusHours(2)), Optional.empty(), Optional.empty()));
        assertError(RestHelper.removeEvent(UUID.randomUUID()));
        assertError(
                RestHelper.editEvent(UUID.randomUUID(), Optional.of("new name"), Optional.of("new description"),
                        Optional.empty(),
                        Optional.empty(), Optional.empty(), Optional.empty()));

    }

    @Test
    public void testCalendarIdNotSet() {
        RestHelper.setCredentials("username", "password");
        assertError(RestHelper.removeCalendar());
        assertError(RestHelper.getEvents(Optional.empty(), Optional.empty()));
        assertError(
                RestHelper.addEvent(Optional.of("name"), Optional.of("description"), Optional.of(LocalDateTime.now()),
                        Optional.of(LocalDateTime.now().plusHours(2)), Optional.empty(), Optional.empty()));
        assertError(RestHelper.removeEvent(UUID.randomUUID()));
        assertError(
                RestHelper.editEvent(UUID.randomUUID(), Optional.of("new name"), Optional.of("new description"),
                        Optional.empty(),
                        Optional.empty(), Optional.empty(), Optional.empty()));
    }

    @Test
    public void testGetUser() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "password");

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200, Persistence.toJSON(user)));
        RestHelper.getUser().consume(receivedUser -> {
            assertEquals(user.getUserId(), receivedUser.getUserId());
            assertEquals(user.getUsername(), receivedUser.getUsername());
            assertEquals(user.getCalendars().size(), receivedUser.getCalendars().size());
            for (int i = 0; i < user.getCalendars().size(); i++) {
                Calendar cal = user.getCalendars().get(i);
                Calendar receivedCal = receivedUser.getCalendars().get(i);
                assertEquals(cal.getName(), receivedCal.getName());
                assertEquals(cal.getCalendarId(), receivedCal.getCalendarId());
            }
        });

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200));
        assertError(RestHelper.getUser());

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(400));
        assertError(RestHelper.getUser());

        when(RestHelper.client.send(any(), any())).thenThrow(new IOException());
        assertError(RestHelper.getUser());
    }

    @Test
    public void testAddUser() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "password");

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200));
        RestHelper.addUser().orElseThrow(s -> new IllegalStateException("Add user did not succeed"));

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(400));
        assertError(RestHelper.addUser());
    }

    @Test
    public void testAddCalendar() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "pasword");

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200));
        RestHelper.addCalendar(Optional.of("new cal"))
                .orElseThrow(s -> new IllegalStateException("Add calendar did not succeed"));

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(400));
        assertError(RestHelper.addCalendar(Optional.of("new cal")));
    }

    @Test
    public void testRemoveCalendar() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "pasword");
        RestHelper.setCaledarId(UUID.randomUUID());

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200));
        RestHelper.removeCalendar()
                .orElseThrow(s -> new IllegalStateException("Remove calendar did not succeed"));

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(400));
        assertError(RestHelper.removeCalendar());
    }

    @Test
    public void testGetEvents() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "password");
        RestHelper.setCaledarId(UUID.randomUUID());

        when(RestHelper.client.send(any(), any()))
                .thenReturn(new CustomHttpResponse(200, Persistence.toJSON(List.of(event1, event2))));
        RestHelper.getEvents(Optional.of(LocalDateTime.now().plusDays(1)),
                Optional.of(LocalDateTime.now().minusHours(2))).consume(events -> {
                    assertEquals(2, events.size());

                    Event receivedEvent1 = events.get(0);
                    assertEquals(event1.getId(), receivedEvent1.getId());
                    assertEquals(event1.getType(), receivedEvent1.getType());
                    assertEquals(event1.getColor(), receivedEvent1.getColor());
                    assertEquals(event1.getTitle(), receivedEvent1.getTitle());
                    assertEquals(event1.getDescription(), receivedEvent1.getDescription());
                    assertEquals(event1.getStartTime(), receivedEvent1.getStartTime());
                    assertEquals(event1.getEndTime(), receivedEvent1.getEndTime());

                    Event receivedEvent2 = events.get(1);
                    assertEquals(event2.getId(), receivedEvent2.getId());
                    assertEquals(event2.getType(), receivedEvent2.getType());
                    assertEquals(event2.getColor(), receivedEvent2.getColor());
                    assertEquals(event2.getTitle(), receivedEvent2.getTitle());
                    assertEquals(event2.getDescription(), receivedEvent2.getDescription());
                    assertEquals(event2.getStartTime(), receivedEvent2.getStartTime());
                    assertEquals(event2.getEndTime(), receivedEvent2.getEndTime());
                });

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(100));
        assertError(RestHelper.getEvents(Optional.empty(), Optional.empty()));
    }

    @Test
    public void testAddEvent() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "password");
        RestHelper.setCaledarId(UUID.randomUUID());

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200));
        RestHelper
                .addEvent(Optional.of("title"), Optional.of("description"), Optional.of(LocalDateTime.now()),
                        Optional.of(LocalDateTime.now().plusHours(2)), Optional.of(Color.RED),
                        Optional.of(EventType.REGULAR))
                .orElseThrow(s -> new IllegalStateException("Remove event did not succeed"));

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(400));
        assertError(RestHelper.addEvent(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty()));
    }

    @Test
    public void testRemoveEvent() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "password");
        RestHelper.setCaledarId(UUID.randomUUID());

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200));
        RestHelper.removeEvent(UUID.randomUUID())
                .orElseThrow(s -> new IllegalStateException("Remove event did not succeed"));

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(400));
        assertError(RestHelper.removeEvent(UUID.randomUUID()));
    }

    @Test
    public void testEditEvent() throws InterruptedException, IOException {
        RestHelper.setCredentials("username", "password");
        RestHelper.setCaledarId(UUID.randomUUID());

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(200));
        RestHelper
                .editEvent(UUID.randomUUID(), Optional.of("title"), Optional.of("description"),
                        Optional.of(LocalDateTime.now()),
                        Optional.of(LocalDateTime.now().plusHours(2)), Optional.of(Color.RED),
                        Optional.of(EventType.REGULAR))
                .orElseThrow(s -> new IllegalStateException("Remove event did not succeed"));

        when(RestHelper.client.send(any(), any())).thenReturn(new CustomHttpResponse(400));
        assertError(RestHelper.editEvent(UUID.randomUUID(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(),
                Optional.empty(), Optional.empty()));
    }
}

final class CustomHttpResponse implements HttpResponse<Object> {
    private int statusCode;
    private String body;

    public CustomHttpResponse(int statusCode) {
        this(statusCode, "");
    }

    public CustomHttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public HttpRequest request() {
        return null;
    }

    @Override
    public Version version() {
        return null;
    }

    @Override
    public Optional<HttpResponse<Object>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public String body() {
        return body;
    }
}
