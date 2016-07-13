package com.mylab.spring.coredemo.service;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Auditorium;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;

public class AuditoriumService extends AbstractService {

    @PostConstruct
    @Autowired
    private void registerAuditoriums(List<Auditorium> auditoriums) {
        auditoriums.stream().forEach(auditorium -> {
            try {
                auditoriumDao.saveEntity(auditorium);
            } catch (DaoException e) {
                LOG().log(Level.WARNING, String.format("FAILED TO REGISTER AUDITORIUM %s", auditorium.getName()), e);
            }
        });
    }

    public List<Auditorium> getAllAuditoriums() {
        return null;
    }

    public Integer getSeatsNumber(Long id) {
        return null;
    }

    public List<Integer> getVipSeats(Long id) {
        return null;
    }
}
