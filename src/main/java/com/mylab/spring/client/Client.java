package com.mylab.spring.client;

/**
 * Created by lilie0-ASUS on 10/27/2015.
 */
public class Client {

    private Long id;
    private String fullName;

    public Client(Long id, String fullName) {
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
}
