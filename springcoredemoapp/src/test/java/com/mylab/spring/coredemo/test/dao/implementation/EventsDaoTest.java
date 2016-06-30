package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.EventDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.test.dao.BulkDaoTest;
import com.mylab.spring.coredemo.test.dao.NamingDaoTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @DataProvider(name = "eventsListPopulator")
    public Iterator<Object[]> populate() {
        return new Iterator<Object[]>() {
            Iterator<Event> internal = events.iterator();
            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Object[] next() {
                return new Object[]{internal.next()};
            }
        };
    }


    @Test(groups = "saveTests", dataProvider = "eventsListPopulator")
    public void saveEvent(Event event) throws DaoException {
        entity = dao.saveEntity(event);
        Assert.assertNotNull(entity, "Entity wasn't saved");
        Assert.assertNotNull(entity.getId(), "Id haven't been set");
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
        assertFilterPredicate(((EventDao) dao).getEventsInRange(from, to),
                event -> event.getDate().after(from) && event.getDate().before(to));
    }

    @Test(dependsOnMethods = "saveEvent",
            groups = {"gettersTests", "eventGettersTests", "bulkTests"},
            priority = 1)
    public void getEventsToDate() {
        assertFilterPredicate(((EventDao) dao).getEventsToDate(to), event -> event.getDate().after(new Date()) &&
                event.getDate().before(to));
    }

    private void assertFilterPredicate(List<Event> eventsToAssert, Predicate<? super Event> predicate) {
        Assert.assertEquals(eventsToAssert, events.parallelStream().filter(predicate).collect(Collectors.toList()),
                "Not all expected events were returned");
    }

    @Override
    @Test(dependsOnMethods = "saveEvent",
            groups = {"gettersTests", "eventGettersTests", "bulkTests"},
            priority = 1)
    public void getAllEntities() {
        Assert.assertEquals(((EventDao) dao).getAllEntities(), events, "Not same events were returned");
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
