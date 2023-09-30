package com.transaccionesbancarias.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PocketNotFoundException extends RuntimeException {
    public PocketNotFoundException(String message) {
        super(message);
    }
}
