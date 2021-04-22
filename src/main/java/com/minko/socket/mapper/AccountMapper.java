package com.minko.socket.mapper;

import com.minko.socket.dto.AccountAdminResponse;
import com.minko.socket.dto.LoginResponse;
import com.minko.socket.dto.RegistrationRequest;
import com.minko.socket.dto.RegistrationResponse;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Role;
import com.minko.socket.entity.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "password", source = "bcryptedPassword")
    @Mapping(target = "enabled", expression = "java(false)")
    @Mapping(target = "roles", source = "roles")
    Account mapDtoToAccount(RegistrationRequest registrationRequest,
        List<Role> roles, String bcryptedPassword);

    @Mapping(target = "enabled", source = "account.enabled")
    @Mapping(target = "authToken", source = "authToken")
    @Mapping(target = "expiresAt", source = "expiresAt")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "roles", expression = "java(getSimpleListOfRoles(account))")
    LoginResponse mapToLoginResponse(Account account, String authToken,
        String refreshToken, Instant expiresAt);

    RegistrationResponse mapFromAccountToDto(Account account);

    @Mapping(target = "roles", expression = "java(getSimpleListOfRoles(account))")
    @Mapping(target = "createdDate", expression = "java(java.util.Date.from(account.getCreatedDate()))")
    AccountAdminResponse mapToAccountWithOutPassword(Account account);

    default List<RoleType> getSimpleListOfRoles(Account account) {
        return account.getRoles().stream().map(Role::getRoleType)
            .collect(Collectors.toList());
    }

}
