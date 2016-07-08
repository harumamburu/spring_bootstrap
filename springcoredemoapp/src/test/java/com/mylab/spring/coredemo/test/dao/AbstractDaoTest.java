package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.Dao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Entity;
import com.mylab.spring.coredemo.test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "daoTest")
public abstract class AbstractDaoTest<T extends Entity, K extends Dao<T>> extends BaseTest {

    protected K dao;
    protected T entity;

    // to be injected in concrete test-classes
    protected abstract void setDao(K dao);
    protected abstract void setEntity(T entity);

    protected void saveEntity() throws DaoException {
        entity = dao.saveEntity(entity);
        assertSaving(entity);
    }

    protected void getEntityById() throws DaoException {
        Assert.assertEquals(dao.getEntityById(entity.getId()), entity, "Failed to get entity by Id");
    }

    protected void getNonExistingEntityById() throws DaoException {
        dao.getEntityById(Long.MAX_VALUE);
    }

    protected void deleteNonExistingEntity() throws DaoException {
        T newEntity = copyEntity(entity);
        newEntity.setId(Long.MAX_VALUE);
        Assert.assertNull(dao.removeEntity(newEntity), "Null should have been returned");
    }

    protected void deleteEntityWithNullId() throws DaoException {
        dao.removeEntity(copyEntity(entity));
    }

    protected void deleteEntity() throws DaoException {
        dao.removeEntity(entity);
        Assert.assertThrows(EntityNotFoundException.class, () -> dao.getEntityById(entity.getId()));
    }

    /**
     * Make a new instance of an entity copying some fields
     * from the source
     * @param entity to be copied
     * @return a new instance of entity class with all required fields
     */
    protected abstract T copyEntity(T entity);

}
