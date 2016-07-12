package com.mylab.spring.coredemo.test.service;

import com.mylab.spring.coredemo.dao.AuditoriumDao;
import com.mylab.spring.coredemo.dao.EventDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Auditorium;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.enumeration.Rating;
import com.mylab.spring.coredemo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Test(groups = "eventServiceTest")
public class EventServiceTest extends AbstractServiceTest<EventService> {

    @Value("${test.bookingserv.dateformat}")
    private String dateFormat;
    @Value("${test.bookingserv.timeformat}")
    private String timeFormat;
    private String eventName = "Event!";
    private String eventDate;
    private String eventTime;
    private Date date;
    private Double eventPrice = 99.9d;
    private Rating eventRating = Rating.HIGH;
    @Resource(name = "testAuditorium2")
    private Auditorium auditorium;
    private Event event;

    @Autowired
    private EventDao eventDao;
    @Autowired
    private AuditoriumDao auditoriumDao;

    @BeforeClass
    private void setupDateTime() throws ParseException {
        eventDate = LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat));
        eventTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
        date = new SimpleDateFormat(dateFormat + " " + timeFormat).parse(eventDate + " " + eventTime);
    }

    @BeforeClass
    private void persistDependencies() throws DaoException {
        auditorium = auditoriumDao.saveEntity(auditorium);
    }

    @Override
    @Autowired
    protected void setService(EventService service) {
        this.service = service;
    }


    @Test
    public void saveEvent() throws DaoException {
        event = service.saveEvent(eventName, date, eventPrice, eventRating);
        Assert.assertNotNull(event.getId(), "Event id wasn't set");
        Assert.assertEquals(event, eventDao.getEntityById(event.getId()));
    }

    @Test(dependsOnMethods = "saveEvent")
    public void assignAuditorium() throws DaoException {
        service.assignAuditorium(event, auditorium, date);
        Auditorium actual = eventDao.getEventAtDate(event, date).getAuditorium();
        Assert.assertNotNull(actual, "Auditorium wasn't assigned for event");
        Assert.assertEquals(actual, auditorium);
    }
}
