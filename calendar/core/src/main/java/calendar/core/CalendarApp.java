package calendar.core;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import calendar.types.Calendar;
import calendar.types.Event;
import calendar.types.User;

public class CalendarApp {
    protected User user;

    public CalendarApp(User user) {
        this.user = user;
        this.user.addCalendar(new Calendar());
    }

    private Stream<Event> eventStream() {
        return user.getCalendars().stream().map(cal -> cal.getEvents()).flatMap(Collection::stream);
    }

    public List<Event> getEvents() {
        return eventStream().toList();
    }

    public List<Event> getEventsBetween(LocalDateTime start, LocalDateTime end) {
        return eventStream().filter(event -> event.getEndTime().isAfter(start) && event.getStartTime().isBefore(end))
                .toList();
    }

    public void addEvent(Event event) {
        addEvent(0, event);
    }

    public void addEvent(int index, Event event) {
        user.getCalendar(index).addEvent(event);
    }

    public void removeEvent(Event event) {
        removeEvent(0, event);
    }

    public void removeEvent(int index, Event event) {
        user.getCalendar(index).removeEvent(event);
    }
}
