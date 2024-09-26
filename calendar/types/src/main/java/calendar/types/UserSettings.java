package calendar.types;

import java.util.TimeZone;
import java.util.UUID;

public class UserSettings {
    private UUID userId;
    private TimeZone timezone;
    private boolean militaryTime;
    private boolean showWeekNr;

    public UserSettings(UUID userId, TimeZone timezone, boolean militaryTime, boolean showWeekNr) {
        this.userId = userId;
        this.timezone = timezone;
        this.militaryTime = militaryTime;
        this.showWeekNr = showWeekNr;
    }

    public UUID getUserId() {
        return userId;
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public boolean getMilitaryTime() {
        return militaryTime;
    }

    public boolean getShowweekNr() {
        return showWeekNr;
    }
}
