/**
 * Provides the core functionality for the calendar application.
 *
 * <p>
 * This package contains the fundamental classes that support
 * calendar operations such as interactions with the restAPI server to manage
 * creating, updating, and deleting calendar events,
 * as well as helper classes for switching between scenes in the application.
 *
 *
 * <h2>Package Overview</h2>
 * The {@code calendar.core} package includes the following primary components:
 * <ul>
 * <li>
 * {@link calendar.core.SceneCore} - Manages the
 * graphical scenes and UI transitions within the application.
 * </li>
 * <li>
 * {@link calendar.core.RestHelper} - Interacts with the restAPI.
 * </li>
 * </ul>
 *
 * <h2>Usage</h2>
 * This package is intended to be used by higher-level modules in
 * the application, such as the UI layer, to perform various calendar
 * operations.
 *
 * <h2>Dependencies</h2>
 * The {@code calendar.core} package depends on the
 * following:
 * <ul>
 * <li>
 * {@code javafx.fxml} - Used for building and managing the
 * UI components of the application, allowing for dynamic and interactive user
 * interfaces.
 * </li>
 * <li>
 * {@code calendar.types} - Defines various data types used
 * across the application, including event details, user preferences, and
 * schedule formats.
 * </li>
 * <li>
 * {@code calendar.persistence} - Handles data
 * storage and retrieval, ensuring that event data, settings, and preferences
 * are saved and loaded consistently.
 * </li>
 * </ul>
 *
 * @since   1.0
 * @version 1.0
 */
package calendar.core;
