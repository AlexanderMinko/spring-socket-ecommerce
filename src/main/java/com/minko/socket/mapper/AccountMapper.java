package com.minko.socket.mapper;

import com.minko.socket.dto.LoginResponse;
import com.minko.socket.dto.RegistrationRequest;
import com.minko.socket.dto.RegistrationResponse;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "password", source = "bCryptedPassword")
    @Mapping(target = "enabled", expression = "java(false)")
    @Mapping(target = "roles", source = "roles")
    Account mapDtoToAccount(
            RegistrationRequest registrationRequest, List<Role> roles, String bCryptedPassword);

    @Mapping(target = "enabled", source = "account.enabled")
    @Mapping(target = "authToken", source = "authToken")
    @Mapping(target = "expiresAt", source = "expiresAt")
    @Mapping(target = "refreshToken", source = "refreshToken")
    LoginResponse mapToLoginResponse(Account account, String authToken, String refreshToken, Instant expiresAt);

    RegistrationResponse mapFromAccountToDto(Account account);

}
