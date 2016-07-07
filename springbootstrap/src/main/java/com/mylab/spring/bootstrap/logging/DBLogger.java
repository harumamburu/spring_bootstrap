package com.mylab.spring.bootstrap.logging;

import com.mylab.spring.bootstrap.event.Event;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

public class DBLogger implements EventLogger {

    private final JdbcTemplate template;
    private final String createTableQuery;
    private final String insertEventQuery;

    public DBLogger(JdbcTemplate template, String createTableQuery, String insertEventQuery) {
        this.template = template;
        this.createTableQuery = createTableQuery;
        this.insertEventQuery = insertEventQuery;
    }

    @Override
    public void logEvent(Event event) {
        template.update(insertEventQuery, event.getId(), event.getCreationDate(),
                event.getStringEvent(), event.getType().name());
    }

    @PostConstruct
    private void createDbTables() throws SQLException{
        // get CREATE TABLE <NAME> statement, split by spaces and get <NAME>
        String tableName = createTableQuery.split("(?= ?\\()")[0].split("\\s")[2];
        // check if table already exists
        if (!template.getDataSource().getConnection().getMetaData().getTables(null, null, tableName, null).next()) {
            template.execute(createTableQuery);
        }
    }
}
