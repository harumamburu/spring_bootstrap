package com.mylab.spring.logging;

import com.mylab.spring.event.Event;
import org.springframework.stereotype.Component;

@Component("consoleLogger")
public class ConsoleEventLogger implements EventLogger {

    public void logEvent(Event event) {
        System.out.println(event.toString());
    }
}
