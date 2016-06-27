package com.mylab.spring.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("client")
public class Client {

    private Long id;
    private String fullName;
    private String greeting;

    public Client(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    @Autowired
    @Qualifier("${client.id}")
    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    @Autowired
    @Qualifier("${client.name}")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGreeting() {
        return greeting;
    }

    @Autowired
    @Qualifier("${client.greeting}")
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
