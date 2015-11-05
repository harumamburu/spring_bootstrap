package com.mylab.spring.event;

import java.util.Date;
import java.util.Random;

public class Event {

    private static final Random RANDOMIZER = new Random();

    private final long id;
    private final Date creationDate;
    private String event;

    public Event(Date creationDate) {
        this.creationDate = creationDate;
        id = RANDOMIZER.nextInt(Integer.MAX_VALUE);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return String.format("Event # %d:%s occurred %F", id, event, creationDate);
    }
}
