package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.NamingDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Entity;
import com.mylab.spring.coredemo.entity.Named;

public abstract class AbstractNamingMemoryDao<T extends Entity & Named> extends AbstractMemoryDao<T> implements NamingDao<T> {

    @Override
    public T getEntityByName(String name) throws DaoException {
        return getStorage().values().parallelStream()
                .filter(entity -> entity.getName().equals(name)).findAny()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with name={%s} doesn't exists", name)));
    }
}
