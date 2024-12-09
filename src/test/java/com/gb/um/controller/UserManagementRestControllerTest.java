package com.gb.um.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.um.model.request.UserRegistrationRequest;
import com.gb.um.repo.entity.Role;
import com.gb.um.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserManagementRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInfoService userInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser() throws Exception {

        //request
        UserRegistrationRequest userRegistrationRequest =
                UserRegistrationRequest.builder()
                        .firstName("first")
                        .lastName("last")
                        .email("userSpecial@gmail.com")
                        .password("12345")
                        .build();

        //request json
        String userRegistrationRequestJson = objectMapper.writeValueAsString(userRegistrationRequest);

        //mocking
        Mockito.doNothing().when(userInfoService).registerUser(userRegistrationRequest);

        //rest api call
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/register")
                        .content(userRegistrationRequestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));

    }
}