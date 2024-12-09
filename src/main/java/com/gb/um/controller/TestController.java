package com.gb.um.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Test Api")
public class TestController {


    @GetMapping("/hello")
    @Operation(description = "Hello test api.")
    public ResponseEntity<String> sayHello() {
        log.info("Hello..!");
        return new ResponseEntity<>("Hello from Grocery Booking App !", HttpStatus.OK);
    }

    @GetMapping("/auth-check")
    @Operation(description = "Authorized api access only.")
    public ResponseEntity<String> authCheck() {
        log.info("Authorization Checked !");
        return new ResponseEntity<>("Auth Check from Grocery Booking App !", HttpStatus.OK);
    }

}
