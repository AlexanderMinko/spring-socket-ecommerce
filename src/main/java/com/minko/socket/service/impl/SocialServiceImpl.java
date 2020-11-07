package com.minko.socket.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.minko.socket.dto.SocialRequestDto;
import com.minko.socket.dto.SocialResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Role;
import com.minko.socket.entity.RoleType;
import com.minko.socket.repository.AccountRepository;
import com.minko.socket.repository.RoleRepository;
import com.minko.socket.security.jwt.JwtProvider;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.SocialService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialServiceImpl implements SocialService {

    @Value("${secretPsw}")
    private String secretPassword;

    @Value("${google.clientId}")
    private String googleId;

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public SocialResponseDto google(SocialRequestDto socialRequestDto) {
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
                        .setAudience(Collections.singletonList(googleId));
        GoogleIdToken googleIdToken = null;
        try {
            googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), socialRequestDto.getToken());
            GoogleIdToken.Payload loadedAccount = googleIdToken.getPayload();
            String email = loadedAccount.getEmail();
            Account account = findOrCreateSocialAccount(email, socialRequestDto);
            return new SocialResponseDto(login(account));
        } catch (IOException e) {
            throw new SocketException("exception while parsing google token");
        }
    }

    public SocialResponseDto facebook(SocialRequestDto socialRequestDto) {
        Facebook facebook = new FacebookTemplate(socialRequestDto.getToken());
        Account loadedAccount = facebook.fetchObject("me", Account.class, "email");
        String email = loadedAccount.getEmail();
        Account account = findOrCreateSocialAccount(email, socialRequestDto);
        return new SocialResponseDto(login(account));
    }

    @Transactional(readOnly = true)
    private Account findOrCreateSocialAccount(String email, SocialRequestDto socialRequestDto) {
        Account account = null;
        if(accountRepository.existsByEmail(email)) {
            String finalEmail = email;
            account = accountRepository.findByEmail(email).orElseThrow( () ->
                    new SocketException("account not found with email - " + finalEmail));
        } else {
            account = createSocialAccount(
                    email, socialRequestDto.getFirstName(), socialRequestDto.getLastName());
        }
        return account;
    }

    @Transactional
    private Account createSocialAccount(String email, String firstName, String lastName) {
        Account account = new Account();
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new SocketException("Role not found with role type - " + RoleType.ROLE_USER));
        List<Role> roles = Arrays.asList(roleUser);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(secretPassword));
        account.setEnabled(true);
        account.setRoles(roles);
        account.setCreatedDate(Instant.now());
        log.info("IN register - account: {} successfully registered", account);
        return accountRepository.save(account);
    }

    private String login(Account account) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getEmail(), secretPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        log.info("Token created at: {} ", Instant.now());
        return token;
    }
}
