package com.mylab.spring.event;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class Event {

    private static final Random RANDOMIZER = new Random();

    private final long id;
    private final Date creationDate;
    private final DateFormat dateFormat;
    private String event;

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

    @Override
    public String toString() {
        return String.format("Event #%d: %s occurred %s", id, event, dateFormat.format(creationDate));
    }
}
