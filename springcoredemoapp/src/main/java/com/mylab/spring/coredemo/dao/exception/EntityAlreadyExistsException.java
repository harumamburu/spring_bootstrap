package com.mylab.spring.coredemo.dao.exception;

public class EntityAlreadyExistsException extends DaoException {

    public EntityAlreadyExistsException() {
        super();
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
