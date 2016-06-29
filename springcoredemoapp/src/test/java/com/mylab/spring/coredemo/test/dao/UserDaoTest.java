package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.Dao;
import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class UserDaoTest extends AbstractDaoTest<User, UserDao> {

    @Override
    @Resource(name = "userMemoryDao")
    protected void setDao(UserDao userDao) {
        dao = userDao;
    }

    @Override
    @Autowired
    protected void setEntity(User user) {
        entity = user;
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = "gettersTests",
            priority = 1)
    public void getUserById() throws DaoException {
        Assert.assertEquals(dao.getEntityById(entity.getId()), entity, "Failed to get user by Id");
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = "gettersTests",
            priority = 1)
    public void getUserByName() throws DaoException  {
        Assert.assertEquals(((UserDao) dao).getEntityByName(entity.getName()), entity, "Failed to get user by name");
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = "gettersTests",
            priority = 1)
    public void getUserByEmail() throws DaoException  {
        Assert.assertEquals(((UserDao) dao).getUserByEmail(entity.getEmail()), entity, "Failed to get user by Email");
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void saveUserWithUniqueNameViolated() throws DaoException {
        User newUser = copyUser(entity);
        newUser.setName("New" + entity.getName());
        dao.saveEntity(entity);
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void saveUserWithUniqueEmailViolated() throws DaoException {
        User newUser = copyUser(entity);
        newUser.setEmail("New" + entity.getEmail());
        dao.saveEntity(entity);
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserById() throws DaoException {
        dao.getEntityById(Long.MAX_VALUE);
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserByName() throws DaoException {
        ((UserDao) dao).getEntityByName("");
    }

    @Test(dependsOnMethods = "saveEntity",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserByEmail() throws DaoException {
        ((UserDao) dao).getUserByEmail("");
    }

    @Test(dependsOnGroups = "gettersTests",
            groups = {"deletingTests", "negativeTests"},
            priority = 3)
    public void deleteNonExistingUser() throws DaoException {
        User newUser = copyUser(entity);
        newUser.setId(Long.MAX_VALUE);
        Assert.assertNull(dao.removeEntity(newUser), "Null should have been returned");
    }

    @Test(dependsOnGroups = "gettersTests",
            groups = {"deletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = DaoException.class)
    public void deleteUserWithNullId() throws DaoException {
        dao.removeEntity(copyUser(entity));
    }

    private User copyUser(User oldUser) {
        User newUser = new User(oldUser.getName(), oldUser.getEmail());
        return newUser;
    }

}
