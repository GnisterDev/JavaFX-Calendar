/**
 * Provides the core functionality for the calendar application.
 *
 * <p>This package contains the fundamental classes and interfaces that support
 * calendar operations such as creating, updating, and deleting calendar events,
 * as well as managing schedules, reminders, and notifications.
 *
 * The core logic and data handling of the calendar application is implemented
 * here.
 *
 * <h2>Package Overview</h2> The {@code calendar.core} package includes the
 * following primary components: <ul> <li> {@link calendar.core.CalendarApp} -
 * The main entry point of the calendar application. This class initializes the
 * application, setting up required resources and configurations, and launching
 * the core components. </li> <li>{@link calendar.core.Core} - Contains the
 * central logic for managing events and scheduling. This class coordinates
 * operations between different modules and ensures data consistency throughout
 * the application. </li> <li> {@link calendar.core.SceneCore} - Manages the
 * graphical scenes and UI transitions within the application. It is responsible
 * for creating and rendering different views based on user interactions, such
 * as viewing schedules or event details.</li> <li> {@link calendar.core.Error}
 * - Contains all posible errors that the user can come across in the
 * application </li> </ul>
 *
 * <h2>Usage</h2> This package is intended to be used by higher-level modules in
 * the application, such as the UI layer, to perform various calendar
 * operations.
 *
 * <h2>Dependencies</h2> The {@code calendar.core} package depends on the
 * following: <ul> <li>{@code javafx.fxml} - Used for building and managing the
 * UI components of the application, allowing for dynamic and interactive user
 * interfaces.</li> <li>{@code calendar.types} - Defines various data types used
 * across the application, including event details, user preferences, and
 * schedule formats.</li> <li>{@code calendar.persistence} - Handles data
 * storage and retrieval, ensuring that event data, settings, and preferences
 * are saved and loaded consistently.</li> </ul>
 *
 * @since   1.0
 * @version 1.0
 */
package calendar.core;
