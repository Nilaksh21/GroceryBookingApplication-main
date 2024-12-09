package com.gb.um.handlers;

import com.gb.um.exception.UserNotFoundException;
import com.gb.um.model.error.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;


@ControllerAdvice
public class UserManagementExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorModel> handleUserNotFound(UserNotFoundException ex) {

        ErrorModel errorModel = ErrorModel.builder()
                .message(ex.getLocalizedMessage())
                .cause(String.valueOf(ex.getCause()))
                .date(new Date())
                .status(HttpStatus.NOT_FOUND.name())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorModel> handleIllegalArgumentException(IllegalArgumentException ex) {

        ErrorModel errorModel = ErrorModel.builder()
                .message(ex.getLocalizedMessage())
                .cause(String.valueOf(ex.getCause()))
                .date(new Date())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

}
