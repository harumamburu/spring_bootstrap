package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.BulkDao;
import com.mylab.spring.coredemo.dao.EventDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Event;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryEventDao implements EventDao {

    private final static AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, Event> EVENTS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    public Event saveEntity(Event entity) throws DaoException {
        return null;
    }

    @Override
    public Event getEntityById(Long id) throws DaoException {
        return null;
    }

    @Override
    public Event removeEntity(Event entity) throws DaoException {
        return null;
    }

    @Override
    public Event getEntityByName(String name) throws DaoException {
        return null;
    }

    @Override
    public List<Event> getEventsInRange(Date from, Date to) {
        return null;
    }

    @Override
    public List<Event> getAllEntities() {
        return null;
    }

    @Override
    public List<Event> getEventsToDate(Date to) {
        return null;
    }
}
