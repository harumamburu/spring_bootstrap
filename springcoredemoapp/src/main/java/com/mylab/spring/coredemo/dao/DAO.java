package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Entity;

public interface Dao<T extends Entity> {

    T saveEntity(T entity) throws DaoException;
    T getEntityById(Long id) throws DaoException;
    T removeEntity(T entity) throws DaoException;
}
