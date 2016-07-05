package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.BookingDao;
import com.mylab.spring.coredemo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService implements Service {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected BookingDao bookingDao;
}
