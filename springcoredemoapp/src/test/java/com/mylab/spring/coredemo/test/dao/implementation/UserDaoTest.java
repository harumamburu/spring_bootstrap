package com.mylab.spring.coredemo.test.dao.implementation;

import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.test.dao.NamingDaoTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@Test(groups = "userDaoTest")
public class UserDaoTest extends NamingDaoTest<User, UserDao> {

    @Override
    @Resource(name = "userMemoryDao")
    protected void setDao(UserDao userDao) {
        dao = userDao;
    }

    @Override
    @Resource(name = "testUser")
    protected void setEntity(User user) {
        entity = user;
    }


    @Test
    public void saveUser() throws DaoException {
        saveEntity();
    }

    @Test(dependsOnMethods = "saveUser", priority = 1)
    public void getUserById() throws DaoException {
        getEntityById();
    }

    @Test(dependsOnMethods = "saveUser", priority = 1)
    public void getUserByName() throws DaoException {
        getEntityByName();
    }

    @Test(dependsOnMethods = "saveUser", priority = 1)
    public void getUserByEmail() throws DaoException  {
        Assert.assertEquals(dao.getUserByEmail(entity.getEmail()), entity, "Failed to get user by Email");
    }

    @Test(priority = 2, expectedExceptions = EntityAlreadyExistsException.class)
    public void saveUserWithUniqueNameViolated() throws DaoException {
        saveEntityWithUniqueNameViolated();
    }

    @Test(priority = 2, expectedExceptions = EntityAlreadyExistsException.class)
    public void saveUserWithUniqueEmailViolated() throws DaoException {
        User newUser = copyEntity(entity);
        newUser.setEmail("New" + entity.getEmail());
        dao.saveEntity(entity);
    }

    @Test(priority = 2, expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserById() throws DaoException {
        getNonExistingEntityById();
    }

    @Test(priority = 2, expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserByName() throws DaoException {
        getNonExistingEntityByName();
    }

    @Test(priority = 2, expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserByEmail() throws DaoException {
        dao.getUserByEmail("");
    }

    @Test(priority = 3, expectedExceptions = EntityNotFoundException.class)
    public void deleteNonExistingUser() throws DaoException {
        deleteNonExistingEntity();
    }

    @Test(priority = 3, expectedExceptions = DaoException.class)
    public void deleteUserWithNullId() throws DaoException {
        deleteEntityWithNullId();
    }

    @Test(dependsOnMethods = { "getUserById", "getUserByName", "getUserByEmail" }, priority = 4)
    public void deleteUser() throws DaoException {
        deleteEntity();
    }


    protected User copyEntity(User oldUser) {
        User newUser = new User(oldUser.getName(), oldUser.getEmail());
        return newUser;
    }

}
