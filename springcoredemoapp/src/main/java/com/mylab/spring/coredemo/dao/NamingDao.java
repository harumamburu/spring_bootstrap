package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.dao.Dao;
import com.mylab.spring.coredemo.entity.Entity;
import com.mylab.spring.coredemo.entity.Named;

public interface NamingDao<T extends Entity & Named> extends Dao<T> {

    T getEntityByName(String name);
}
