package com.gb.um.service;

import com.gb.um.repo.entity.Role;
import com.gb.um.repo.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDY4NjIxNDEsImV4cCI6MTcwNjk0ODU0MX0.HpKPKelYmRAP4xpROcnpl2HM2QAn7CTZbNkbwqdpZq8";

    @Test
    void extractExpiration() {
        assertThrows(ExpiredJwtException.class,()->jwtService.extractExpiration(token).getTime());
    }


    @Test
    void getSigningKey() {
        String algorithm = "HmacSHA384";
        String signedAlgorithm = jwtService.getSigningKey().getAlgorithm();
        assertEquals(algorithm, signedAlgorithm);

    }

    @Test
    void generateToken() {
        UserDetails userDetails = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();
        jwtService.generateToken(userDetails);
    }

    @Test
    void generateTokenExtraClaims() {
        UserDetails userDetails = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();
        jwtService.generateToken(new HashMap<>(), userDetails);
    }

    @Test
    void testGenerateTokenWithExpiry() {
        UserDetails userDetails = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();
        long expiryInHours = 2l;
        jwtService.generateToken(userDetails, expiryInHours);
    }

    @Test
    void buildToken() {
        UserDetails userDetails = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();
        long expiryInHours = 2l;
        jwtService.buildToken(new HashMap<>(), userDetails, expiryInHours);
    }

    @Test
    void isTokenValid() {
        UserDetails userDetails = UserInfo.builder()
                .email("admin@gmail.com")
                .password("12345")
                .role(Role.ADMIN)
                .build();
        assertThrows(ExpiredJwtException.class,()-> jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenExpired() {
        assertThrows(ExpiredJwtException.class, () ->
                jwtService.isTokenExpired(token));
    }
}