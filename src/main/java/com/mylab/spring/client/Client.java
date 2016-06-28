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
    public void setId(@Value("${client.id:#{new java.util.Random().nextInt(T(java.lang.Integer).MAX_VALUE) + 1}}")Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    @Autowired
    public void setFullName(@Value("${client.name:" +
            "#{T(java.lang.System).getProperty('os.name').startsWith('Windows')" +
            "? systemEnvironment['USERNAME'] : systemEnvironment['USER']}}")String fullName) {
        this.fullName = fullName;
    }

    public String getGreeting() {
        return greeting;
    }

    @Autowired
    public void setGreeting(@Value("${client.greeting:#{'Hello'}}")String greeting) {
        this.greeting = greeting;
    }
}
