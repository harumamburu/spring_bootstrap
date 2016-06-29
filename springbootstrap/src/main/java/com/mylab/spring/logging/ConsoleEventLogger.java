package com.mylab.spring.logging;

import com.mylab.spring.event.Event;

public class ConsoleEventLogger implements EventLogger {

    public void logEvent(Event event) {
        System.out.println(event.toString());
    }
}
