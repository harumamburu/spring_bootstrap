package com.mylab.spring.bootstrap.logging.db;

import com.mylab.spring.bootstrap.event.Event;
import com.mylab.spring.bootstrap.event.EventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

public class DBEventsTableConnector {

    private final JdbcTemplate template;
    private final String createTableQuery;
    private final String insertEventQuery;
    private final String selectEventByIdQuery;

    @Value("${table.event.name:EVENTS}")
    private String eventsTableName;
    @Value("${table.event.date:CREATION_DATE}")
    private String eventDateColumn;
    @Value("${table.event.id:EVENT_ID}")
    private String eventIdColumn;
    @Value("${table.event.message:EVENT}")
    private String eventMessageColumn;
    @Value("${table.event.type:EVENT_TYPE}")
    private String eventTypeColumn;

    public DBEventsTableConnector(JdbcTemplate template, String createTableQuery,
                                  String insertEventQuery, String selectEventByIdQuery) {
        this.template = template;
        this.createTableQuery = createTableQuery;
        this.insertEventQuery = insertEventQuery;
        this.selectEventByIdQuery = selectEventByIdQuery;
    }

    public void insertEvent(Event event) {
        template.update(insertEventQuery, event.getId(), event.getCreationDate(),
                event.getStringEvent(), event.getType().name());
    }

    public Event selectEvent(long id) {
        return template.queryForObject(selectEventByIdQuery, new Object[]{id}, new RowMapper<Event>() {
            @Override
            public Event mapRow(ResultSet resultSet, int i) throws SQLException {
                Event result = new Event(new Date(resultSet.getTimestamp(eventDateColumn).getTime()),
                        DateFormat.getDateTimeInstance(), resultSet.getLong(eventIdColumn));
                result.setStringEvent(resultSet.getString(eventMessageColumn));
                result.setType(EventType.valueOf(resultSet.getString(eventTypeColumn)));
                return result;
            }
        });
    }

    @PostConstruct
    private void createDbTables() throws SQLException {
        // check if table already exists
        if (!template.getDataSource().getConnection().getMetaData().getTables(null, null, eventsTableName, null).next()) {
            template.execute(createTableQuery);
        }
    }
}
