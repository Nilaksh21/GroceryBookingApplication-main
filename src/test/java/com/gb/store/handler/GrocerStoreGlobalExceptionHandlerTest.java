package com.gb.store.handler;

import com.gb.store.exception.GroceryNotFoundException;
import com.gb.store.exception.InsufficientInventoryException;
import com.gb.um.model.error.ErrorModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class GrocerStoreGlobalExceptionHandlerTest {

    private GrocerStoreGlobalExceptionHandler exceptionHandler = new GrocerStoreGlobalExceptionHandler();

    @Test
    void handleGroceryNotFoundException() {
        GroceryNotFoundException exception = new GroceryNotFoundException("Grocery not found");

        // call handler method
        ResponseEntity<ErrorModel> responseEntity = exceptionHandler.handlerGroceryNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Grocery not found", responseEntity.getBody().getMessage());
    }

    @Test
    void handleInsufficientInventoryException() {
        InsufficientInventoryException exception = new InsufficientInventoryException("Insufficient inventory");

        // call handler method
        ResponseEntity<ErrorModel> responseEntity = exceptionHandler.handlerInsufficientInventoryException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Insufficient inventory", responseEntity.getBody().getMessage());
    }
}