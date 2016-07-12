package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Event;

import java.util.Date;
import java.util.List;

public interface EventDao extends NamingDao<Event>, BulkDao<Event> {

    List<Event> getEventsInRange(Date from, Date to) throws DaoException;

    /**
     * Get a particalar event at a particular non-ambiguous date
     * @return event
     * @throws DaoException
     */
    Event getEventAtDate(Event event, Date date) throws DaoException;

    /**
     * Gete all possible events for a day represented by the specified date
     * @param event
     * @param date a date instance from which day of year and year will be taken
     * @return list of specified events for a specified event
     */
    List<Event> getEventsAtDate(Event event, Date date);
}
