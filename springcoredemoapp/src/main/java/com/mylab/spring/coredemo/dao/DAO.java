package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.entity.Entity;

public interface Dao<T extends Entity> {

    T saveEntity(T entity);
    T getEntityById(Long id);
    T removeEntity(T entity);
}
