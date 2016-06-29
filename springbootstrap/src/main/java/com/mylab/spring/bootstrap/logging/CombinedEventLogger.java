package com.mylab.spring.bootstrap.logging;

import com.mylab.spring.bootstrap.event.Event;

import java.util.List;

public class CombinedEventLogger implements EventLogger {

    private final List<EventLogger> loggers;

    public CombinedEventLogger(List<EventLogger> loggers) {
        this.loggers = loggers;
    }

    @Override
    public void logEvent(Event event) {
        loggers.forEach(logger->logger.logEvent(event));
    }
}
