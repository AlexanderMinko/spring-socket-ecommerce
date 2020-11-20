package com.minko.socket.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.minko.socket.dto.LoginResponse;
import com.minko.socket.dto.SocialRequest;
import com.minko.socket.mapper.AccountMapper;
import com.minko.socket.repository.AccountRepository;
import com.minko.socket.repository.RoleRepository;
import com.minko.socket.repository.VerificationTokenRepository;
import com.minko.socket.security.jwt.JwtProvider;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.MailService;
import com.minko.socket.service.RefreshTokenService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private MailService mailService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GoogleIdTokenVerifier.Builder builder;

    @Mock
    GoogleIdToken.Payload payload;

    @Mock
    GoogleIdToken googleIdToken;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @Ignore
    void googleTest() throws IOException {
//        SocialRequest socialRequest = new SocialRequest("fname", "lname", "token", "url");
//        LoginResponse loginResponseExp = new LoginResponse("fname", "lname", "sabaka@sabaka.ua",
//                "url", true, null, "token", "rtoken", Instant.now());
//        String email = "sabaka@sabaka.ua";
//
//        builder.setAudience(anyCollection());
//        when(googleIdToken.getPayload()).thenReturn(payload);
//        when(payload.getEmail()).thenReturn(email);
//
//        LoginResponse loginResponseAct = authService.google(socialRequest);
//        assertThat(loginResponseExp).isEqualTo(loginResponseAct);
//        TODO, I don't know how to test it pls help me!
    }

    @Test
    void facebook() {
        //TODO
    }

    @Test
    void createCustomAccount() {
    }

    @Test
    void verifyAccount() {
    }

    @Test
    void loginAccount() {
    }

    @Test
    void refreshToken() {
    }
}