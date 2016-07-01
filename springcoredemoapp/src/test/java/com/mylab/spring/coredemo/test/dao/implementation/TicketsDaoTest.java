package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.test.dao.AbstractDaoTest;
import com.mylab.spring.coredemo.test.dao.BulkDaoTest;
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

public class TicketsDaoTest extends AbstractDaoTest<Ticket, TicketDao> implements BulkDaoTest<Ticket, TicketDao> {

    @Autowired
    private List<Ticket> tickets;
    @Autowired
    private Event event;
    @Autowired
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
    public void saveTicket() throws DaoException {
        saveEntity();
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
        assertEqualListAndFilteredTickets(((TicketDao) dao).getTicketsByEvent(event),
                ticket -> ticket.getEvent().equals(event),
                "Returned tickets' events don't match");
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"gettersTests", "ticketGettersTests", "bulkTests"},
            priority = 1)
    public void getTicketsForUser() {
        assertEqualListAndFilteredTickets(((TicketDao) dao).getTicketsByUser(user),
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
            groups = {"negativeTests", "gettersTests", "ticketGettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingTicketById() throws DaoException {
        getNonExistingEntityById();
    }

    @Test(dependsOnMethods = "saveTicket",
            groups = {"deletingTests", "ticketDeletingTests"},
            priority = 3)
    public void deleteNonExistingTicket() throws DaoException {
        deleteNonExistingEntity();
    }
    
    @Test(dependsOnMethods = "saveTicket",
            groups = {"deletingTests", "ticketDeletingTests", "negativeTests"},
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
        Ticket newTicket = new Ticket(ticket.getEvent(), ticket.getPrice(), ticket.getSeat());
        newTicket.setUser(ticket.getUser());
        return newTicket;
    }
}
