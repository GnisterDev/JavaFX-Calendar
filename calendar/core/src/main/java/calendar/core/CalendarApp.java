package calendar.core;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import calendar.types.Event;
import calendar.types.User;

/**
 * The {@code CalendarApp} class provides functionalities to manage events for a specific user within their calendars.
 * It allows users to add, remove, and query events, as well as create new events with validation.
 */
public class CalendarApp {
    public static final int HOURS_IN_A_DAY = 24;
    public static final int DAYS_IN_A_WEEK = 7;

    private static final boolean EVENT_HAS_MAX_LENGTH = false;
    private static final int MAX_EVENT_LENGTH_IN_DAYS = 7;
    private static final int MAX_EVENT_LENGTH_IN_HOURS = MAX_EVENT_LENGTH_IN_DAYS * HOURS_IN_A_DAY;

    protected User user;

    /**
     * Constructs a {@code CalendarApp} for the specified user.
     *
     * @param user the user whose calendar is being managed by this application
     */
    public CalendarApp(User user) {
        this.user = user;
    }

    /**
     * Retrieves a stream of all events across all calendars for the user.
     *
     * @return a {@link Stream} of {@link Event} objects
     */
    private Stream<Event> eventStream() {
        return user.getCalendars().stream().map(cal -> cal.getEvents()).flatMap(Collection::stream);
    }

    /**
     * Retrieves a list of all events across all calendars for the user.
     *
     * @return a {@link List} of {@link Event} objects
     */
    public List<Event> getEvents() {
        return eventStream().toList();
    }

    /**
     * Retrieves a list of events that occur between the specified start and end times.
     *
     * @param start the start time for filtering events
     * @param end   the end time for filtering events
     * @return a {@link List} of {@link Event} objects occurring between the specified times
     */
    public List<Event> getEventsBetween(LocalDateTime start, LocalDateTime end) {
        return eventStream()
                .filter(event -> event.getEndTime().isAfter(start) && event.getStartTime().isBefore(end))
                .toList();
    }

    /**
     * Adds an event to the user's default calendar (index 0).
     *
     * @param event the {@link Event} to add
     */
    public void addEvent(Event event) {
        addEvent(0, event);
    }

    /**
     * Adds an event to a specified calendar by index.
     *
     * @param index the index of the calendar to which the event should be added
     * @param event the {@link Event} to add
     */
    public void addEvent(int index, Event event) {
        user.getCalendar(index).addEvent(event);
    }

    /**
     * Creates a new event with validation for title, start, and end times.
     * 
     * @param title       the title of the event
     * @param description a description of the event
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     * @return an {@code Optional<String>} containing an error message if validation fails, or an empty {@code Optional} if the event is created successfully
     */
    @SuppressWarnings("unused")
    public Optional<String> createEvent(
            String title,
            String description,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        if (title.isBlank())
            return Optional.of("Title cannot be blank");
        if (startTime.isAfter(endTime))
            return Optional.of("Start time cannot be after end time");
        if (EVENT_HAS_MAX_LENGTH && startTime.plusHours(MAX_EVENT_LENGTH_IN_HOURS).isAfter(endTime))
            return Optional.of("Event can be a maximum of "
                    + MAX_EVENT_LENGTH_IN_DAYS
                    + " days ("
                    + MAX_EVENT_LENGTH_IN_HOURS
                    + " hours)");
        addEvent(new Event(title, description, startTime, endTime));
        return Optional.empty();
    }

    /**
     * Removes an event from the user's default calendar (index 0).
     *
     * @param event the {@link Event} to remove
     */
    public void removeEvent(Event event) {
        removeEvent(0, event);
    }

    /**
     * Removes an event from a specified calendar by index.
     *
     * @param index the index of the calendar from which the event should be removed
     * @param event the {@link Event} to remove
     */
    public void removeEvent(int index, Event event) {
        user.getCalendar(index).removeEvent(event);
    }
}
