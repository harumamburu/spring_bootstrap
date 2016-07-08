package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.entity.Event;
import com.mylab.spring.coredemo.entity.Ticket;
import com.mylab.spring.coredemo.entity.User;

import java.util.List;

public interface TicketDao extends Dao<Ticket>, BulkDao<Ticket> {

    List<Ticket> getTicketsForEvent(Event event);
}
