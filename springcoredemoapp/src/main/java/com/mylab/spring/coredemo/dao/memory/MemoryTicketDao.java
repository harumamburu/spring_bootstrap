package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.TicketDao;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MemoryTicketDao extends AbstractMemoryDao<Ticket> implements TicketDao {

    private final static AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, Ticket> TICKETS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    protected Map<Long, Ticket> getStorage() {
        return TICKETS;
    }

    @Override
    protected AtomicLong getCounter() {
        return COUNTER;
    }

    @Override
    public List<Ticket> getAllEntities() {
        return TICKETS.values().parallelStream()
                .sorted(Comparator.comparing(Ticket::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getTicketsForEvent(Event event) {
        return  collectByPredicate(ticket -> ticket.getEvent().equals(event));
    }

    @Override
    public List<Ticket> getTicketsForUser(User user) {
        return  collectByPredicate(ticket -> ticket.getUser().equals(user));
    }

    private List<Ticket> collectByPredicate(Predicate<? super Ticket> predicate) {
        return TICKETS.values().parallelStream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    protected boolean isSavedAlready(Ticket entity) {
        // check if a ticket for a particular event and seat place was already saved
        return TICKETS.values().parallelStream().anyMatch(
                ticket -> ticket.getEvent().equals(entity.getEvent()) && ticket.getSeat() == entity.getSeat());
    }
}
