package com.mylab.spring.coredemo.test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    private ConfigurableApplicationContext context;

    @BeforeSuite
    public void initializeSpring() {
        context = new ClassPathXmlApplicationContext("/spring-test-config.xml");
    }

    @AfterSuite
    public void closeSpringContext() {
        context.close();
    }
}
