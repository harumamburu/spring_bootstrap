package com.mylab.spring.coredemo.dao;

import com.mylab.spring.coredemo.entity.User;

public interface UserDao extends NamingDao<User> {

    User getUserByEmail(String email);
}
