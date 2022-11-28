package com.example.hospitalreview2.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserJoinResponse {
    private String userName;
    private String email;
}