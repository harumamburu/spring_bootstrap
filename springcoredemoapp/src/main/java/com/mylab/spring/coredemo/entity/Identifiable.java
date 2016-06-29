package com.mylab.spring.coredemo.entity;

public interface Identifiable {

    Long getId();
    void setId(Long id);
    default Boolean isIdNull() {
        return getId() == null;
    }
}
