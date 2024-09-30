package calendar.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty
    private UUID userId;
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private List<Calendar> calendars;
    private UserSettings settings;

    public User(String username, String password) {
        this.userId = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.calendars = new ArrayList<>();
        this.settings = new UserSettings(userId);
    }

    @JsonCreator
    public User(@JsonProperty("userId") UUID userId,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("calendars") List<Calendar> calendars) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.calendars = new ArrayList<>(calendars);
        this.settings = settings;
    }

    // public UUID getUserId() {
    // return userId;
    // }

    public String getUsername() {
        return username;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public List<Calendar> getCalendars() {
        return new ArrayList<>(calendars);
    }

    public Calendar getCalendar(int index) {
        return calendars.get(index);
    }

    public void removeCalendar(Calendar calendar) {
        calendars.remove(calendar);
    }

    public void removeCalendar(int index) {
        calendars.remove(index);
    }

    public void addCalendar(Calendar calendar) {
        calendars.add(calendar);
    }

    public int calendarCount() {
        return calendars.size();
    }
}
