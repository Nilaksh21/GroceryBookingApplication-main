package com.gb.um.service;

import com.gb.um.model.request.LoginRequest;
import com.gb.um.model.response.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthenticationService {

    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            UserInfoService userInfoService,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse authenticate(LoginRequest loginRequest) {
        log.info("Login Request : Username {}, Password {}", loginRequest.getEmail(),"*".repeat(loginRequest.getPassword().length()));
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        //once the user is authenticated fetch user details
        var userInfo = userInfoService.loadUserByUsername(loginRequest.getEmail());

        //get the JWT Token from user details
        String jwtToken = jwtService.generateToken(userInfo);
        log.info("JWT Token : {}",jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
