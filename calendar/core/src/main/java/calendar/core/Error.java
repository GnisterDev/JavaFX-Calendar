package calendar.core;

public final class Error {
    private Error() {
    }

    /** Error message fow when start time or end time is not selected. */
    public static final String EVENT_START_END_TIME_NOT_SELECTED =
            "Start and end times must be selected.";
    /** Error message for when the title is blank. */
    public static final String EVENT_TITLE_IS_BLANK = "Title cannot be blank";
    /** Error message for when end time is before start time. */
    public static final String EVENT_START_IS_AFTER_END =
            "Start time cannot be after end time";
    /** Error message for when a simmelar event already exists. */
    public static final String EVENT_ALREADY_EXISTS = "Event already exists";
    /** Error message for when the event last too long. */
    public static final String EVENT_TOO_LONG = "Event can be a maximum of "
            + CalendarApp.MAX_EVENT_LENGTH_IN_DAYS
            + " days ("
            + CalendarApp.MAX_EVENT_LENGTH_IN_DAYS * CalendarApp.HOURS_IN_A_DAY
            + " hours)";

    /** Error message for when the username or password is inncorrect. */
    public static final String LOGIN_USERNAME_OR_PASSWORD_INCORRECT =
            "Username or password is incorrect.";

    /** Error message for when the selected username already exists. */
    public static final String SIGNUP_USERNAME_ALREADY_EXISTS =
            "Username already exists.";
    /** Error message for when the username is empty. */
    public static final String SIGNUP_USERNAME_IS_EMPTY =
            "Username cannot be empty";
    /** Error message for when the password is empty. */
    public static final String SIGNUP_PASSWORD_IS_EMPTY =
            "Password cannot be empty";
    /** Error message for when the password is too short. */
    public static final String SIGNUP_PASSWORD_TOO_SHORT =
            "Password must be at least " + Core.MIN_PASSWORD_LENGTH
                    + " characters long.";
}
