package com.telerikacademy.web.beertagprojects.exceptions;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String type, String attribure, String value) {
        super(String.format("%s with %s %s already exists.", type, attribure, value));
    }
}
