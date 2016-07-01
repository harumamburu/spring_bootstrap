package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.test.dao.NamingDaoTest;
import com.mylab.spring.coredemo.test.dao.BulkDaoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Iterator;

public class AuditoriumsDaoTest extends NamingDaoTest<Auditorium, AuditoriumDao>
        implements BulkDaoTest<Auditorium, AuditoriumDao> {

    @Autowired
    private List<Auditorium> auditoriums;

    @Override
    @Autowired
    protected void setDao(AuditoriumDao dao) {
        this.dao = dao;
    }

    @Override
    @Resource(name = "testAuditorium")
    protected void setEntity(Auditorium entity) {
        this.entity = entity;
    }

    @DataProvider(name = "auditoriumsPopulator")
    private Iterator<Object[]> populateAuditoriums() {
        Iterator<Auditorium> internal = auditoriums.iterator();
        Iterator<Object[]> populator = new Iterator<Object[]>() {
            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Object[] next() {
                return new Object[]{ internal.next() };
            }
        };
        return populator;
    }


    @Test(groups = {"saveTests", "auditoriumsSaveTests"}, dataProvider = "auditoriumsPopulator")
    protected void saveAuditorium(Auditorium auditorium) throws DaoException {
        entity = dao.saveEntity(auditorium);
        Assert.assertNotNull(entity, "Entity wasn't saved");
        Assert.assertNotNull(entity.getId(), "Id haven't been set");
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"gettersTests", "auditoriumGettersTests"},
            priority = 1)
    protected void getAuditoriumById() throws DaoException {
        getEntityById();
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"gettersTests", "auditoriumGettersTests"},
            priority = 1)
    protected void getAuditoriumByName() throws DaoException {
        super.getEntityByName();
    }

    @Override
    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"gettersTests", "auditoriumGettersTests", "bulkTests"},
            priority = 1)
    public void getAllEntities() {
        Assert.assertEquals(((AuditoriumDao) dao).getAllEntities(), auditoriums, "Not all auditoriums match");
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"gettersTests", "auditoriumGettersTests"},
            priority = 1)
    public void getNumberOfSeats() {
        Assert.assertEquals(((AuditoriumDao) dao).getNumberOfSeats(entity.getId()),
                entity.getSeatsNumber(), "Number of seats doesn't match");
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"gettersTests", "auditoriumGettersTests", "bulkTests"},
            priority = 1)
    public void getVipSeats() {
        Assert.assertEquals(((AuditoriumDao) dao).getVipSeats(entity.getId()),
                entity.getVipSeats(), "Vip seats doesn't match");
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"negativeTests", "saveTests", "auditoriumSaveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    protected void saveAuditoriumWithUniqueNameViolated() throws DaoException {
        saveEntityWithUniqueNameViolated();
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"negativeTests", "gettersTests", "auditoriumGettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    protected void getNonExistingAuditoriumById() throws DaoException {
        getNonExistingEntityById();
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"negativeTests", "gettersTests", "auditoriumGettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    protected void getNonExistingAuditoriumByName() throws DaoException {
        getNonExistingEntityByName();
    }
    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"deletingTests", "auditoriumDeletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = EntityNotFoundException.class)
    protected void deleteNonExistingAuditorium() throws DaoException {
        deleteNonExistingEntity();
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"deletingTests", "auditoriumDeletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = DaoException.class)
    protected void deleteAuditoriumWithNullId() throws DaoException {
        deleteEntityWithNullId();
    }

    @Test(dependsOnMethods = "saveAuditorium",
            groups = {"deletingTests", "auditoriumDeletingTests"},
            priority = 4)
    protected void deleteAuditorium() throws DaoException {
        deleteEntity();
    }

    @Override
    protected Auditorium copyEntity(Auditorium entity) {
        Auditorium newAuditorium = new Auditorium(entity.getName(), entity.getSeatsNumber());
        newAuditorium.setVipSeats(entity.getVipSeats());
        return newAuditorium;
    }

}