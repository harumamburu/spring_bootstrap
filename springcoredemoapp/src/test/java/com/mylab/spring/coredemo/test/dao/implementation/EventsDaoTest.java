package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.EventDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.test.dao.BulkDaoTest;
import com.mylab.spring.coredemo.test.dao.NamingDaoTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class EventsDaoTest extends NamingDaoTest<Event, EventDao> implements BulkDaoTest<Event, EventDao> {

    @Resource(name = "eventsList")
    private List<Event> events;
    @Resource(name = "fromDate")
    private Date from;
    @Resource(name = "toDate")
    private Date to;

    @Override
    @Resource(name = "eventBean")
    protected void setEntity(Event event) {
        entity = event;
    }

    @Override
    @Resource(name = "eventMemoryDao")
    protected void setDao(EventDao eventDao) {
        dao = eventDao;
    }


    @Test(groups = "saveTests")
    public void saveEvent() throws DaoException {
        saveEntity();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"gettersTests", "eventGettersTests"},
            priority = 1)
    public void getEventById() throws DaoException {
        getEntityById();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"gettersTests", "eventGettersTests"},
            priority = 1)
    public void getEventByName() throws DaoException {
        getEntityByName();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"gettersTests", "eventGettersTests", "bulkTests"},
            priority = 1)
    public void getEventsInRange() {
        assertAllMatchPredicate(((EventDao) dao).getEventsInRange(from, to),
                event -> event.getDate().after(from) && event.getDate().before(to));
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"gettersTests", "eventGettersTests", "bulkTests"},
            priority = 1)
    public void getEventsToDate() {
        assertAllMatchPredicate(((EventDao) dao).getEventsToDate(to), event -> event.getDate().before(to));
    }

    private void assertAllMatchPredicate(List<Event> eventsToAssert, Predicate<? super Event> predicate) {
        Assert.assertTrue(eventsToAssert.parallelStream().allMatch(predicate),
                "Not all expected events were returned");
    }

    @Override
    @Test(dependsOnMethods = "saveEvent",
            groups = {"gettersTests", "eventGettersTests", "bulkTests"},
            priority = 1)
    public void getAllEntities() {
        Assert.assertEquals(((EventDao) dao).getAllEntities(), Collections.singletonList(entity), "Not same events were returned");
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"negativeTests", "saveTests", "eventSaveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void saveEventWithUniqueNameViolated() throws DaoException {
        saveEntityWithUniqueNameViolated();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"negativeTests", "gettersTests", "eventGettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingEventById() throws DaoException {
        getNonExistingEntityById();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"negativeTests", "gettersTests", "eventGettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingEventByName() throws DaoException {
        getNonExistingEntityByName();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"deletingTests", "eventDeletingTests", "negativeTests"},
            priority = 3)
    public void deleteNonExistingEvent() throws DaoException {
        deleteNonExistingEntity();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"deletingTests", "eventDeletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = DaoException.class)
    public void deleteEventWithNullId() throws DaoException {
        deleteEntityWithNullId();
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"deletingTests", "eventDeletingTests"},
            priority = 4)
    public void deleteEvent() throws DaoException {
        deleteEntity();
    }

    @Override
    protected Event copyEntity(Event event) {
        Event newEvent = new Event(event.getName(), event.getDate(), event.getBasePrice());
        newEvent.setRating(event.getRating());
        return newEvent;
    }
}
