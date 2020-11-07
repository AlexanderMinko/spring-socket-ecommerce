package com.minko.socket.controller;

import com.minko.socket.dto.RegistrationRequestDto;
import com.minko.socket.dto.RegistrationResponseDto;
import com.minko.socket.dto.SocialRequestDto;
import com.minko.socket.dto.SocialResponseDto;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.impl.SocialServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final SocialServiceImpl authService;
    private final AccountService accountService;

    @PostMapping("/google")
    public ResponseEntity<SocialResponseDto> google(@RequestBody SocialRequestDto socialRequestDto) {
        return new ResponseEntity<>(authService.google(socialRequestDto), HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<SocialResponseDto> facebook(@RequestBody SocialRequestDto socialRequestDto) {
        return new ResponseEntity<>(authService.facebook(socialRequestDto), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDto> registrationCustomAccount(
            @RequestBody RegistrationRequestDto registrationRequestDto) {
        return new ResponseEntity<>(accountService.createCustomAccount(registrationRequestDto), HttpStatus.CREATED);
    }
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable String token) {
        accountService.verifyAccount(token);
        return new ResponseEntity<>( "Account activated successfully" , HttpStatus.OK);
    }
}
