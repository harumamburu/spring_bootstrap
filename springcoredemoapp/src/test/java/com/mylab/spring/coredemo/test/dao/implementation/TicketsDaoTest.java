package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.TicketDao;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.test.dao.AbstractDaoTest;
import com.mylab.spring.coredemo.test.dao.BulkDaoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TicketsDaoTest extends AbstractDaoTest<Ticket, TicketDao> implements BulkDaoTest<Ticket, TicketDao> {

    @Autowired
    private List<Ticket> tickets;
    @Resource(name = "ticketTestEvent")
    private Event event;
    @Resource(name = "ticketTestUser")
    private User user;

    @DataProvider(name = "ticketsPopulator")
    private Iterator<Object[]> populateTickets() {
        Iterator<Ticket> ticketIterator = tickets.iterator();
        Iterator<Object[]> populator = new Iterator<Object[]>() {
            @Override
            public boolean hasNext() {
                return ticketIterator.hasNext();
            }

            @Override
            public Object[] next() {
                return new Object[]{ ticketIterator.next() };
            }
        };
        return populator;
    }

    @Override
    @Autowired
    protected void setDao(TicketDao ticketDao) {
        dao = ticketDao;
    }

    @Override
    @Resource(name = "testTicket")
    protected void setEntity(Ticket ticket) {
        entity = ticket;
    }


    @Test(groups = {"saveTests", "ticketsSaveTests"}, dataProvider = "ticketsPopulator")
    public void saveTicket(Ticket ticket) throws DaoException {
        entity = dao.saveEntity(ticket);
        Assert.assertNotNull(entity, "Entity wasn't saved");
        Assert.assertNotNull(entity.getId(), "Id haven't been set");
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"gettersTests", "ticketGettersTests"},
            priority = 1)
    public void getTicketById() throws DaoException {
        getEntityById();
    }

    @Override
    @Test(dependsOnMethods = "saveTicket",
            groups = {"gettersTests", "ticketGettersTests", "bulkTests"},
            priority = 1)
    public void getAllEntities() {
        Assert.assertEquals(((TicketDao) dao).getAllEntities(), tickets,
                "Returned tickets lists isn't equal to expected");
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"gettersTests", "ticketGettersTests", "bulkTests"},
            priority = 1)
    public void getTicketsForEvent() {
        assertEqualListAndFilteredTickets(((TicketDao) dao).getTicketsForEvent(event),
                ticket -> ticket.getEvent().equals(event),
                "Returned tickets' events don't match");
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"gettersTests", "ticketGettersTests", "bulkTests"},
            priority = 1)
    public void getTicketsForUser() {
        assertEqualListAndFilteredTickets(((TicketDao) dao).getTicketsForUser(user),
                ticket -> ticket.getUser().equals(user),
                "Returned tickets' users don't match");
    }

    private void assertEqualListAndFilteredTickets(List<Ticket> ticketsToAssert,
                                                   Predicate<? super Ticket> filterPredicate,
                                                   String message) {
        Assert.assertEquals(ticketsToAssert,
                tickets.parallelStream().filter(filterPredicate).collect(Collectors.toList()), message);
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"gettersTests", "ticketGettersTests", "bulkTests"},
            priority = 1)
    public void getTicketsForNonExistingUser() {
        User newUser = new User("new_" + user.getName(), "new_" + user.getEmail());
        assertEqualListAndFilteredTickets(((TicketDao) dao).getTicketsForUser(newUser),
                ticket -> ticket.getUser().equals(newUser),
                "There shouldn't be tickets for fake user");
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"gettersTests", "ticketGettersTests", "bulkTests"},
            priority = 1)
    public void getTicketsForNonExistingEvent() {
        Event newEvent = new Event("new_" + event.getName(), event.getDate(), event.getBasePrice() + 100500);
        assertEqualListAndFilteredTickets(((TicketDao) dao).getTicketsForEvent(newEvent),
                ticket -> ticket.getEvent().equals(newEvent),
                "There shouldn't be tickets for fake event");
    }

    @Test(groups = {"negativeTests", "gettersTests", "ticketGettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingTicketById() throws DaoException {
        getNonExistingEntityById();
    }

    @Test(groups = {"deletingTests", "ticketDeletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = EntityNotFoundException.class)
    public void deleteNonExistingTicket() throws DaoException {
        deleteNonExistingEntity();
    }
    
    @Test(groups = {"deletingTests", "ticketDeletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = DaoException.class)
    public void deleteTicketWithNullId() throws DaoException {
        deleteEntityWithNullId();
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"deletingTests", "ticketDeletingTests"},
            priority = 4)
    public void deleteTicket() throws DaoException {
        deleteEntity();
    }

    @Override
    protected Ticket copyEntity(Ticket ticket) {
        Ticket newTicket = new Ticket(ticket.getEvent(), ticket.getUser(), ticket.getSeat());
        return newTicket;
    }
}
