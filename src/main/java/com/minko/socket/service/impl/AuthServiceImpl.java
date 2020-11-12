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

    public LoginResponse google(SocialRequest socialRequest) {
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
                        .setAudience(Collections.singletonList(googleId));
        GoogleIdToken googleIdToken = null;
        try {
            googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), socialRequest.getToken());
            GoogleIdToken.Payload loadedAccount = googleIdToken.getPayload();
            String email = loadedAccount.getEmail();
            Account account = findOrCreateSocialAccount(email, socialRequest);
            return loginAccount(new LoginRequest(account.getEmail(), secretPassword));
        } catch (IOException e) {
            throw new SocketException("exception while parsing google token");
        }
    }

    public LoginResponse facebook(SocialRequest socialRequest) {
        Facebook facebook = new FacebookTemplate(socialRequest.getToken());
        Account loadedAccount = facebook.fetchObject("me", Account.class, "email");
        String email = loadedAccount.getEmail();
        Account account = findOrCreateSocialAccount(email, socialRequest);
        return loginAccount(new LoginRequest(account.getEmail(), secretPassword));
    }

    @Transactional(readOnly = true)
    private Account findOrCreateSocialAccount(String email, SocialRequest socialRequest) {
        Account account = null;
        if(accountService.existsByEmail(email)) {
            account = accountService.getByEmail(email);
        } else {
            account = createSocialAccount(new RegistrationRequest(
                    socialRequest.getFirstName(), socialRequest.getLastName(),
                    email, secretPassword, socialRequest.getPhotoUrl()));
        }
        return account;
    }

    @Transactional
    private Account createSocialAccount(RegistrationRequest registrationRequest) {
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new SocketException("Role not found with role type - " + RoleType.ROLE_USER));
        List<Role> roles = Arrays.asList(roleUser);
        String bCryptedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        Account account = accountMapper.mapDtoToAccount(registrationRequest, roles, bCryptedPassword);
        account.setEnabled(true);
        log.info("IN register - account: {} successfully registered", account);
        return accountRepository.save(account);
    }

    @Transactional
    public RegistrationResponse createCustomAccount(RegistrationRequest registrationRequest) {
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new SocketException("Role not found with role type - " + RoleType.ROLE_USER));
        List<Role> roles = Arrays.asList(roleUser);
        String bCryptedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        registrationRequest.setPhotoUrl("assets/images/avatar/BasePhoto.jpg");
        Account account = accountMapper.mapDtoToAccount(registrationRequest, roles, bCryptedPassword);
        accountRepository.save(account);
        VerificationToken verificationToken = generateVerificationToken(account);
        mailService.sendMail(new NotificationEmail("Please activate your account",
                account.getEmail(), "Thank you for registration to \"Socket\"! " +
                "Click on the link to activate your account: " +
                "http://localhost:8080/api/auth/accountVerification/" + verificationToken.getToken()));
        return accountMapper.mapFromAccountToDto(account);
    }

    @Transactional
    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new SocketException("Verification token doesn't exit - " + token));
        String email = verificationToken.getAccount().getEmail();
        Account account = accountService.getByEmail(email);
        account.setEnabled(true);
        accountRepository.save(account);
        verificationTokenRepository.delete(verificationToken);
    }

    @Transactional
    private VerificationToken generateVerificationToken(Account account) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAccount(account);
        verificationToken.setExpirationDate(Instant.now().plusMillis(verifyTokenExp));
        return verificationTokenRepository.save(verificationToken);
    }

    public LoginResponse loginAccount(LoginRequest loginRequest) {
        String authToken = login(loginRequest);
        String refreshToken = refreshTokenService.generateRefreshToken().getToken();
        Instant expiresAt = Instant.now().plusMillis(jwtProvider.getExpiration());
        return accountMapper.mapToLoginResponse(getCurrentAccount(), authToken, refreshToken, expiresAt);
    }

    @Transactional(readOnly = true)
    private Account getCurrentAccount() {
        AccountPrincipal principal = (AccountPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return accountService.getByEmail(principal.getUsername());
    }

    private String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        log.info("Token created at: {} ", Instant.now());
        return token;
    }

    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Account account = accountService.getByEmail(refreshTokenRequest.getEmail());
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String authToken = jwtProvider.generateTokenByEmail(refreshTokenRequest.getEmail());
        Instant expiresAt = Instant.now().plusMillis(jwtProvider.getExpiration());
        return accountMapper.mapToLoginResponse(
                account, authToken, refreshTokenRequest.getRefreshToken(), expiresAt);
    }
}
