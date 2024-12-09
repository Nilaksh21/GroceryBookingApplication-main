package com.gb.um.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ApplicationConfigurationTest {

    private final ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();

    @Test
    void testPasswordEncoderBean() {
        // Given
        PasswordEncoder passwordEncoder = applicationConfiguration.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
}