package com.mylab.spring.bootstrap.logging;

import com.mylab.spring.bootstrap.event.Event;

public class ConsoleEventLogger implements EventLogger {

    public void logEvent(Event event) {
        System.out.println(event.toString());
    }
}
