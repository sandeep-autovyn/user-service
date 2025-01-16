package com.user.management.exception;

public class UserNameAlreadyExists extends RuntimeException {

    public UserNameAlreadyExists() {
    }

    public UserNameAlreadyExists(String message) {
        super(message);
    }
}
