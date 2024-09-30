package calendar.types;

import java.util.TimeZone;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSettings {
    @JsonProperty
    private UUID userId;
    @JsonProperty
    private TimeZone timezone;
    @JsonProperty
    private boolean militaryTime;
    @JsonProperty
    private boolean showWeekNr;

    @JsonCreator
    public UserSettings(@JsonProperty("userId") UUID userId,
            @JsonProperty("timezone") TimeZone timezone,
            @JsonProperty("militaryTime") boolean militaryTime,
            @JsonProperty("showWeekNr") boolean showWeekNr) {
        this.userId = userId;
        this.timezone = timezone;
        this.militaryTime = militaryTime;
        this.showWeekNr = showWeekNr;
    }

    public UserSettings(UUID userId) {
        this(userId, TimeZone.getDefault(), true, false);
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

    public boolean getShowWeekNr() {
        return showWeekNr;
    }
}
