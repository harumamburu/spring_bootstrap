package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.EventDao
        ;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Event;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
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
        // check if an event with such name on that particular date was already saved
        return EVENTS.values().parallelStream().anyMatch(e -> e.getName().equals(entity.getName()) &&
                e.getDate().equals(entity.getDate()));
    }

    @Override
    public Event getEventAtDate(Event event, Date date) throws DaoException {
        Predicate<Event> isNameEqual = e -> e.getName().equals(event.getName());
        Predicate<Event> isDateEqual = e -> LocalDate.from(e.getDate().toInstant().atZone(ZoneId.systemDefault()))
                .equals(LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault())));

        return EVENTS.values().parallelStream().filter(isNameEqual.and(isDateEqual))
                .findFirst()
                .orElseThrow(() ->new EntityNotFoundException(
                        String.format("No event with name %s at %s was found", event.getName(), date.toString())));
    }

    @Override
    public List<Event> getEventsAtDate(Event event, Date date) {
        int year = getLocalDatefromDate(date).getYear();
        int day = getLocalDatefromDate(date).getDayOfYear();
        return EVENTS.values().parallelStream().filter(e -> e.getName().equals(event.getName()) &&
                getLocalDatefromDate(e.getDate()).getYear() == year &&
                getLocalDatefromDate(e.getDate()).getDayOfYear() == day).collect(Collectors.toList());
    }

    private LocalDate getLocalDatefromDate(Date date) {
        return LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()));
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
