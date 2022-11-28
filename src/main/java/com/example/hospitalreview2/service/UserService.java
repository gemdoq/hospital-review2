package com.example.hospitalreview2.service;

import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserDto join(UserJoinRequest userJoinRequest) {
        return new UserDto("", "", "");
    }
}