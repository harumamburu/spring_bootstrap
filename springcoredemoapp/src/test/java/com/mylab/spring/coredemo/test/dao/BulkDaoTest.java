package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.BulkDao;
import com.mylab.spring.coredemo.entity.Entity;
import org.testng.annotations.Test;

public interface BulkDaoTest<T extends Entity, K extends BulkDao<T>> {

    @Test
    void getAllEntities();
}
