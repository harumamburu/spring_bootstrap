package com.mylab.spring.logging;

import com.mylab.spring.event.Event;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("combinedLogger")
public class CombinedEventLogger implements EventLogger {

    private List<EventLogger> loggers;

    public CombinedEventLogger(List<EventLogger> loggers) {
        this.loggers = loggers;
    }

    @Override
    public void logEvent(Event event) {
        loggers.forEach(logger->logger.logEvent(event));
    }
}
