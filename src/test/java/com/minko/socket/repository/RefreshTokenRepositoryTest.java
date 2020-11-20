package com.minko.socket.repository;

import com.minko.socket.entity.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshToken refreshToken;
    private RefreshToken savedRefreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = new RefreshToken(null, "token", Instant.now());
        savedRefreshToken = refreshTokenRepository.save(refreshToken);
    }

    @Test
    void findByToken() {
        Optional<RefreshToken> refreshTokenAct = refreshTokenRepository.findByToken(refreshToken.getToken());
        assertThat(refreshTokenAct).isEqualTo(Optional.of(savedRefreshToken));
    }

    @Test
    void deleteByToken() {
        refreshTokenRepository.deleteByToken(refreshToken.getToken());
        assertThat(refreshTokenRepository.findById(savedRefreshToken.getId())).isEmpty();
    }
}