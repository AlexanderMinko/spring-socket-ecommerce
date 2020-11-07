package com.minko.socket.service.impl;

import com.minko.socket.dto.RegistrationRequestDto;
import com.minko.socket.dto.RegistrationResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.NotificationEmail;
import com.minko.socket.entity.VerificationToken;
import com.minko.socket.mapper.AccountMapper;
import com.minko.socket.repository.AccountRepository;
import com.minko.socket.repository.VerificationTokenRepository;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Value("${secretPsw}")
    private String secretPassword;

    @Value("${verify.token.expired}")
    private long verifyTokenExp;

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Transactional
    public RegistrationResponseDto createCustomAccount(RegistrationRequestDto registrationRequestDto) {
        Account account = accountMapper.mapDtoToAccount(registrationRequestDto);
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
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new SocketException("Account not found with email - " + email));
        account.setEnabled(true);
        accountRepository.save(account);
        verificationTokenRepository.delete(verificationToken);
    }

    private VerificationToken generateVerificationToken(Account account) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAccount(account);
        verificationToken.setExpirationDate(Instant.now().plusMillis(verifyTokenExp));
        return verificationTokenRepository.save(verificationToken);
    }

}
