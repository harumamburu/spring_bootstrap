package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.AuditoriumDao;
import com.mylab.spring.coredemo.dao.BookingDao;
import com.mylab.spring.coredemo.dao.EventDao;
import com.mylab.spring.coredemo.dao.TicketDao;
import com.mylab.spring.coredemo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

public abstract class AbstractService implements Service {

    protected Logger LOG() {
        return Logger.getLogger(this.getClass().getSimpleName());
    }

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected BookingDao bookingDao;

    @Autowired
    protected TicketDao ticketDao;

    @Autowired
    protected EventDao eventDao;

    @Autowired
    protected AuditoriumDao auditoriumDao;
}
