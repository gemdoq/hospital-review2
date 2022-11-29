package com.example.hospitalreview2.service;

import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import com.example.hospitalreview2.domain.dto.UserLoginRequest;
import com.example.hospitalreview2.domain.entity.User;
import com.example.hospitalreview2.exception.HospitalReviewAppException;
import com.example.hospitalreview2.repository.UserRepository;
import com.example.hospitalreview2.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.hospitalreview2.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.koken.secret")
    private String secretKey;
    private long expiredTimeMs = 1000 * 60 * 60;

    public UserDto join(UserJoinRequest userJoinRequest) {
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new HospitalReviewAppException(DUPLICATED_USER_NAME, String.format("UserName:%s", userJoinRequest.getUserName()));
                });

        User savedUser = userRepository.save(userJoinRequest.toEntity(bCryptPasswordEncoder.encode(userJoinRequest.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .build();
    }

    public String login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByUserName(userLoginRequest.getUserName())
                .orElseThrow(() -> new HospitalReviewAppException(NOT_FOUND, String.format("UserName %s is not registered user", userLoginRequest.getUserName())));

        if(!bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new HospitalReviewAppException(INVALID_PASSWORD, "Incorrect password");
        }

        String token = JwtTokenUtil.generateToken(userLoginRequest.getUserName(), secretKey, expiredTimeMs);
        return token;
    }
}