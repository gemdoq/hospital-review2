package com.example.hospitalreview2.service;

import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import com.example.hospitalreview2.domain.entity.User;
import com.example.hospitalreview2.exception.ErrorCode;
import com.example.hospitalreview2.exception.HospitalReviewAppException;
import com.example.hospitalreview2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.hospitalreview2.exception.ErrorCode.DUPLICATED_USER_NAME;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest userJoinRequest) {
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> { throw new HospitalReviewAppException(DUPLICATED_USER_NAME, "User already exists");
                });

        User user = User.builder()
                .userName(userJoinRequest.getUserName())
                .password(userJoinRequest.getPassword())
                .email(userJoinRequest.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .build();
    }
}