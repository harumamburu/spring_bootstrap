package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.EventDao
        ;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Event;

import java.util.Comparator;
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
        // check if an event with such name was already saved
        return EVENTS.values().parallelStream().anyMatch(event -> event.getName().equals(entity.getName()));
    }

    @Override
    public List<Event> getEventsInRange(Date from, Date to) throws IllegalDaoRequestException {
        if (from.after(to) || from.equals(to)) {
            throw new IllegalDaoRequestException("Requested events' dates range is inconsistent");
        }
        return EVENTS.values().parallelStream().filter(event -> event.getDate().after(from))
                .filter(event -> event.getDate().before(to))
                .sorted(Comparator.comparing(Event::getId)).collect(Collectors.toList());
    }

    @Override
    public List<Event> getAllEntities() {
        return EVENTS.values().parallelStream()
                .sorted(Comparator.comparing(Event::getId))
                .collect(Collectors.toList());
    }
}
