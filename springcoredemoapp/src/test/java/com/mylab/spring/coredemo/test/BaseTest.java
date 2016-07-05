package com.mylab.spring.coredemo.test;

import com.mylab.spring.coredemo.entity.Entity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;

@ContextConfiguration(locations = "classpath:spring-test-config.xml")
public class BaseTest extends AbstractTestNGSpringContextTests {

    protected void assertSaving(Entity entity) {
        Assert.assertNotNull(entity , String.format("Null was returned instead of %s", entity.toString()));
        Assert.assertNotNull(entity.getId(), String.format("Id wasn't set for %s", entity));
    }
}
