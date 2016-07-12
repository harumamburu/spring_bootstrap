package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.EventDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.dao.exception.IllegalDaoRequestException;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.test.dao.BulkDaoTest;
import com.mylab.spring.coredemo.test.dao.NamingDaoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Test(groups = "eventsDaoTest")
public class EventsDaoTest extends NamingDaoTest<Event, EventDao> implements BulkDaoTest<Event, EventDao> {

    @Autowired
    private List<Event> events;
    @Resource(name = "fromDate")
    private Date from;
    @Resource(name = "toDate")
    private Date to;
    private Date eventDatePlusSixH;
    private Event shiftedEvent;

    @Override
    @Resource(name = "testingEvent")
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

    @BeforeClass
    public void shiftEventDate() {
        eventDatePlusSixH = Date.from(entity.getDate().toInstant().atZone(ZoneId.systemDefault()).plusHours(6).toInstant());
        shiftedEvent = copyEntity(entity);
        shiftedEvent.setDate(eventDatePlusSixH);
    }


    @Test(dataProvider = "eventsListPopulator")
    public void saveEvent(Event event) throws DaoException {
        entity = dao.saveEntity(event);
        assertSaving(entity);
    }

    @Test
    public void saveEventWithSameNameOtherDates() throws DaoException {
        assertSaving(dao.saveEntity(shiftedEvent));
        events.add(shiftedEvent);
    }

    @Test(dependsOnMethods = "saveEvent", priority = 1)
    public void getEventById() throws DaoException {
        getEntityById();
    }

    @Test(dependsOnMethods = "saveEvent", priority = 1)
    public void getEventByName() throws DaoException {
        getEntityByName();
    }

    @Test(dependsOnMethods = "saveEventWithSameNameOtherDates", priority = 1)
    public void getParticularEventAtDate() throws DaoException {
        Assert.assertEquals(dao.getEventAtDate(shiftedEvent, eventDatePlusSixH), shiftedEvent);
    }

    @Test(dependsOnMethods = "saveEvent", priority = 1)
    public void getEventsInRange() throws DaoException {
        assertEqualListAndFilteredEvents(dao.getEventsInRange(from, to),
                event -> event.getDate().after(from) && event.getDate().before(to));
    }

    @Test(dependsOnMethods = "saveEvent", priority = 1)
    public void getEventsToDate() throws DaoException {
        assertEqualListAndFilteredEvents(dao.getEventsInRange(new Date(), to),
                event -> event.getDate().after(new Date()) && event.getDate().before(to));
    }

    @Test(priority = 1)
    public void getEventsInOldRange() throws DaoException {
        Date oldFrom = substractWeeks(from, 4);
        Date oldTo = substractWeeks(to, 4);
        Assert.assertEquals(dao.getEventsInRange(oldFrom, oldTo), new ArrayList<Event>(0));
    }

    private Date substractWeeks(Date date, int weeks) {
        return Date.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .minusWeeks(weeks).toInstant(ZoneOffset.ofHours(0)));
    }

    private void assertEqualListAndFilteredEvents(List<Event> eventsToAssert, Predicate<? super Event> filterPredicate) {
        Assert.assertEquals(eventsToAssert, events.parallelStream().filter(filterPredicate).collect(Collectors.toList()),
                "Not all expected events were returned");
    }

    @Override
    @Test(dependsOnMethods = "saveEvent", priority = 1)
    public void getAllEntities() {
        Assert.assertEquals(dao.getAllEntities(), events, "Not same events were returned");
    }

    @Test(priority = 2, expectedExceptions = EntityAlreadyExistsException.class)
    public void saveEventWithUniqueNameAndDatesViolated() throws DaoException {
        saveEntityWithUniqueNameViolated();
    }

    @Test(priority = 2, expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingEventById() throws DaoException {
        getNonExistingEntityById();
    }

    @Test(priority = 2, expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingEventByName() throws DaoException {
        getNonExistingEntityByName();
    }

    @Test(priority = 2, expectedExceptions = IllegalDaoRequestException.class)
    public void getEventsInBrokenRange() throws DaoException {
        dao.getEventsInRange(to, from);
    }

    @Test(priority = 2, expectedExceptions = IllegalDaoRequestException.class)
    public void getEventsInNoRange() throws DaoException {
        dao.getEventsInRange(to, to);
    }

    @Test(priority = 3, expectedExceptions = EntityNotFoundException.class)
    public void deleteNonExistingEvent() throws DaoException {
        deleteNonExistingEntity();
    }

    @Test(priority = 3, expectedExceptions = DaoException.class)
    public void deleteEventWithNullId() throws DaoException {
        deleteEntityWithNullId();
    }

    @Test(dataProvider = "eventsListPopulator", priority = 4,
            dependsOnMethods = { "getEventById", "getEventByName",
                    "getEventsInRange", "getAllEntities", "getEventsToDate" })
    public void deleteEvent(Event event) throws DaoException {
        entity = event;
        deleteEntity();
    }


    @Override
    protected Event copyEntity(Event event) {
        Event newEvent = new Event(event.getName(), event.getDate(), event.getBasePrice());
        newEvent.setRating(event.getRating());
        return newEvent;
    }
}
