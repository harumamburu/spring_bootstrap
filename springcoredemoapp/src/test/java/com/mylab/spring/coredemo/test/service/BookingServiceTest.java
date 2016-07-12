package com.mylab.spring.coredemo.test.service;

import com.mylab.spring.coredemo.dao.AuditoriumDao;
import com.mylab.spring.coredemo.dao.EventDao;
import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Booking;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private UserDao userDao;
    @Autowired
    private AuditoriumDao auditoriumDao;
    @Autowired
    private EventDao eventDao;

    @Value("#{'${test.bookingserv.ticket.seats}'.split(',')}")
    private List<Integer> seats;
    @Value("${test.bookingserv.dateformat}")
    private String dateFormat;
    @Value("${test.bookingserv.timeformat}")
    private String timeFormat;
    private String date;
    private String time;
    private double price = 0d;

    @DataProvider(name = "seatsProvider")
    private Iterator<Object[]> provideSeats() {
        return new Iterator<Object[]>() {
            Iterator<Integer> internal = seats.iterator();
            // skip one seat to be able to check booking fof non-existing user
            {
                if (internal.hasNext()) {
                    internal.next();
                }
            }
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

    @BeforeClass
    private void setDateTimeStrings() {
        date = DateTimeFormatter.ofPattern(dateFormat).format(event.getDate().toInstant().atZone(ZoneId.systemDefault()));
        time = DateTimeFormatter.ofPattern(timeFormat).format(event.getDate().toInstant().atZone(ZoneId.systemDefault()));
    }

    @BeforeClass
    private void persistDependencies() throws DaoException {
        auditoriumDao.saveEntity(event.getAuditorium());
        userDao.saveEntity(user);
        eventDao.saveEntity(event);
    }


    @Test(dataProvider = "seatsProvider")
    public void bookTicket(Integer seat) throws DaoException {
        Booking booking = service.bookTicket(user, new Ticket(event, seat));
        price += booking.getTicket().getPrice();
        Assert.assertNotNull(booking, "Booking wasn't saved");
        Assert.assertNotNull(booking.getId(), "Booking id wasn't set");
        Assert.assertEquals(booking.getUser(), user);
        Assert.assertEquals(booking.getTicket().getEvent(), event, "Booking's event is incorrect");
        Assert.assertEquals(booking.getTicket().getSeat(), seat, "Booking's seat is incorrect");
    }

    @Test(priority = 1)
    public void getTicketPrice() throws DaoException {
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
        service.getTicketsForEventAt(event, DateTimeFormatter.ofPattern(dateFormat).format(LocalDate.now()));
    }

    @Test(priority = 1, expectedExceptions = EntityNotFoundException.class)
    public void getTicketsForNonExistingEvent() throws DaoException {
        service.getTicketsForEventAt(new Event(), date);
    }

    @Test(priority = 1, expectedExceptions = IllegalDaoRequestException.class)
    public void bookTicketForSeatOutOfAuditoriumsRange() throws DaoException {
        service.bookTicket(user, new Ticket(event, event.getAuditorium().getNumberOfSeats() + 1));
    }

    @Test(priority = 2, expectedExceptions = EntityAlreadyExistsException.class)
    public void bookAlreadyBookedTicket() throws DaoException {
        service.bookTicket(user, new Ticket(event, seats.get(1)));
    }

}
