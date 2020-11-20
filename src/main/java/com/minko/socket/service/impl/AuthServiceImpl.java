package com.minko.socket.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.minko.socket.dto.*;
import com.minko.socket.entity.*;
import com.minko.socket.exception.SocketException;
import com.minko.socket.mapper.AccountMapper;
import com.minko.socket.repository.AccountRepository;
import com.minko.socket.repository.RoleRepository;
import com.minko.socket.repository.VerificationTokenRepository;
import com.minko.socket.security.AccountPrincipal;
import com.minko.socket.security.jwt.JwtProvider;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.AuthService;
import com.minko.socket.service.MailService;
import com.minko.socket.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${secretPsw}")
    private String secretPassword;

    @Value("${google.clientId}")
    private String googleId;

    @Value("${verify.token.expired}")
    private long verifyTokenExp;

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final AccountMapper accountMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleIdTokenVerifier.Builder builder;

    public LoginResponse google(SocialRequest socialRequest) {
        builder.setAudience(Collections.singletonList(googleId));
        GoogleIdToken googleIdToken = null;
        try {
            googleIdToken = GoogleIdToken.parse(builder.getJsonFactory(), socialRequest.getToken());
            GoogleIdToken.Payload loadedAccount = googleIdToken.getPayload();
            String email = loadedAccount.getEmail();
            Account account = findOrCreateSocialAccount(email, socialRequest);
            LoginRequest loginRequest = new LoginRequest(account.getEmail(), secretPassword);
            log.info("In google - loginRequest: {} successfully created", loginRequest);
            return loginAccount(loginRequest);
        } catch (IOException e) {
            log.error("In google - Exception while parsing google token. Message: {} ", e.getMessage());
        }
        return null;
    }

    public LoginResponse facebook(SocialRequest socialRequest) {
        Facebook facebook = new FacebookTemplate(socialRequest.getToken());
        Account loadedAccount = facebook.fetchObject("me", Account.class, "email");
        String email = loadedAccount.getEmail();
        Account account = findOrCreateSocialAccount(email, socialRequest);
        LoginRequest loginRequest = new LoginRequest(account.getEmail(), secretPassword);
        log.info("In facebook - loginRequest: {} successfully created", loginRequest);
        return loginAccount(loginRequest);
    }

    @Transactional(readOnly = true)
    public Account findOrCreateSocialAccount(String email, SocialRequest socialRequest) {
        Account account = null;
        if(accountService.existsByEmail(email)) {
            account = accountService.getByEmail(email);
            log.info("In findOrCreateSocialAccount - account: {} successfully logined", account);
        } else {
            RegistrationRequest registrationRequest = new RegistrationRequest(
                    socialRequest.getFirstName(), socialRequest.getLastName(),
                    email, secretPassword, socialRequest.getPhotoUrl());
            account = createSocialAccount(registrationRequest);
            log.info("In findOrCreateSocialAccount - registrationRequest: {} successfully created",
                    registrationRequest);
        }
        return account;
    }

    @Transactional
    public Account createSocialAccount(RegistrationRequest registrationRequest) {
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new SocketException("Role not found with role type - " + RoleType.ROLE_USER));
        List<Role> roles = Collections.singletonList(roleUser);
        String bCryptedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        Account account = accountMapper.mapDtoToAccount(registrationRequest, roles, bCryptedPassword);
        account.setEnabled(true);
        Account savedAccount = accountRepository.save(account);
        log.info("IN createSocialAccount - account: {} successfully created", savedAccount);
        return savedAccount;
    }

    @Transactional
    public RegistrationResponse createCustomAccount(RegistrationRequest registrationRequest) {
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new SocketException("Role not found with role type - " + RoleType.ROLE_USER));
        List<Role> roles = Collections.singletonList(roleUser);
        String bCryptedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        registrationRequest.setPhotoUrl("assets/images/avatar/BasePhoto.jpg");
        Account account = accountMapper.mapDtoToAccount(registrationRequest, roles, bCryptedPassword);
        accountRepository.save(account);
        VerificationToken verificationToken = generateVerificationToken(account);
        mailService.sendMail(new NotificationEmail("Please activate your account",
                account.getEmail(), "Thank you for registration to \"Socket\"! " +
                "Click on the link to activate your account: " +
                "http://localhost:4200/verification/" + verificationToken.getToken()));
        RegistrationResponse registrationResponse = accountMapper.mapFromAccountToDto(account);
        log.info("In createCustomAccount - registrationResponse: {} successfully created", registrationResponse);
        return registrationResponse;
    }

    @Transactional
    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new SocketException("Verification token doesn't exit - " + token));
        String email = verificationToken.getAccount().getEmail();
        Account account = accountService.getByEmail(email);
        account.setEnabled(true);
        Account savedAccount = accountRepository.save(account);
        verificationTokenRepository.delete(verificationToken);
        log.info("In verifyAccount - account: {} is enabled, verificationToken: {} successfully deleted",
                savedAccount, verificationToken);
    }

    @Transactional
    public VerificationToken generateVerificationToken(Account account) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAccount(account);
        verificationToken.setExpirationDate(Instant.now().plusMillis(verifyTokenExp));
        VerificationToken savedVerificationToken = verificationTokenRepository.save(verificationToken);
        log.info("In generateVerificationToken - verificationToken: {} successfully created", verificationToken);
        return savedVerificationToken;
    }

    public LoginResponse loginAccount(LoginRequest loginRequest) {
        String authToken = login(loginRequest);
        String refreshToken = refreshTokenService.generateRefreshToken().getToken();
        Instant expiresAt = Instant.now().plusMillis(jwtProvider.getExpiration());
        LoginResponse loginResponse = accountMapper
                .mapToLoginResponse(getCurrentAccount(), authToken, refreshToken, expiresAt);
        log.info("In loginAccount - loginResponse: {} successfully created", loginResponse);
        return loginResponse;
    }

    @Transactional(readOnly = true)
    public Account getCurrentAccount() {
        AccountPrincipal principal = (AccountPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Account account = accountService.getByEmail(principal.getUsername());
        log.info("In getCurrentAccount account: {} successfully returned", account);
        return account;
    }

    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        log.info("In login - token: {} successfully created at: {}", token, Instant.now());
        return token;
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Account account = accountService.getByEmail(refreshTokenRequest.getEmail());
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String authToken = jwtProvider.generateTokenByEmail(refreshTokenRequest.getEmail());
        Instant expiresAt = Instant.now().plusMillis(jwtProvider.getExpiration());
        LoginResponse loginResponse = accountMapper.mapToLoginResponse(
                account, authToken, refreshTokenRequest.getRefreshToken(), expiresAt);
        log.info("In refreshToken - loginResponse: {} successfully created", loginResponse);
        return loginResponse;
    }
}
