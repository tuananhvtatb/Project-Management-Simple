package com.vti.company.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String exception) {
        super(exception);
    }
}
