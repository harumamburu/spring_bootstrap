package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.Dao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Entity;
import com.mylab.spring.coredemo.test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public abstract class AbstractDaoTest<T extends Entity, K extends Dao<T>> extends BaseTest {

    protected Dao<T> dao;
    protected T entity;
    
    protected abstract void setDao(K dao);
    protected abstract void setEntity(T entity);
    
    @Test(groups = "saveTests")
    public void saveEntity() throws DaoException {
        entity = dao.saveEntity(entity);
        Assert.assertNotNull(entity, "User wasn't saved");
        Assert.assertNotNull(entity.getId(), "Id Haven't been set");
    }


    @Test(dependsOnGroups = "deletingTests",
            priority = 4)
    public void deleteEntity() throws DaoException {
        dao.removeEntity(entity);
        Assert.assertThrows(EntityNotFoundException.class, () -> dao.getEntityById(entity.getId()));
    }

}
