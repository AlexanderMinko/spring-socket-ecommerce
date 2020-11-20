package com.minko.socket.service.impl;

import com.minko.socket.entity.RefreshToken;
import com.minko.socket.exception.SocketException;
import com.minko.socket.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Captor
    private ArgumentCaptor<String> tokenArgumentCaptor;

    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = new RefreshToken(1L, "token", Instant.now());
    }

    @Test
    void generateRefreshToken() {
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        RefreshToken refreshTokenAct = refreshTokenService.generateRefreshToken();
        assertThat(refreshTokenAct).isEqualTo(refreshToken);
    }

    @Test
    void validateRefreshToken() {
        when(refreshTokenRepository.findByToken(refreshToken.getToken())).thenReturn(Optional.of(refreshToken));
        refreshTokenService.validateRefreshToken(refreshToken.getToken());
        verify(refreshTokenRepository, times(1)).findByToken(tokenArgumentCaptor.capture());
        assertThat(refreshToken.getToken()).isEqualTo(tokenArgumentCaptor.getValue());
        assertThatThrownBy(() -> refreshTokenService.validateRefreshToken("lol")).isInstanceOf(SocketException.class)
                .hasMessage("Refresh token not found - lol");
    }

    @Test
    void deleteRefreshToken() {
        refreshTokenService.deleteRefreshToken(refreshToken.getToken());
        verify(refreshTokenRepository, times(1)).deleteByToken(tokenArgumentCaptor.capture());
        assertThat(refreshToken.getToken()).isEqualTo(tokenArgumentCaptor.getValue());
    }

}