package coding.challenge.domain;

import java.time.LocalTime;
import java.util.Objects;

public class Event {
    private LocalTime timestamp;
    private EventType eventType;
    private String team;

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return timestamp.equals(event.timestamp) &&
                eventType == event.eventType &&
                team.equals(event.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, eventType, team);
    }
}
