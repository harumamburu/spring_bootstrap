package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Auditorium;

import java.util.List;

public interface AuditoriumDao extends NamingDao<Auditorium>, BulkDao<Auditorium> {

    Integer getNumberOfSeats(Long id) throws EntityNotFoundException;
    List<Integer> getVipSeats(Long id) throws EntityNotFoundException;
}
