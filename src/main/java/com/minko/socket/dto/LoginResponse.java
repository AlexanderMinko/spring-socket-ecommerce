package com.minko.socket.dto;

import com.minko.socket.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

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
    private List<RoleType> roles;
    private String authToken;
    private String refreshToken;
    private Instant expiresAt;

}
