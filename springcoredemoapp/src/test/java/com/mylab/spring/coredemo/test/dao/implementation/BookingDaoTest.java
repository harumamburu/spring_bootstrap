package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.BookingDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Booking;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.test.dao.AbstractDaoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Test(groups = "bookingDaoTest")
public class BookingDaoTest extends AbstractDaoTest<Booking, BookingDao> {

    @Resource(name = "bookingTestUser")
    private User user;
    @Autowired
    private List<Event> events;
    @Autowired
    private List<Booking> bookings;


    @Override
    @Autowired
    protected void setDao(BookingDao dao) {
        this.dao = dao;
    }

    @Override
    @Resource(name = "testBooking")
    protected void setEntity(Booking entity) {
        this.entity = entity;
    }

    @DataProvider(name = "bookingsPopulator")
    private Iterator<Object[]> populateBookings() {
        return new Iterator<Object[]>() {
            Iterator<Booking> internal = bookings.iterator();

            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Object[] next() {
                return new Object[]{ internal.next() };
            }
        };
    }


    @Test(dataProvider = "bookingsPopulator")
    protected void saveBooking(Booking booking) throws DaoException {
        entity = dao.saveEntity(booking);
        assertSaving(entity);
    }

    @Test(dependsOnMethods = "saveBooking", priority = 1)
    protected void getBookingById() throws DaoException {
        super.getEntityById();
    }

    @Test(dependsOnMethods = "saveBooking", priority = 1)
    public void getBookingsForUser() throws DaoException {
        assertListEqualsToFiltered(dao.getBookingsForUser(user),
                booking -> booking.getUser().equals(user));
    }

    @Test(dependsOnMethods = "saveBooking", priority = 1)
    public void getBookingsForEvent() throws DaoException {
        assertListEqualsToFiltered(dao.getBookingsForEvent(events.get(0)),
                booking -> booking.getTicket().getEvent().equals(events.get(0)));
    }

    @Test(dependsOnMethods = "saveBooking", priority = 1)
    public void getBookingsForEventAndUser() throws DaoException {
        assertListEqualsToFiltered(dao.getUserBookingsForEvent(events.get(0), user),
                booking -> booking.getUser().equals(user) && booking.getTicket().getEvent().equals(events.get(0)));
    }

    private void assertListEqualsToFiltered(List<Booking> toAssert, Predicate<Booking> filter) {
        Assert.assertEquals(toAssert, bookings.parallelStream().filter(filter).collect(Collectors.toList()),
                "Not all bookings were returned as expected");
    }

    @Test(dependsOnMethods = "saveBooking", priority = 1)
    public void getBookingsForNonExistingUser() throws DaoException {
        assertListEmpty(dao.getBookingsForUser(new User()));
    }

    @Test(dependsOnMethods = "saveBooking", priority = 1)
    public void getBookingsForNonExistingEvent() throws DaoException {
        assertListEmpty(dao.getBookingsForEvent(new Event()));
    }

    @Test(dependsOnMethods = "saveBooking", priority = 1)
    public void getBookingsForNonExistingEventAndUser() throws DaoException {
        assertListEmpty(dao.getUserBookingsForEvent(new Event(), new User()));
    }

    private void assertListEmpty(List<Booking> toAssert) {
        Assert.assertEquals(toAssert, new ArrayList<Booking>(0), "Returned list wasn't empty");
    }

    @Test(priority = 2, expectedExceptions = EntityNotFoundException.class)
    protected void getNonExistingBookingById() throws DaoException {
        super.getNonExistingEntityById();
    }

    @Test(priority = 2, expectedExceptions = IllegalDaoRequestException.class)
    public void getBookingsWithNullUser() throws DaoException {
        dao.getBookingsForUser(null);
    }

    @Test(priority = 2, expectedExceptions = IllegalDaoRequestException.class)
    public void getBookingsWithNullEvent() throws DaoException {
        dao.getBookingsForEvent(null);
    }

    @Test(priority = 2, expectedExceptions = IllegalDaoRequestException.class)
    public void getBookingsWithNullEventAndUser() throws DaoException {
        dao.getUserBookingsForEvent(null, null);
    }

    @Test(priority = 3, expectedExceptions = EntityNotFoundException.class)
    protected void deleteNonExistingBooking() throws DaoException {
        super.deleteNonExistingEntity();
    }

    @Test(priority = 3, expectedExceptions = DaoException.class)
    protected void deleteBookingWithNullId() throws DaoException {
        super.deleteEntityWithNullId();
    }

    @Test(priority = 4, dataProvider = "bookingsPopulator",
            dependsOnMethods = { "getBookingById", "getBookingsForUser",
                    "getBookingsForEvent", "getBookingsForEventAndUser" })
    protected void deleteBooking(Booking booking) throws DaoException {
        entity = booking;
        super.deleteEntity();
    }


    @Override
    protected Booking copyEntity(Booking entity) {
        Booking newBooking = new Booking(entity.getTicket(), entity.getUser());
        return newBooking;
    }
}
