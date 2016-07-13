package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Auditorium;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;

public abstract class AuditoriumService extends AbstractService {

    @PostConstruct
    private void registerAuditoriums() {
        getAuditoriums().stream().forEach(auditorium -> {
            try {
                auditoriumDao.saveEntity(auditorium);
            } catch (DaoException e) {
                LOG().log(Level.WARNING, String.format("FAILED TO REGISTER AUDITORIUM %s", auditorium.getName()), e);
            }
        });
    }

    // a method contract for spring method injection
    protected abstract List<Auditorium> getAuditoriums();

    public List<Auditorium> getAllAuditoriums() {
        return auditoriumDao.getAllEntities();
    }

    public Integer getSeatsNumber(Long id) throws DaoException {
        return auditoriumDao.getNumberOfSeats(id);
    }

    public List<Integer> getVipSeats(Long id) throws DaoException {
        return auditoriumDao.getVipSeats(id);
    }
}
