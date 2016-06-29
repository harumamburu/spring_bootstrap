package com.mylab.spring.coredemo.test.dao;

import com.mylab.spring.coredemo.dao.UserDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.dao.exception.EntityAlreadyExistsException;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.User;
import com.mylab.spring.coredemo.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.Assert.ThrowingRunnable;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class UserDaoTest extends BaseTest {

    @Autowired
    private User user;
    @Resource(name = "userMemoryDao")
    private UserDao userDao;
    
    @Test(groups = "saveTests")
    public void saveUser() throws DaoException {
        user = userDao.saveEntity(user);
        Assert.assertNotNull(user, "User wasn't saved");
        Assert.assertNotNull(user.getId(), "Id Haven't been set");
    }
    
    @Test(dependsOnMethods = "saveUser",
            groups = "gettersTests",
            priority = 1)
    public void getUserById() throws DaoException {
        Assert.assertEquals(userDao.getEntityById(user.getId()), user, "Failed to get user by Id");
    }

    @Test(dependsOnMethods = "saveUser",
            groups = "gettersTests",
            priority = 1)
    public void getUserByName() throws DaoException  {
        Assert.assertEquals(userDao.getEntityByName(user.getName()), user, "Failed to get user by name");
    }

    @Test(dependsOnMethods = "saveUser",
            groups = "gettersTests",
            priority = 1)
    public void getUserByEmail() throws DaoException  {
        Assert.assertEquals(userDao.getUserByEmail(user.getEmail()), user, "Failed to get user by Email");
    }

    @Test(dependsOnMethods = "saveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void saveUserWithUniqueNameViolated() throws DaoException {
        User newUser = copyUser(user);
        newUser.setName("New" + user.getName());
        userDao.saveEntity(user);
    }

    @Test(dependsOnMethods = "saveUser",
            groups = {"negativeTests", "saveTests"},
            priority = 2,
            expectedExceptions = EntityAlreadyExistsException.class)
    public void saveUserWithUniqueEmailViolated() throws DaoException {
        User newUser = copyUser(user);
        newUser.setEmail("New" + user.getEmail());
        userDao.saveEntity(user);
    }

    @Test(dependsOnMethods = "saveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserById() throws DaoException {
        userDao.getEntityById(Long.MAX_VALUE);
    }

    @Test(dependsOnMethods = "saveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserByName() throws DaoException {
        userDao.getEntityByName("");
    }

    @Test(dependsOnMethods = "saveUser",
            groups = {"negativeTests", "gettersTests"},
            priority = 2,
            expectedExceptions = EntityNotFoundException.class)
    public void getNonExistingUserByEmail() throws DaoException {
        userDao.getUserByEmail("");
    }

    @Test(dependsOnGroups = "gettersTests",
            groups = {"deletingTests", "negativeTests"},
            priority = 3)
    public void deleteNonExistingUser() throws DaoException {
        User newUser = copyUser(user);
        newUser.setId(Long.MAX_VALUE);
        Assert.assertNull(userDao.removeEntity(newUser), "Null should have been returned");
    }

    @Test(dependsOnGroups = "gettersTests",
            groups = {"deletingTests", "negativeTests"},
            priority = 3,
            expectedExceptions = DaoException.class)
    public void deleteUserWithNullId() throws DaoException {
        userDao.removeEntity(copyUser(user));
    }

    @Test(dependsOnGroups = "deletingTests",
            priority = 4)
    public void deleteUser() throws DaoException {
        userDao.removeEntity(user);
        Assert.assertThrows(EntityNotFoundException.class, () -> userDao.getEntityById(user.getId()));
    }

    private User copyUser(User oldUser) {
        User newUser = new User(oldUser.getName(), oldUser.getEmail());
        return newUser;
    }

}
