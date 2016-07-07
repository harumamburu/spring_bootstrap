package com.mylab.spring.bootstrap.logging.db;

import com.mylab.spring.bootstrap.event.Event;
import com.mylab.spring.bootstrap.logging.EventLogger;

public class DBLogger implements EventLogger {

    private final DBEventsTableConnector connector;

    public DBLogger(DBEventsTableConnector connector) {
        this.connector = connector;
    }

    @Override
    public void logEvent(Event event) {
        connector.insertEvent(event);
    }

}
