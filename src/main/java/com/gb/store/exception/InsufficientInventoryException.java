package com.gb.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InsufficientInventoryException extends RuntimeException{

    public InsufficientInventoryException() {
    }

    public InsufficientInventoryException(String message) {
        super(message);
    }
}
