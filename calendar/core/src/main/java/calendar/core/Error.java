package calendar.core;

public class Error {
    public static final String EVENT_START_END_TIME_NOT_SELECTED = "Start and end times must be selected.";
    public static final String EVENT_TITLE_IS_BLANK = "Title cannot be blank";
    public static final String EVENT_START_IS_AFTER_END = "Start time cannot be after end time";
    public static final String EVENT_ALREADY_EXISTS = "Event already exists";
    public static final String EVENT_TOO_LONG = "Event can be a maximum of "
            + CalendarApp.MAX_EVENT_LENGTH_IN_DAYS
            + " days ("
            + CalendarApp.MAX_EVENT_LENGTH_IN_HOURS
            + " hours)";

    public static final String LOGIN_USERNAME_OR_PASSWORD_INCORRECT = "Username or password is incorrect.";

    public static final String SIGNUP_USERNAME_ALREADY_EXISTS = "Username already exists.";
    public static final String SIGNUP_USERNAME_IS_EMPTY = "Username cannot be empty";
    public static final String SIGNUP_PASSWORD_IS_EMPTY = "Password cannot be empty";
    public static final String SIGNUP_PASSWORD_TOO_SHORT = "Password must be at least "
            + Core.MIN_PASSWORD_LENGTH
            + " characters long.";
}
