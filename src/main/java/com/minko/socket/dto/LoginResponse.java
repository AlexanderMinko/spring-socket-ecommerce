package com.minko.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private Boolean enabled;
    private String authToken;
    private String refreshToken;
    private Instant expiresAt;

}
