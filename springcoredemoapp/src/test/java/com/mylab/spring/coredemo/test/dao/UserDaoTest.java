package com.mylab.spring.coredemo.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserDaoTest {

    @Autowired
    @Qualifier("userMemoryDao")
    private User user;
    private DAO<User> userDao;
    
    @Test(groups = "saveTests")
    public void testSaveUser() {
        user = userDao.save(user);
        Assert.assertNotNull(user.getId(), "Id Haven't been set");
    }
    
    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserById() {
        Assert.assertEquals(userDao.getById(user.getId()), user, "Failed to get user by Id");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserByName() {
        Assert.assertEquals(userDao.getByName(user.getName()), user, "Failed to get user by name");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = "gettersTests",
            priority = 1)
    public void testGetUserByEmail() {
        Assert.assertEquals(userDao.getByEmail(user.getEmail()), user, "Failed to get user by Email");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = AlreadyExistsException.class)
    public void testNegativeSaveUserWithName() {
        User newUser = copyUser(user);
        newUser.setName("New" + user.getName());
        userDao.save(user);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = AlreadyExistsException.class)
    public void testNegativeSaveUserWithEmail() {
        User newUser = copyUser(user);
        newUser.setEmail("New" + user.getEmail());
        userDao.save(user);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserById() {
        userDao.getById(Long.MAX_VALUE);
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserById() {
        userDao.getByName("");
    }

    @Test(dependsOnMethods = "testSaveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void testNegativeGetUserById() {
        userDao.getByEmail("");
    }

    @Test(dependsOnGroups = "gettersTests", priority = 3)
    public void deleteUserTest() {
        user = userDao.delete(user);
        Assert.assertNull(userDao.getById(user.getId()), "Deleted user is still in the storage");
    }

    private User copyUser(User oldUser) {
        User newUser = oldUser;
        newUser.setId(null);
        return newUser;
    }

}
