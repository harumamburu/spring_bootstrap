package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserDaoTest {

    @Autowired
    @Qualifier("userMemoryDao")
    private User user;
    private UserDao userDao;
    
    @Test(groups = "saveTests")
    public void testSaveUser() {
        user = userDao.saveEntity(user);
        Assert.assertNotNull(user.getId(), "Id Haven't been set");
    }
    
    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserById() {
        Assert.assertEquals(userDao.getEntityById(user.getId()), user, "Failed to get user by Id");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserByName() {
        Assert.assertEquals(userDao.getEntityByName(user.getName()), user, "Failed to get user by name");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserByEmail() {
        Assert.assertEquals(userDao.getUserByEmail(user.getEmail()), user, "Failed to get user by Email");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void testNegativeSaveUserWithName() {
        User newUser = copyUser(user);
        newUser.setName("New" + user.getName());
        userDao.saveEntity(user);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void testNegativeSaveUserWithEmail() {
        User newUser = copyUser(user);
        newUser.setEmail("New" + user.getEmail());
        userDao.saveEntity(user);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserById() {
        userDao.getEntityById(Long.MAX_VALUE);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserByName() {
        userDao.getEntityByName("");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserByEmail() {
        userDao.getUserByEmail("");
    }

    @Test(dependsOnGroups = "gettersTests", priority = 3)
    public void deleteUserTest() {
        user = userDao.removeEntity(user);
        Assert.assertNull(userDao.getEntityById(user.getId()), "Deleted user is still in the storage");
    }

    private User copyUser(User oldUser) {
        User newUser = oldUser;
        newUser.setId(null);
        return newUser;
    }

}
