package com.minko.socket.service;

import com.minko.socket.dto.SocialRequestDto;
import com.minko.socket.dto.SocialResponseDto;

public interface SocialService {

    SocialResponseDto google(SocialRequestDto socialRequestDto);

    SocialResponseDto facebook(SocialRequestDto socialRequestDto);

}
