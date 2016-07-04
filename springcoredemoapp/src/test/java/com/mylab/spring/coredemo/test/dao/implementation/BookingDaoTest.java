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

public class BookingDaoTest extends AbstractDaoTest<Booking, BookingDao> {

    @Resource(name = "ticketTestUser")
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
        Iterator<Booking> internal = bookings.iterator();

        return new Iterator<Object[]>() {
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


    @Test(groups = {"saveTests", "bookingSaveTests"}, dataProvider = "bookingsPopulator")
    protected void saveBooking(Booking booking) throws DaoException {
        entity = dao.saveEntity(entity);
        Assert.assertNotNull(entity, "Entity wasn't saved");
        Assert.assertNotNull(entity.getId(), "Id haven't been set");
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"gettersTests", "bookingGettersTests"},
            priority = 1)
    protected void getBookingById() throws DaoException {
        super.getEntityById();
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"gettersTests", "bookingGettersTests"},
            priority = 1)
    public void getBookingsForUser() throws DaoException {
        assertListEqualsToFiltered(dao.getBookingsForUser(user),
                booking -> booking.getUser().equals(user));
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"gettersTests", "bookingGettersTests"},
            priority = 1)
    public void getBookingsForEvent() throws DaoException {
        assertListEqualsToFiltered(dao.getBookingsForEvent(events.get(0)),
                booking -> booking.getEvent().equals(events.get(0)));
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"gettersTests", "bookingGettersTests"},
            priority = 1)
    public void getBookingsForEventAndUser() throws DaoException {
        assertListEqualsToFiltered(dao.getBookingsForEventUser(events.get(0), user),
                booking -> booking.getUser().equals(user) && booking.getEvent().equals(events.get(0)));
    }

    private void assertListEqualsToFiltered(List<Booking> toAssert, Predicate<Booking> filter) {
        Assert.assertEquals(toAssert, bookings.parallelStream().filter(filter).collect(Collectors.toList()),
                "Not all bookings were returned as expected");
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"gettersTests", "bookingGettersTests"},
            priority = 1)
    public void getBookingsForNonExistingUser() throws DaoException {
        assertListEmpty(dao.getBookingsForUser(new User()));
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"gettersTests", "bookingGettersTests"},
            priority = 1)
    public void getBookingsForNonExistingEvent() throws DaoException {
        assertListEmpty(dao.getBookingsForEvent(new Event()));
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"gettersTests", "bookingGettersTests"},
            priority = 1)
    public void getBookingsForNonExistingEventAndUser() throws DaoException {
        assertListEmpty(dao.getBookingsForEventUser(new Event(), new User()));
    }

    private void assertListEmpty(List<Booking> toAssert) {
        Assert.assertEquals(toAssert, new ArrayList<Booking>(0), "Returned list wasn't empty");
    }

    @Test(groups = {"negativeTests", "gettersTests", "bookingGettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    protected void getNonExistingBookingById() throws DaoException {
        super.getNonExistingEntityById();
    }

    @Test(groups = {"negativeTests", "gettersTests", "bookingGettersTests"},
            priority = 2,
            expectedExceptions = IllegalDaoRequestException.class)
    public void getBookingsWithNullUser() throws DaoException {
        dao.getBookingsForUser(null);
    }

    @Test(groups = {"negativeTests", "gettersTests", "bookingGettersTests"},
            priority = 2,
            expectedExceptions = IllegalDaoRequestException.class)
    public void getBookingsWithNullEvent() throws DaoException {
        dao.getBookingsForEvent(null);
    }

    @Test(groups = {"negativeTests", "gettersTests", "bookingGettersTests"},
            priority = 2,
            expectedExceptions = IllegalDaoRequestException.class)
    public void getBookingsWithNullEventAndUser() throws DaoException {
        dao.getBookingsForEventUser(null, null);
    }

    @Test(groups = {"deletingTests", "bookingDeletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = EntityNotFoundException.class)
    protected void deleteNonExistingBooking() throws DaoException {
        super.deleteNonExistingEntity();
    }

    @Test(groups = {"deletingTests", "bookingDeletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = DaoException.class)
    protected void deleteBookingWithNullId() throws DaoException {
        super.deleteEntityWithNullId();
    }

    @Test(dependsOnMethods = "saveBooking",
            groups = {"deletingTests", "bookingDeletingTests"},
            priority = 4)
    protected void deleteBooking() throws DaoException {
        super.deleteEntity();
    }

    @Override
    protected Booking copyEntity(Booking entity) {
        Booking newBooking = new Booking(entity.getTickets());
        return newBooking;
    }
}
