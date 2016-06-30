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

    // to be injected in concrete test-classes
    protected abstract void setDao(K dao);
    protected abstract void setEntity(T entity);

    @Test(groups = "saveTests")
    public void saveEntity() throws DaoException {
        entity = dao.saveEntity(entity);
        Assert.assertNotNull(entity, "Entity wasn't saved");
        Assert.assertNotNull(entity.getId(), "Id haven't been set");
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = "gettersTests",
            priority = 1)
    public void getEntityById() throws DaoException {
        Assert.assertEquals(dao.getEntityById(entity.getId()), entity, "Failed to get entity by Id");
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingEntityById() throws DaoException {
        dao.getEntityById(Long.MAX_VALUE);
    }

    @Test(dependsOnGroups = "gettersTests",
            groups = {"deletingTests", "negativeTests"},
            priority = 3)
    public void deleteNonExistingEntity() throws DaoException {
        T newEntity = copyEntity(entity);
        newEntity.setId(Long.MAX_VALUE);
        Assert.assertNull(dao.removeEntity(newEntity), "Null should have been returned");
    }

    @Test(dependsOnGroups = "gettersTests",
            groups = {"deletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = DaoException.class)
    public void deleteEntityWithNullId() throws DaoException {
        dao.removeEntity(copyEntity(entity));
    }

    @Test(dependsOnGroups = "deletingTests",
            priority = 4)
    public void deleteEntity() throws DaoException {
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
