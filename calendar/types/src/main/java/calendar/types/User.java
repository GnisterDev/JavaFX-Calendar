package calendar.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID userId;
    private String username;
    private String password;
    private List<Calendar> calendars;

    public User(String username, String password) {
        this(UUID.randomUUID(), username, password, new ArrayList<>());
    }

    public User(UUID userId, String username, String password, List<Calendar> calendars) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.calendars = new ArrayList<>(calendars);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
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
