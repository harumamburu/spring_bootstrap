package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.Dao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Entity;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public abstract class AbstractMemoryDao<T extends Entity> implements Dao<T> {

    protected abstract Map<Long, T> getStorage();
    protected abstract AtomicLong getCounter();

    /**
     * Check if the passed entity was already persisted, by searching its unique fields
     * in stored entities
     * @param entity
     * @return true if at least one entity with unique field
     * of the same value as from the passed was already saved,
     * false otherwise
     */
    protected abstract boolean isSavedAlready(T entity);

    @Override
    public T saveEntity(T entity) throws DaoException {
        if (isSavedAlready(entity)) {
            throw new EntityAlreadyExistsException(String.format("%s already exists", entity.toString()));
        }
        entity.setId(getCounter().incrementAndGet());
        getStorage().put(entity.getId(), entity);
        return entity;
    }

    @Override
    public T getEntityById(Long id) throws DaoException {
        return Optional.ofNullable(getStorage().get(id))
                .orElseThrow(supplyEntityNotFound(id));
    }

    @Override
    public T removeEntity(T entity) throws DaoException {
        if (entity.isIdNull()) {
            throw new DaoException("Entity id is null");
        }
        return Optional.ofNullable(getStorage().remove(entity.getId()))
                .orElseThrow(supplyEntityNotFound(entity.getId()));
    }

    private Supplier<EntityNotFoundException> supplyEntityNotFound(Long id) {
        return () -> new EntityNotFoundException(String.format("Entity with id={%d} doesn't exists", id));
    }
}
