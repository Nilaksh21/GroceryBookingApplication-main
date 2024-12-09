package com.gb.um.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.um.model.request.LoginRequest;
import com.gb.um.model.response.AuthenticationResponse;
import com.gb.um.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login() throws Exception {

        String sampleToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDY4NjIxNDEsImV4cCI6MTcwNjk0ODU0MX0.HpKPKelYmRAP4xpROcnpl2HM2QAn7CTZbNkbwqdpZq8";

        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("12345")
                .build();

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(sampleToken)
                .build();

        //json
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        //mocking
        Mockito.when(authenticationService.authenticate(loginRequest)).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .content(loginRequestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(sampleToken));

    }
}