package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Booking;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookingService extends AbstractService {

    /*getTicketPrice(event, date, time, seats, user) - returns price for ticket for specified event on specific date and time for specified seats.
    bookTicket(user, ticket) - user could  be registered or not. If user is registered, then booking information is stored for that user. Purchased tickets for particular event should be stored
    getTicketsForEvent(event, date) - get all purchased tickets for event for specific date*/

    @Value("${booking.vip.coefficient:1.5}")
    private Double vipCoeff;
    @Value("${test.bookingserv.dateformat}")
    private String dateFormat;
    @Value("${test.bookingserv.timeformat}")
    private String timeFormat;


    public Booking bookTicket(User user, Ticket ticket) throws DaoException {
        if (auditoriumDao.getNumberOfSeats(ticket.getEvent().getAuditorium().getId()) < ticket.getSeat()) {
            throw new IllegalDaoRequestException("No such seat available");
        }

        if (auditoriumDao.getVipSeats(ticket.getEvent().getAuditorium().getId()).contains(ticket.getSeat())) {
            ticket.setPrice(ticket.getPrice() * vipCoeff);
        }

        User confirmedUser = null;
        try {
            if (user.getId() != null) {
                confirmedUser = userDao.getEntityById(user.getId());
            } else {
                confirmedUser = userDao.getUserByEmail(user.getEmail());
            }
        } catch (DaoException exc){}


        ticket = ticketDao.saveEntity(ticket);
        Booking booking = new Booking(ticket, user);

        if (confirmedUser != null) {
            booking = bookingDao.saveEntity(booking);
        }
        return booking;
    }

    public double getTicketPrice(Event event, String date, String time, List<Integer> seats, User user) throws DaoException {
        Date eventDate = parseDate(date + " " + time, dateFormat + " " + timeFormat);
        return bookingDao.getUserBookingsForEvent(eventDao.getEventAtDate(event, eventDate), user).parallelStream()
                .map(Booking::getTicket)
                .filter(ticket -> seats.contains(ticket.getSeat()))
                .mapToDouble(Ticket::getPrice).sum();
    }

    private Date parseDate(String date, String format) throws IllegalDaoRequestException {
        try {
            return Date.from(new SimpleDateFormat(format).parse(date).toInstant());
        } catch (ParseException e) {
            throw new IllegalDaoRequestException("Failed to parse provided date");
        }
    }

    public List<Ticket> getTicketsForEventAt(Event event, String date) throws DaoException {
        Date eventDate = parseDate(date, dateFormat);
        List<Event> events = eventDao.getEventsAtDate(event, eventDate);
        if (events.isEmpty()) {
            throw new EntityNotFoundException("No such events at specified date found");
        }
        return events.parallelStream().flatMap(e -> ticketDao.getTicketsForEvent(e).stream())
                .collect(Collectors.toList());
    }
}
