package com.mylab.spring.bootstrap.event;

import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

public class Event {

    private static final Random RANDOMIZER = new Random();

    private final long id;
    private final Date creationDate;
    private final DateFormat dateFormat;
    private String event;
    private EventType type;

    public Event(Date creationDate, DateFormat dateFormat) {
        this.creationDate = creationDate;
        this.dateFormat = dateFormat;
        id = RANDOMIZER.nextInt(Integer.MAX_VALUE);
    }

    public Event(Date creationDate, DateFormat dateFormat, long id) {
        this.creationDate = creationDate;
        this.dateFormat = dateFormat;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getStringEvent() {
        return event;
    }

    public void setStringEvent(String event) {
        this.event = event;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s: #%d {%s} occurred %s",
                Optional.ofNullable(type).map(Enum::name).orElse("Event"),
                id, event, dateFormat.format(creationDate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event1 = (Event) o;

        if (id != event1.id) return false;
        if (!creationDate.equals(event1.creationDate)) return false;
        if (!dateFormat.equals(event1.dateFormat)) return false;
        if (!event.equals(event1.event)) return false;
        return type == event1.type;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + dateFormat.hashCode();
        result = 31 * result + event.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
