# Project Overview: Calendar

## Modules and Their Roles

### 1. calendar.ui

-   **Role:** User Interface
-   **Responsibilities:**
    -   Contains the main entry point (`App.java`).
    -   Includes all FXML files and their controllers.
    -   Handles user interactions and displays data.
-   **Dependencies:** `calendar.core`, `calendar.types`

### 2. calendar.types

-   **Role:** Data Models
-   **Responsibilities:**
    -   Defines data classes such as `Calendar`, `Event`, `UserSettings`, and `UserStore`.
-   **Dependencies:** None

### 3. calendar.persistence

-   **Role:** Data Persistence
-   **Responsibilities:**
    -   Provides functions to read and write data types (`Calendar`, `Event`, etc.) to and from JSON files.
-   **Dependencies:** `calendar.types`

### 4. calendar.core

-   **Role:** Business Logic
-   **Responsibilities:**
    -   Contains the `CalendarApp` class, which handles the core calendar logic.
    -   Manages interactions between the UI and data persistence layers.
-   **Dependencies:** `calendar.types`, `calendar.persistence`
