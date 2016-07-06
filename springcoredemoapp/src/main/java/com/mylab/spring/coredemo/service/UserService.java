package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserService extends AbstractService {

    /*register, remove, getById, getUserByEmail, getUsersByName, getBookedTickets*/

    public User register(User user) throws DaoException {
        return userDao.saveEntity(user);
    }

    public User remove(User user) throws DaoException {
        User deleted = userDao.removeEntity(user);
        bookingDao.getBookingsForUser(deleted).parallelStream().forEach(booking -> {
            try {
                bookingDao.removeEntity(booking);
            } catch (DaoException e) {
            }
        });
        return deleted;
    }

    public User getById(Long id) throws DaoException {
        return userDao.getEntityById(id);
    }

    public User getByName(String name) throws DaoException {
        return userDao.getEntityByName(name);
    }

    public User getByEmail(String email) throws DaoException {
        return userDao.getUserByEmail(email);
    }

    public List<Ticket> getBookedTickets(User user) throws DaoException {
        return bookingDao.getBookingsForUser(user).parallelStream()
                .flatMap(booking -> booking.getTickets().parallelStream()).collect(Collectors.toList());
        // get streams of bookings' ticket lists flattened to one single stream and collect it as list
    }
}
