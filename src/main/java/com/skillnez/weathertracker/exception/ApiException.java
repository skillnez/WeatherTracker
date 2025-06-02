package com.skillnez.weathertracker.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
