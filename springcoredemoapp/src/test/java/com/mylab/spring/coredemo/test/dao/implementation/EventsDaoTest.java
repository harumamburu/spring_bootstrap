package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.EventDao;
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

    @Test(dependsOnMethods = "saveEntity",
            groups = {"gettersTests", "bulkTests"},
            priority = 1)
    public void getEventsInRange() {
        assertAllMatchPredicate(((EventDao) dao).getEventsInRange(from, to),
                event -> event.getDate().after(from) && event.getDate().before(to));
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"gettersTests", "bulkTests"},
            priority = 1)
    public void getEventsToDate() {
        assertAllMatchPredicate(((EventDao) dao).getEventsToDate(to), event -> event.getDate().before(to));
    }

    private void assertAllMatchPredicate(List<Event> eventsToAssert, Predicate<? super Event> predicate) {
        Assert.assertTrue(eventsToAssert.parallelStream().allMatch(predicate),
                "Not all expected events were returned");
    }

    @Override
    @Test(dependsOnMethods = "saveEntity",
            groups = {"gettersTests", "bulkTests"},
            priority = 1)
    public void getAllEntities() {
        Assert.assertEquals(((EventDao) dao).getAllEntities(), Collections.singletonList(entity), "Not same events were returned");
    }

    @Override
    protected Event copyEntity(Event event) {
        Event newEvent = new Event(event.getName(), event.getDate(), event.getBasePrice());
        newEvent.setBasePrice(event.getBasePrice());
        newEvent.setRating(event.getRating());
        return event;
    }
}
