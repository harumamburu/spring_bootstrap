package com.mylab.spring.event;

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
    private Optional<EventType> type = Optional.empty();

    public Event(Date creationDate, DateFormat dateFormat) {
        this.creationDate = creationDate;
        this.dateFormat = dateFormat;
        id = RANDOMIZER.nextInt(Integer.MAX_VALUE);
    }

    public String getStringEvent() {
        return event;
    }

    public void setStringEvent(String event) {
        this.event = event;
    }

    public EventType getType() {
        return type.orElse(null);
    }

    public void setType(EventType type) {
        this.type = Optional.of(type);
    }

    @Override
    public String toString() {
        return String.format("%s: #%d {%s} occurred %s",
                type.map(t -> t.name()).orElse("Event"),
                id, event, dateFormat.format(creationDate));
    }
}
