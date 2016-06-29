package com.mylab.spring.coredemo.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = "classpath:spring-test-config.xml")
public class BaseTest extends AbstractTestNGSpringContextTests {

}
