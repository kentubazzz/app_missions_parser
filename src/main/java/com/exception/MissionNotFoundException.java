package com.exception;

public class MissionNotFoundException extends Exception {
    public MissionNotFoundException(String message) {
        super(message);
    }

    public MissionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
