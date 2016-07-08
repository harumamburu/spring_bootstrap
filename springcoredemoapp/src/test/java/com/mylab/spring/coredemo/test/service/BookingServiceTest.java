package com.mylab.spring.coredemo.test.service;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

@Test(groups = "bookingServiceTest")
public class BookingServiceTest extends AbstractServiceTest<BookingService> {

    @Resource(name = "testUser")
    private User user;
    @Resource(name = "testingEvent")
    private Event event;
    @Autowired
    private List<Integer> seats;

    @Value("test.event.date")
    private String date;
    @Value("test.event.time")
    private String time;
    @Value("test.event.price")
    private Double price;

    @DataProvider(name = "seatsProvider")
    private Iterator<Object[]> provideSeats() {
        return new Iterator<Object[]>() {
            Iterator<Integer> internal;
            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Object[] next() {
                return new Object[] { internal.next() };
            }
        };
    }

    @Override
    @Autowired
    protected void setService(BookingService service) {
        this.service = service;
    }


    @Test(dataProvider = "seatsProvider")
    public void bookTicket(Integer seat) throws DaoException {
        service.bookTicket(user, new Ticket(event, seat));
    }

    @Test(priority = 1)
    public void getTicketPrice() {
        Assert.assertEquals(service.getTicketPrice(event, date, time, seats, user), price, "event's Price doesn't match");
    }

    @Test(priority = 1)
    public void getTickets() throws DaoException {
        service.getTicketsForEventAt(event, date);
    }

    @Test(priority = 1)
    public void bookTicketForNonExistingUser() throws DaoException {
        service.bookTicket(new User(), new Ticket(event, seats.get(0)));
    }

    @Test(priority = 1, expectedExceptions = EntityNotFoundException.class)
    public void getTicketsEventAndDateDontMatch() throws DaoException {
        service.getTicketsForEventAt(event, DateTimeFormatter.ofPattern("M/e/y").format(LocalDate.now()));
    }

    @Test(priority = 1, expectedExceptions = EntityNotFoundException.class)
    public void getTicketsForNonExistingEvent() throws DaoException {
        service.getTicketsForEventAt(new Event(), date);
    }

    @Test(priority = 1, expectedExceptions = IllegalDaoRequestException.class)
    public void bookTicketForSeatOutOfAuditoriumsRange() throws DaoException {
        service.bookTicket(user, new Ticket(event, event.getAuditorium().getNumberOfSeats() + 1));
    }

}
