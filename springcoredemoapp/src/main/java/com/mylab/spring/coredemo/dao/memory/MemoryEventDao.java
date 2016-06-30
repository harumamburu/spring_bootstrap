package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.EventDao
        ;
import com.mylab.spring.coredemo.entity.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MemoryEventDao extends AbstractNamingMemoryDao<Event> implements EventDao {

    private final static AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, Event> EVENTS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    protected Map<Long, Event> getStorage() {
        return EVENTS;
    }

    @Override
    protected AtomicLong getCounter() {
        return COUNTER;
    }

    @Override
    protected boolean isSavedAlready(Event entity) {
        return EVENTS.values().parallelStream().anyMatch(event -> event.getName().equals(entity.getName()));
    }

    @Override
    public List<Event> getEventsInRange(Date from, Date to) {
        return EVENTS.values().parallelStream().filter(event -> event.getDate().after(from))
                .filter(event -> event.getDate().before(to)).collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsToDate(Date to) {
        return getEventsInRange(new Date(), to);
    }

    @Override
    public List<Event> getAllEntities() {
        return new ArrayList<>(EVENTS.values());
    }
}
