package com.mylab.spring.coredemo.dao.exception;

public class EntityNotFoundException extends DaoException {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
