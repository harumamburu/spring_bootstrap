package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class UserDaoTest extends BaseTest {

    @Autowired
    private User user;
    @Resource(name = "userMemoryDao")
    private UserDao userDao;
    
    @Test(groups = "saveTests")
    public void testSaveUser() throws DaoException {
        user = userDao.saveEntity(user);
        Assert.assertNotNull(user, "User wasn't saved");
        Assert.assertNotNull(user.getId(), "Id Haven't been set");
    }
    
    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserById() throws DaoException {
        Assert.assertEquals(userDao.getEntityById(user.getId()), user, "Failed to get user by Id");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserByName() throws DaoException  {
        Assert.assertEquals(userDao.getEntityByName(user.getName()), user, "Failed to get user by name");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserByEmail() throws DaoException  {
        Assert.assertEquals(userDao.getUserByEmail(user.getEmail()), user, "Failed to get user by Email");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void testNegativeSaveUserWithUniqueNameViolated() throws DaoException {
        User newUser = copyUser(user);
        newUser.setName("New" + user.getName());
        userDao.saveEntity(user);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void testNegativeSaveUserWithUniqueEmailViolated() throws DaoException {
        User newUser = copyUser(user);
        newUser.setEmail("New" + user.getEmail());
        userDao.saveEntity(user);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserById() throws DaoException  {
        userDao.getEntityById(Long.MAX_VALUE);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserByName() throws DaoException  {
        userDao.getEntityByName("");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserByEmail() throws DaoException  {
        userDao.getUserByEmail("");
    }

    @Test(dependsOnGroups = "gettersTests",
            priority = 3,
            expectedExceptions = EntityNotFoundException.class)
    public void deleteUserTest() throws DaoException  {
        user = userDao.removeEntity(user);
        userDao.getEntityById(user.getId());
    }

    @Test(dependsOnGroups = "gettersTests",
            priority = 3)
    public void deleteNonExistingUserTest() throws DaoException  {
        Assert.assertNull(userDao.removeEntity(copyUser(user)), "Null should have been returned");
    }

    private User copyUser(User oldUser) {
        User newUser = new User(oldUser.getName(), oldUser.getEmail());
        return newUser;
    }

}
