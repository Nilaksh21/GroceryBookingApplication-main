package com.gb.um.handlers;

import com.gb.um.exception.UserNotFoundException;
import com.gb.um.model.error.ErrorModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserManagementExceptionHandlerTest {

    private UserManagementExceptionHandler handler = new UserManagementExceptionHandler();

    @Test
    void handleUserNotFound() {
        UserNotFoundException userNotFoundException = new UserNotFoundException("User not found");

        ResponseEntity<ErrorModel> responseEntity = handler.handleUserNotFound(userNotFoundException);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        //assert error model
        ErrorModel errorModel = responseEntity.getBody();
        assertNotNull(errorModel);
        assertEquals("User not found", errorModel.getMessage());
        assertNotNull(errorModel.getDate());
        assertEquals(HttpStatus.NOT_FOUND.name(), errorModel.getStatus());
    }

    @Test
    void handleIllegalArgumentException() {

        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Invalid argument");
        ResponseEntity<ErrorModel> responseEntity = handler.handleIllegalArgumentException(illegalArgumentException);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorModel errorModel = responseEntity.getBody();
        assertNotNull(errorModel);
        assertEquals("Invalid argument", errorModel.getMessage());
        assertNotNull(errorModel.getDate());
        assertEquals(HttpStatus.BAD_REQUEST.name(), errorModel.getStatus());
    }
}