package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.NamingDao;
import com.mylab.spring.coredemo.entity.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserDao implements NamingDao<User> {

    private final static AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, User> USERS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    public User saveEntity(User entity) {
        return null;
    }

    @Override
    public User getEntityById(Long id) {
        return null;
    }

    @Override
    public User removeEntity(User entity) {
        return null;
    }

    @Override
    public User getEntityByName(String name) {
        return null;
    }

}
