package com.gb.um.service;

import com.gb.um.model.request.UserRegistrationRequest;
import com.gb.um.repo.UserInfoRepository;
import com.gb.um.repo.entity.Role;
import com.gb.um.repo.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Username : {}",username);
        Optional<UserInfo> user = userInfoRepository.findByEmail(username);
        //throw exception if the username is invalid
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid Username");
        }

        return user.get();
    }

    @Transactional
    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        log.info(" user registration request : email {}, password {}, role {}",
                userRegistrationRequest.getEmail(),
                "*".repeat(userRegistrationRequest.getPassword().length()),
                userRegistrationRequest.getRole() != null ? userRegistrationRequest.getRole().name() : Role.USER.name());

        //user request validation is added through spring boot validator
        var userInfo = UserInfo.builder()
                .firstName(userRegistrationRequest.getFirstName())
                .lastName(userRegistrationRequest.getLastName())
                .email(userRegistrationRequest.getEmail())
                .password(
                        passwordEncoder.encode(
                                userRegistrationRequest.getPassword()
                        )
                )
                .role(
                        userRegistrationRequest.getRole() != null
                                ? userRegistrationRequest.getRole()
                                : Role.USER
                )
                .build();

        userInfoRepository.save(userInfo);

    }

}
