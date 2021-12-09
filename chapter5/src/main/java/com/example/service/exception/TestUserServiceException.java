package com.example.service.exception;

public class TestUserServiceException extends RuntimeException {
    public TestUserServiceException() {
    }

    public TestUserServiceException(String message) {
        super(message);
    }

    public TestUserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
