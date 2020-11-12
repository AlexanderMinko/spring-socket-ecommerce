package com.minko.socket.service;

import com.minko.socket.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken();

    void validateRefreshToken(String token);

    void deleteRefreshToken(String token);
}
