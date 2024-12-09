package com.gb.um.service;

import com.gb.um.model.request.UserRegistrationRequest;
import com.gb.um.repo.UserInfoRepository;
import com.gb.um.repo.entity.Role;
import com.gb.um.repo.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    void loadUserByUsername() {
        String username = "user@gmail.com";

        UserInfo userInfo = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        //mocking
        Mockito.when(userInfoRepository.findByEmail(username)).thenReturn(Optional.of(userInfo));

        //method call

        UserDetails userDetailsResponse = userInfoService.loadUserByUsername(username);

        assertEquals(username, userDetailsResponse.getUsername());
        assertEquals("12345", userDetailsResponse.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String username = "invalid@gmail.com";

        //mocking
        Mockito.when(userInfoRepository.findByEmail(username)).thenReturn(Optional.empty());

        //method call
        assertThrows(UsernameNotFoundException.class,
                () -> userInfoService.loadUserByUsername(username));
    }

    @Test
    void registerUser() {

        UserRegistrationRequest userRegistrationRequest =
                UserRegistrationRequest.builder()
                        .firstName("first")
                        .lastName("last")
                        .email("user@gmail.com")
                        .password("12345")
                        .role(Role.USER)
                        .build();

        UserInfo userInfo =
                UserInfo.builder()
                        .firstName("first")
                        .lastName("last")
                        .email("user@gmail.com")
                        .password("abcdef123456789")
                        .role(Role.USER)
                        .build();

        //mocking
        Mockito.when(passwordEncoder.encode("12345")).thenReturn("abcdef123456789");
        Mockito.when(userInfoRepository.save(userInfo)).thenReturn(userInfo);

        userInfoService.registerUser(userRegistrationRequest);
    }
}