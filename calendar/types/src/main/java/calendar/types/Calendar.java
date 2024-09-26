package calendar.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Calendar {
    private UUID userId;
    private List<Event> events;

    public Calendar() {
        this(UUID.randomUUID());
    }

    public Calendar(UUID userId) {
        this.userId = userId;
    }

    public Calendar(List<Event> events) {
        this(events, UUID.randomUUID());
    }

    public Calendar(List<Event> events, UUID userId) {
        this.events = new ArrayList<>(events);
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public Event getEvent(int index) {
        return events.get(index);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void removeEvent(int index) {
        events.remove(index);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public int eventCount() {
        return events.size();
    }

}
