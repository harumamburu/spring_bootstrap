package com.mylab.spring.logging;

import com.mylab.spring.event.Event;

public interface EventLogger {

    void logEvent(Event event);
}
