package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Event;

import java.util.Date;
import java.util.List;

public interface EventDao extends NamingDao<Event>, BulkDao<Event> {

    List<Event> getEventsInRange(Date from, Date to) throws DaoException;
    List<Event> getEventsToDate(Date to) throws DaoException;
}
