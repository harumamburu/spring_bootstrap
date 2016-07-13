package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.BookingDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Booking;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.User;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MemoryBookingDao extends AbstractMemoryDao<Booking> implements BookingDao {

    private static final AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, Booking> BOOKINGS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    protected Map<Long, Booking> getStorage() {
        return BOOKINGS;
    }

    @Override
    protected AtomicLong getCounter() {
        return COUNTER;
    }

    @Override
    public List<Booking> getBookingsForUser(User user) throws DaoException {
        checkParameters(user == null);
        return getFilteredValues(forUser(user));
    }

    @Override
    public List<Booking> getBookingsForEvent(Event event) throws DaoException {
        checkParameters(event == null);
        return getFilteredValues(forEvent(event));
    }

    @Override
    public List<Booking> getBookingsForEventUser(Event event, User user) throws DaoException {
        checkParameters(event == null || user == null);
        return getFilteredValues(forEvent(event).and(forUser(user)));
    }

    @Override
    public List<Booking> getUserBookingsForEventAt(User user, Event event, Date date) throws DaoException {
        checkParameters(user == null || event == null || date == null);
        return getFilteredValues(forUser(user).and(forEvent(event)).and(atDate(date)));
    }

    private List<Booking> getFilteredValues(Predicate<Booking> filter) {
        return BOOKINGS.values().parallelStream().
                filter(filter)
                .sorted(Comparator.comparing(Booking::getId))
                .collect(Collectors.toList());
    }



    private Predicate<Booking> forEvent(Event event) {
        return booking -> booking.getTicket().getEvent().equals(event);
    }

    private Predicate<Booking> atDate(Date date) {
        return booking -> booking.getTicket().getEvent().getDate().equals(date);
    }

    private Predicate<Booking> forUser(User user) {
        return booking -> booking.getUser().equals(user);
    }

    @Override
    protected boolean isSavedAlready(Booking entity) {
        return BOOKINGS.values().parallelStream()
                .anyMatch(booking -> booking.getUser().equals(entity.getUser()) &&
                booking.getTicket().equals(entity.getTicket()));
    }

    private void checkParameters(boolean parameterCheck) throws IllegalDaoRequestException {
        if (parameterCheck) {
            throw new IllegalDaoRequestException("Parameters can't be null");
        }
    }
}
