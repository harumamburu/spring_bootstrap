package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.NamingDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Entity;
import com.mylab.spring.coredemo.entity.Named;
import org.testng.Assert;
import org.testng.annotations.Test;

public abstract class NamingDaoTest<T extends Entity & Named, K extends NamingDao<T>> extends AbstractDaoTest<T, K> {

    @Test(dependsOnMethods = "saveEntity",
            groups = "gettersTests",
            priority = 1)
    public void getEntityByName() throws DaoException {
        Assert.assertEquals(((K) dao).getEntityByName(((T) entity).getName()), entity, "Failed to get entity by name");
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void saveEntityWithUniqueNameViolated() throws DaoException {
        T newEntity = (T) copyEntity(entity);
        newEntity.setName("New" + ((T) entity).getName());
        dao.saveEntity(entity);
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingEntityByName() throws DaoException {
        ((K) dao).getEntityByName("");
    }
}
