package com.mylab.spring.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Client {

    private Long id;
    private String fullName;
    private String greeting;

    @Autowired
    public Client(@Value("${client.id}")Long id, @Value("${client.name}")String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGreeting() {
        return greeting;
    }

    @Value("${client.greeting}")
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
