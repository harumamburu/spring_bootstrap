package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Booking;
import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.User;

import java.util.List;

public interface BookingDao extends Dao<Booking> {

    List<Booking> getBookingsForUser(User user) throws DaoException;
    List<Booking> getBookingsForEvent(Event event) throws DaoException;
    List<Booking> getBookingsForEventUser(Event event, User user) throws DaoException;
}
