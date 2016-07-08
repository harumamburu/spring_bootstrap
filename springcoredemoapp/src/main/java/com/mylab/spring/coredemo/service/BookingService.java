package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Booking;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.DoubleStream;

public class BookingService extends AbstractService {

    /*getTicketPrice(event, date, time, seats, user) - returns price for ticket for specified event on specific date and time for specified seats.
    bookTicket(user, ticket) - user could  be registered or not. If user is registered, then booking information is stored for that user. Purchased tickets for particular event should be stored
    getTicketsForEvent(event, date) - get all purchased tickets for event for specific date*/

    @Value("${booking.vip.coefficient:1.5}")
    private Double vipCoeff;

    public void bookTicket(User user, Ticket ticket) throws DaoException {
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

        if (confirmedUser != null) {
            bookingDao.saveEntity(new Booking(ticket, user));
        }

        ticketDao.saveEntity(ticket);
    }

    public double getTicketPrice(Event event, String date, String time, List<Integer> seats, User user) throws DaoException {
        Event confirmedEvent = confirmEventRegistration(event);

        if (!DateTimeFormatter.ofPattern("H:m").format(event.getDate().toInstant().atZone(ZoneId.systemDefault())).equals(time)) {
            throw new EntityNotFoundException("Event's description and provided times don't match");
        }

        return ticketDao.getTicketsForEvent(event).parallelStream().filter(ticket -> user.equals(user)).mapToDouble(Ticket::getPrice).sum();
    }

    public List<Ticket> getTicketsForEventAt(Event event, String date) throws DaoException {
        Event confirmedEvent = confirmEventRegistration(event);

        if (LocalDate.from(confirmedEvent.getDate().toInstant().atZone(ZoneId.systemDefault()))
                .compareTo(LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/y"))) != 0) {
            throw new EntityNotFoundException("Event's description and provided dates don't match");
        }

        return ticketDao.getTicketsForEvent(confirmedEvent);
    }

    private Event confirmEventRegistration(Event event) throws DaoException {
        Event confirmedEvent;
        if (event.getId() != null) {
            confirmedEvent = eventDao.getEntityById(event.getId());
        } else {
            confirmedEvent = eventDao.getEntityByName(event.getName());
        }
        if (confirmedEvent == null) {
            throw new EntityNotFoundException("No such event registered");
        }
        return event;
    }

}
