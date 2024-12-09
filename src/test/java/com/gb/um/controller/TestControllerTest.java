package com.gb.um.controller;

import com.gb.um.repo.UserInfoRepository;
import com.gb.um.repo.entity.Role;
import com.gb.um.repo.entity.UserInfo;
import com.gb.um.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    void sayHello() throws Exception {

        String expectedResponse = "Hello from Grocery Booking App !";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/hello")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }

    @Test
    void authCheck() throws Exception {
        String expectedResponse = "Auth Check from Grocery Booking App !";

        UserInfo userInfo = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();


        userInfoRepository.save(userInfo);

        String userJwtToken = jwtService.generateToken(userInfo);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/auth-check")
                        .header("Authorization", "Bearer " + userJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }
}