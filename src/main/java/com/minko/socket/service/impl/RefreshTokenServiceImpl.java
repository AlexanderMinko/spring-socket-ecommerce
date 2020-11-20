package com.minko.socket.service.impl;

import com.minko.socket.entity.RefreshToken;
import com.minko.socket.exception.SocketException;
import com.minko.socket.repository.RefreshTokenRepository;
import com.minko.socket.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
        log.info("In generateRefreshToken - refreshToken: {}, successfully created at {}",
                savedRefreshToken, Instant.now());
        return savedRefreshToken;
    }

    @Override
    @Transactional(readOnly = true)
    public void validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SocketException("Refresh token not found - " + token));
        log.info("In validateRefreshToken - refreshToken: {} successfully validated", refreshToken);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        log.info("In deleteRefreshToken - refreshToken with token: {} successfully deleted", token);
    }
}
