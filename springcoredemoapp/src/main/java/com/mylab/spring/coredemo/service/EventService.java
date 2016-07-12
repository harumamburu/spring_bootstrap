package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Auditorium;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.enumeration.Rating;

import java.util.Date;

public class EventService extends AbstractService {


    public Event saveEvent(String eventName, Date date, Double eventPrice, Rating eventRating) throws DaoException {
        Event event = new Event(eventName, date, eventPrice);
        event.setRating(eventRating);
        return eventDao.saveEntity(event);
    }

    public void assignAuditorium(Event event, Auditorium auditorium, Date date) throws DaoException {
        Event eventAt = eventDao.getEventAtDate(event, date);
        eventAt.setAuditorium(auditorium);
        eventDao.updateEntity(event);
    }
}
