package com.mylab.spring.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Component("event")
@Scope("prototype")
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
}
