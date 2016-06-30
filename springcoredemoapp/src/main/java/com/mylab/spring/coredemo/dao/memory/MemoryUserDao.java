package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserDao extends AbstractNamingMemoryDao<User> implements UserDao {

    private final static AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, User> USERS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    protected Map<Long, User> getStorage() {
        return USERS;
    }

    @Override
    protected AtomicLong getCounter() {
        return COUNTER;
    }

    @Override
    protected boolean isSavedAlready(User entity) {
        return USERS.values().parallelStream().anyMatch(
                user -> user.getEmail().equals(entity.getEmail()) || user.getName().equals(entity.getName()));
    }

    @Override
    public User getUserByEmail(String email) throws DaoException {
        return USERS.values().parallelStream()
                .filter(user -> user.getEmail().equals(email)).findAny()
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with email={%s} doesn't exists", email)));
    }
}
