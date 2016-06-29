package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserDao implements UserDao {

    private final static AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, User> USERS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    public User saveEntity(User entity) throws DaoException {
        if (USERS.values().parallelStream().anyMatch(
                user -> user.getEmail().equals(entity.getEmail()) || user.getName().equals(entity.getName()))) {
            throw new EntityAlreadyExistsException(String.format("%s already exists", entity.toString()));
        }
        entity.setId(COUNTER.incrementAndGet());
        USERS.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User getEntityById(Long id) throws DaoException {
        return Optional.ofNullable(USERS.get(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id={%d} doesn't exists", id)));
    }

    @Override
    public User removeEntity(User entity) throws DaoException {
        if (entity.isIdNull()) {
            throw new DaoException("Entity id is null");
        }
        return USERS.remove(entity.getId());
    }

    @Override
    public User getEntityByName(String name) throws DaoException {
        return USERS.values().parallelStream()
                .filter(user -> user.getName().equals(name)).findAny()
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with name={%s} doesn't exists", name)));
    }

    public User getUserByEmail(String email) throws DaoException {
        return USERS.values().parallelStream()
                .filter(user -> user.getEmail().equals(email)).findAny()
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with email={%s} doesn't exists", email)));
    }
}
