/**
 * Provides the persistence functionality for the calendar application.
 *
 * <p>
 * This package contains classes and interfaces responsible for managing the
 * storage, retrieval, and updating of calendar event data. It enables
 * interaction with the database (JSON-file), ensuring that user events,
 * schedules, and preferences are saved and accessible across sessions.
 *
 * <h2>Package Overview</h2> The {@code calendar.persistence} package includes
 * the following primary components:
 * <ul>
 * <li>{@link calendar.persistence.Persistance} - A Data Access Object (DAO)
 * class that handles the database operations for calendar events, such as
 * creating, reading, updating, and deleting events.</li>
 * <li>{@link calendar.persistence.internal} - Manages the connection to the
 * database, ensuring that database sessions are created and closed efficiently.
 * </li>
 * </ul>
 *
 * <h2>Usage</h2> This package is used by other modules within the calendar
 * application to persist and retrieve event data and user preferences. It
 * abstracts the underlying database operations, allowing other modules to
 * interact with event data without needing detailed knowledge of the storage
 * implementation.
 *
 * <h2>Dependencies</h2> The {@code com.example.persistence} package depends on
 * the following:
 * <ul>
 * <li>{@code calendar.types} - Defines custom data types and domain-specific
 * classes, such as event types, schedule formats, and user preferences, used
 * across the application.</li>
 * <li>{@code com.fasterxml.jackson.annotation} - Provides annotations for
 * controlling JSON serialization and deserialization of entities and custom
 * types. This helps in defining JSON structures when saving and loading data.
 * </li>
 * <li>{@code com.fasterxml.jackson.core} - Contains core classes for handling
 * JSON parsing and generation. This dependency enables efficient data
 * transformation between objects and JSON, a common format for data
 * interchange.</li>
 * <li>{@code com.fasterxml.jackson.databind} - Supports data binding between
 * Java objects and JSON, enabling flexible configuration for serialization and
 * deserialization logic. This module is essential for converting entity objects
 * to JSON and vice versa.</li>
 * <li>{@code com.fasterxml.jackson.datatype.jsr310} - Provides support for Java
 * 8 date and time (JSR-310) types, such as {@code LocalDate} and
 * {@code LocalDateTime}, within the Jackson serialization framework. It ensures
 * seamless JSON conversion of modern date/time types.</li>
 * </ul>
 *
 * @since   1.0
 * @version 1.0
 */
package calendar.persistence;
