package calendar.core;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import calendar.types.Event;
import calendar.types.User;
import javafx.scene.paint.Color;

public class CalendarApp {
    private static boolean EVENT_HAS_MAX_LENGTH = false;
    public static int HOURS_IN_A_DAY = 24;
    public static int DAYS_IN_A_WEEK = 7;
    private static int MAX_EVENT_LENGTH_IN_DAYS = 7;
    private static int MAX_EVENT_LENGTH_IN_HOURS = MAX_EVENT_LENGTH_IN_DAYS * HOURS_IN_A_DAY;

    protected User user;

    public CalendarApp(User user) {
        this.user = user;
    }

    private Stream<Event> eventStream() {
        return user.getCalendars().stream().map(cal -> cal.getEvents()).flatMap(Collection::stream);
    }

    public List<Event> getEvents() {
        return eventStream().toList();
    }

    public List<Event> getEventsBetween(LocalDateTime start, LocalDateTime end) {
        return eventStream().filter(event -> event.getEndTime().isAfter(start) && event.getStartTime().isBefore(end))
                .toList();
    }

    public void addEvent(Event event) {
        addEvent(0, event);
    }

    public void addEvent(int index, Event event) {
        user.getCalendar(index).addEvent(event);
    }

    public Optional<String> createEvent(String title, String description, LocalDateTime startTime,
            LocalDateTime endTime, Color color) {
        if (title.isBlank())
            return Optional.of("Title cannot be blank");
        if (startTime.isAfter(endTime))
            return Optional.of("Starttime cannot be after endtime");
        if (EVENT_HAS_MAX_LENGTH && startTime.plusHours(MAX_EVENT_LENGTH_IN_HOURS).isAfter(endTime))
            return Optional.of("Event can be a maximum of"
                    + MAX_EVENT_LENGTH_IN_DAYS
                    + "days ("
                    + MAX_EVENT_LENGTH_IN_HOURS
                    + " hours)");
        Event newEvent = new Event(title, description, startTime, endTime, color);
        if (eventStream().anyMatch(p -> p.equals(newEvent)))
            return Optional.of("Event already exists");
        addEvent(newEvent);
        return Optional.empty();
    }

    public void removeEvent(Event event) {
        removeEvent(0, event);
    }

    public void removeEvent(int index, Event event) {
        user.getCalendar(index).removeEvent(event);
    }
}
