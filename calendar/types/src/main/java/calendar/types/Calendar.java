package calendar.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Calendar {
    @JsonProperty
    private UUID userId;
    @JsonProperty
    private List<Event> events;

    public Calendar() {
        this(UUID.randomUUID());
    }

    public Calendar(UUID userId) {
        this(new ArrayList<>(), userId);
    }

    public Calendar(List<Event> events) {
        this(UUID.randomUUID(), events);
    }

    @JsonCreator
    public Calendar(@JsonProperty("userId") UUID userId, @JsonProperty("events") List<Event> events) {
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
