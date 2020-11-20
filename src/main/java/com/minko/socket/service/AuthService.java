package com.minko.socket.service;

import com.minko.socket.dto.*;

public interface AuthService {

    LoginResponse google(SocialRequest socialRequest);

    LoginResponse facebook(SocialRequest socialRequest);

    RegistrationResponse createCustomAccount(RegistrationRequest registrationRequest);

    void verifyAccount(String token);

    LoginResponse loginAccount(LoginRequest loginRequest);

    LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
