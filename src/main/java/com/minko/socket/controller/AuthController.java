package com.minko.socket.controller;

import com.minko.socket.dto.*;
import com.minko.socket.service.RefreshTokenService;
import com.minko.socket.service.impl.AuthServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthServiceImpl authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> google(@RequestBody SocialRequest socialRequest) {
        return new ResponseEntity<>(authService.google(socialRequest), HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<LoginResponse> facebook(@RequestBody SocialRequest socialRequest) {
        return new ResponseEntity<>(authService.facebook(socialRequest), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> registrationAccount(
            @RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authService.createCustomAccount(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.loginAccount(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authService.refreshToken(refreshTokenRequest), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return new ResponseEntity<>(
                "Refresh token deleted successfully - " + refreshTokenRequest.getRefreshToken(), HttpStatus.OK);
    }

    @GetMapping("/verificate/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>( "Account activated successfully" , HttpStatus.OK);
    }
}
