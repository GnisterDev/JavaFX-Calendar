package calendar.types;

/**
 * The {@code EventType} enum represents the type of an event.
 *
 * <p>
 * This enum defines the possible types of events that can be associated with an
 * {@link Event}. Currently, the types are not used for anything.
 * </p>
 *
 * The current types are:
 * <ul>
 * <li>{@link #REGULAR} - Represents the first type of event.</li>
 * <li>{@link #ALL_DAY} - Represents the second type of event.</li>
 * </ul>
 */
public enum EventType {
    /** This is the type that shows up in the {@code normal} calendar. */
    REGULAR,
    /**
     * This is the type that shows up in the {@code allDay} section at the top.
     */
    ALL_DAY
}
