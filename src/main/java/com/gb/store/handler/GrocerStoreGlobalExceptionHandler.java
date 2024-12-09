package com.gb.store.handler;

import com.gb.store.exception.GroceryNotFoundException;
import com.gb.store.exception.InsufficientInventoryException;
import com.gb.um.model.error.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GrocerStoreGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GroceryNotFoundException.class)
    public ResponseEntity<ErrorModel> handlerGroceryNotFoundException(GroceryNotFoundException ex) {

        ErrorModel errorModel = ErrorModel.builder()
                .message(ex.getLocalizedMessage())
                .cause(String.valueOf(ex.getCause()))
                .date(new Date())
                .status(HttpStatus.NOT_FOUND.name())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ErrorModel> handlerInsufficientInventoryException(InsufficientInventoryException ex) {

        ErrorModel errorModel = ErrorModel.builder()
                .message(ex.getLocalizedMessage())
                .cause(String.valueOf(ex.getCause()))
                .date(new Date())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
