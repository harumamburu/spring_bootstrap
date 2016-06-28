package com.mylab.spring.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Client {

    private Long id;
    private String fullName;
    private String greeting;

    public Long getId() {
        return id;
    }

    @Autowired
    public void setId(@Value("${client.id}")Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    @Autowired
    public void setFullName(@Value("${client.name}")String fullName) {
        this.fullName = fullName;
    }

    public String getGreeting() {
        return greeting;
    }

    @Autowired
    public void setGreeting(@Value("${client.greeting}")String greeting) {
        this.greeting = greeting;
    }
}
