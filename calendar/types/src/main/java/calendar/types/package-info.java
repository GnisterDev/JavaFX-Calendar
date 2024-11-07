/**
 * Provides the core functionality for the calendar application.
 *
 * <p>
 * This package contains the fundamental classes and interfaces that support
 * calendar operations such as creating, updating, and deleting calendar events,
 * as well as managing schedules, reminders, and notifications.
 *
 * The core logic and data handling of the calendar application is implemented
 * here.
 *
 * <h2>Package Overview</h2> The {@code calendar.types} package includes the
 * following primary components:
 * <ul>
 * <li>{@link calendar.types.Calendar} - Represents the overall calendar
 * structure, containing all events, settings, and configurations relevant to
 * managing and viewing calendar data.</li>
 * <li>{@link calendar.types.Event} - Defines individual events within the
 * calendar, including details such as the event's start time, end time,
 * description, and associated calendar.</li>
 * <li>{@link calendar.types.EventType} - An enum to help differentiate between
 * various kinds of events, potentially allowing for different handling or
 * styling of each type.</li>
 * <li>{@link calendar.types.User} - Represents a user within the calendar
 * system, containing information such as the user's name, contact details, and
 * preferences.</li>
 * <li>{@link calendar.types.UserSettings} - Contains user-specific preferences
 * and configurations related to their calendar experience.</li>
 * <li>{@link calendar.types.UserStore} - Manages the storage and retrieval of
 * user data, such as user accounts, settings, and associated calendar
 * events.</li>
 * </ul>
 *
 * <h2>Usage</h2> This package is intended to be used by core to handle the
 * spesifics of what to show to the user in the ui moule.
 *
 * <h2>Dependencies</h2> The {@code calendar.types} package depends on the
 * following:
 * <ul>
 * <li>{@code javafx.graphics} - Provides the necessary graphic spesific classes
 * that neeed to be stored.</li>
 * <li>{@code com.fasterxml.jackson.annotation},
 * {@code com.fasterxml.jackson.core}, {@code com.fasterxml.jackson.databind} -
 * Provides the spesific decorates so that the persistance moduale recognizes
 * the fields it needs to be abel to reconstruct the structure stored by
 * it.</li>
 * </ul>
 *
 * @since   1.0
 * @version 1.0
 */
package calendar.types;
