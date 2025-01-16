package com.user.management.exception;

public class RandomUserException extends  RuntimeException {
    private String statusCode;
    public RandomUserException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;

    }

    public RandomUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
