package com.mylab.spring.coredemo.dao.exception;

public class IllegalDaoRequestException extends DaoException {

    public IllegalDaoRequestException() {
        super();
    }

    public IllegalDaoRequestException(String message) {
        super(message);
    }

    public IllegalDaoRequestException(Throwable cause) {
        super(cause);
    }
}
