package com.mylab.spring.coredemo.test.service;

import com.mylab.spring.coredemo.dao.BookingDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Booking;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.service.UserService;
import com.mylab.spring.coredemo.test.util.EntryPoint;
import com.mylab.spring.coredemo.test.util.EntryPointConfiguration;
import com.mylab.spring.coredemo.test.util.EntryPointMethodInvocationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// unable to mark as @Test since entryPointTailingConfiguration
// will be counted as s test then
@Listeners(EntryPointMethodInvocationListener.class)
public class UserServiceTest extends AbstractServiceTest<UserService> implements EntryPointConfiguration {

    @Resource(name = "testUser")
    private User user;
    @Autowired
    private List<Booking> bookings;
    @Autowired
    private BookingDao bookingDao;

    @Override
    @Autowired
    protected void setService(UserService service) {
        this.service = service;
    }


    @Test(groups = "usersServiceTest")
    @EntryPoint
    public void registerUser() throws DaoException {
        user = service.register(user);
        assertSaving(user);
    }

    @Override
    public void entryPointTailingConfiguration() {
        // set user ID acquired after registration to mock bookings belonging to that user
        bookings.parallelStream()
                .filter(booking -> booking.getUser().getName().equals(user.getName()) &&
                        booking.getUser().getEmail().equals(user.getEmail()))
                .forEach(booking -> booking.getUser().setId(user.getId()));
        // persist all bookings
        bookings.parallelStream().forEach(booking -> {
                    try {
                        bookingDao.saveEntity(booking);
                    } catch (DaoException e) {
                        logger.error("Failed to save booking", e);
                    }
                });
    }

    @Test(groups = "usersServiceTest", priority = 1, dependsOnMethods = "registerUser")
    public void getUserById() throws DaoException {
        assertEqualToUser(service.getById(user.getId()));
    }

    @Test(groups = "usersServiceTest", priority = 1, dependsOnMethods = "registerUser")
    public void getUserByName() throws DaoException {
        assertEqualToUser(service.getByName(user.getName()));
    }

    @Test(groups = "usersServiceTest", priority = 1, dependsOnMethods = "registerUser")
    public void getUserByEmail() throws DaoException {
        assertEqualToUser(service.getByEmail(user.getEmail()));
    }
    
    private void assertEqualToUser(User userToAssert) {
        Assert.assertEquals(userToAssert, user, "Not same user was return");
    }

    @Test(groups = "usersServiceTest", priority = 1, dependsOnMethods = "registerUser")
    public void getBookedTicketsForUser() throws DaoException {
        Assert.assertEquals(service.getBookedTickets(user),
                bookings.parallelStream().filter(booking ->booking.getUser().equals(user))
                        .map(booking -> booking.getTicket())
                        .collect(Collectors.toList()),
                "Not all booked tickets were returned");
    }

    @Test(groups = "usersServiceTest", priority = 2, dependsOnMethods = "registerUser")
    public void removeUserTest() throws DaoException {
        Assert.assertEquals(service.remove(user), user);
    }

    @Test(groups = "usersServiceTest", priority = 3, dependsOnMethods = "removeUserTest")
    public void getBookingsForRemovedUser() throws DaoException {
        Assert.assertEquals(service.getBookedTickets(user), new ArrayList<Ticket>(0));
    }


    @AfterClass(alwaysRun = true)
    private void deleteAllBookings() {
        bookings.parallelStream().filter(booking -> !booking.getUser().equals(user)).forEach(booking -> {
            try {
                bookingDao.removeEntity(booking);
            } catch (DaoException e) {
                logger.error("Failed to remove booking", e);
            }
        });
    }
}
