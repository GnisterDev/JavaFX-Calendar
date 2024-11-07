package calendar.core;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import calendar.types.RestCalendar;
import calendar.types.Event;
import calendar.types.RestUser;
import calendar.types.EventType;
import javafx.scene.paint.Color;

/**
 * The {@code CalendarApp} class provides functionalities to manage events for a
 * specific user within their calendars. It allows users to add, remove, and
 * query events, as well as create new events with validation.
 */
public class CalendarApp {

    /** Constant representing the number of hours in a day. */
    public static final int HOURS_IN_A_DAY = 24;

    /** Constant representing the number of days in a week. */
    public static final int DAYS_IN_A_WEEK = 7;

    /**
     * Flag indicating whether an event has a maximum allowed length. Set to
     * false by default, meaning there is no length restriction unless otherwise
     * specified.
     */
    private static final boolean EVENT_HAS_MAX_LENGTH = false;

    /** Constant representing the maximum length of an event in days. */
    public static final int MAX_EVENT_LENGTH_IN_DAYS = 7;

    /** The user currently. */
    protected RestUser user;

    /**
     * Constructs a {@code CalendarApp} for the specified user.
     *
     * @param user the user whose calendar is being managed by this application
     */
    public CalendarApp(final RestUser user) {
        this.user = user;
        this.user.addCalendar(new RestCalendar());
    }

    /**
     * Retrieves a stream of all events across all calendars for the user.
     *
     * @return a {@link Stream} of {@link Event} objects
     */
    private Stream<Event> eventStream() {
        return user.getCalendars().stream().map(cal -> cal.getEvents())
                .flatMap(Collection::stream);
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
     * Retrieves a list of events that occur between the specified start and end
     * times.
     *
     * @param  start the start time for filtering events
     * @param  end   the end time for filtering events
     * @return       a {@link List} of {@link Event} objects occurring between
     *               the specified times.
     */
    public List<Event> getEventsBetween(final LocalDateTime start,
            final LocalDateTime end) {
        return eventStream().filter(event -> event.getEndTime().isAfter(start)
                && event.getStartTime().isBefore(end)).toList();
    }

    /**
     * Adds an event to the user's default calendar (index 0).
     *
     * @param event the {@link Event} to add
     */
    public void addEvent(final Event event) {
        addEvent(0, event);
    }

    /**
     * Adds an event to a specified calendar by index.
     *
     * @param index the index of the calendar to which the event should be
     *              added.
     * @param event the {@link Event} to add
     */
    public void addEvent(final int index, final Event event) {
        user.getCalendar(index).addEvent(event);
    }

    /**
     * Creates a new event with validation for title, start, and end times.
     *
     * @param  title       the title of the event
     * @param  description a description of the event
     * @param  startTime   the start time of the event
     * @param  endTime     the end time of the event
     * @param  color       the {@link Color} of the event
     * @param  type        the {@link EventType} of event
     * @return             an {@code Optional<String>} containing an error
     *                     message if validation fails, or an empty
     *                     {@code Optional} if the event is created
     *                     successfully.
     */
    @SuppressWarnings("unused")
    public Optional<String> createEvent(final String title,
            final String description,
            final LocalDateTime startTime,
            final LocalDateTime endTime,
            final Color color,
            final EventType type) {

        if (title.isBlank()) return Optional.of(Error.EVENT_TITLE_IS_BLANK);
        if (startTime.isAfter(endTime))
            return Optional.of(Error.EVENT_START_IS_AFTER_END);
        if (EVENT_HAS_MAX_LENGTH && startTime
                .plusHours(MAX_EVENT_LENGTH_IN_DAYS * HOURS_IN_A_DAY)
                .isAfter(endTime))
            return Optional.of(Error.EVENT_TOO_LONG);

        Event newEvent =
                new Event(title, description, startTime, endTime, color, type);
        if (eventStream().anyMatch(p -> p.equals(newEvent)))
            return Optional.of(Error.EVENT_ALREADY_EXISTS);
        addEvent(newEvent);
        return Optional.empty();
    }

    /**
     * Removes an event from the user's default calendar (index 0).
     *
     * @param event the {@link Event} to remove
     */
    public void removeEvent(final Event event) {
        removeEvent(0, event);
    }

    /**
     * Removes an event from a specified calendar by index.
     *
     * @param index the index of the calendar from which the event should be
     *              removed.
     * @param event the {@link Event} to remove
     */
    public void removeEvent(final int index, final Event event) {
        user.getCalendar(index).removeEvent(event);
    }
}
