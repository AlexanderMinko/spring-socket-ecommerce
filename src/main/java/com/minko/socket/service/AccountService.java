package com.minko.socket.service;

import com.minko.socket.dto.RegistrationRequestDto;
import com.minko.socket.dto.RegistrationResponseDto;

public interface AccountService {

    boolean existsByEmail(String email);

    RegistrationResponseDto createCustomAccount(RegistrationRequestDto registrationRequestDto);

    void verifyAccount(String token);
}
