package com.mylab.spring.coredemo.test.service;

import com.mylab.spring.coredemo.service.Service;
import com.mylab.spring.coredemo.test.BaseTest;
import org.testng.annotations.Test;

@Test(groups = "serviceTest"/*, dependsOnGroups = "daoTest"*/)
public abstract class AbstractServiceTest<T extends Service> extends BaseTest {

    protected T service;

    protected abstract void setService(T service);
}
