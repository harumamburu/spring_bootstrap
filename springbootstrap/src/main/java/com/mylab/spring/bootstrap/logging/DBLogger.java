package com.mylab.spring.bootstrap.logging;

import com.mylab.spring.bootstrap.event.Event;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;

public class DBLogger implements EventLogger {

    private final JdbcTemplate template;

    public DBLogger(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void logEvent(Event event) {

    }

    @PostConstruct
    private void createDbTables() {

    }
}
