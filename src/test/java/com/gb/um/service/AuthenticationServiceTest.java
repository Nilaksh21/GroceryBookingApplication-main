package com.gb.um.service;

import com.gb.um.model.request.LoginRequest;
import com.gb.um.model.response.AuthenticationResponse;
import com.gb.um.repo.UserInfoRepository;
import com.gb.um.repo.entity.Role;
import com.gb.um.repo.entity.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserInfoService userInfoService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void authenticate() {

        String email = "admin@gmail.com";
        String password = "12345";
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDY4NjIxNDEsImV4cCI6MTcwNjk0ODU0MX0.HpKPKelYmRAP4xpROcnpl2HM2QAn7CTZbNkbwqdpZq8";

        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        UserDetails userInfo = UserInfo.builder()
                .email(email)
                .password(password)
                .role(Role.ADMIN)
                .build();

        Authentication authentication = Mockito.mock(Authentication.class);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);


        //mocking
        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken))
                .thenReturn(authentication);

        Mockito.when(userInfoService.loadUserByUsername(email)).thenReturn(userInfo);

        Mockito.when(jwtService.generateToken(userInfo)).thenReturn(jwtToken);

        //call the method
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(loginRequest);

        assertEquals(jwtToken, authenticationResponse.getToken());

    }
}