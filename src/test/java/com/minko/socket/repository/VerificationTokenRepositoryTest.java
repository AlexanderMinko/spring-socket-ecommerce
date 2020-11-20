package com.minko.socket.repository;

import com.minko.socket.entity.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private VerificationToken savedVerificationToken;

    @BeforeEach
    void setUp() {
        VerificationToken verificationToken = new VerificationToken(null, "token", Instant.now(), null);
        savedVerificationToken = verificationTokenRepository.save(verificationToken);
    }

    @Test
    void findByToken() {
        Optional<VerificationToken> verificationTokenAct =
                verificationTokenRepository.findByToken(savedVerificationToken.getToken());
        assertThat(verificationTokenAct).isEqualTo(Optional.of(savedVerificationToken));
    }
}