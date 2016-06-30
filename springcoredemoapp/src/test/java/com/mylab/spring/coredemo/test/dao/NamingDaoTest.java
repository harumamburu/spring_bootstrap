package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.NamingDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Entity;
import com.mylab.spring.coredemo.entity.Named;
import org.testng.Assert;

public abstract class NamingDaoTest<T extends Entity & Named, K extends NamingDao<T>> extends AbstractDaoTest<T, K> {

    protected void getEntityByName() throws DaoException {
        Assert.assertEquals(((K) dao).getEntityByName(((T) entity).getName()), entity, "Failed to get entity by name");
    }

    protected void saveEntityWithUniqueNameViolated() throws DaoException {
        T newEntity = (T) copyEntity(entity);
        newEntity.setName("New" + ((T) entity).getName());
        dao.saveEntity(entity);
    }

    protected void getNonExistingEntityByName() throws DaoException {
        ((K) dao).getEntityByName("");
    }
}
