package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.entity.Entity;

import java.util.List;

public interface BulkDao<T extends Entity> {

    List<T> getAllEntities();
}
