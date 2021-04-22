package com.minko.socket.mapper;

import com.minko.socket.dto.AccountAdminResponse;
import com.minko.socket.dto.LoginResponse;
import com.minko.socket.dto.RegistrationRequest;
import com.minko.socket.dto.RegistrationResponse;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Role;
import com.minko.socket.entity.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    private RegistrationResponse registrationResponse;
    private List<RoleType> roleTypes;
    private Account account;
    private String bCryptedPassword;
    private List<Role> roles;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapperImpl();
        roles = Collections.singletonList(new Role(1L, RoleType.ROLE_USER));
        bCryptedPassword = "123";
        roleTypes = Collections.singletonList(RoleType.ROLE_USER);
        account = new Account(null, "firstName", "lastName", "alex@i.ua", bCryptedPassword,
                Instant.now(), false, "url", roles);
    }

    @Test
    void mapDtoToAccountTest() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                account.getFirstName(), account.getLastName(),
                account.getEmail(), account.getPassword(), account.getPhotoUrl());
        Account accountAct = accountMapper.mapDtoToAccount(registrationRequest, roles, bCryptedPassword);
        assertThat(accountAct).usingRecursiveComparison().ignoringFields("createdDate").isEqualTo(account);
        log.info("In mapDtoToAccount - accountAct: {}", accountAct);
        log.info("In mapDtoToAccount - isEqualTo: {}", account);
    }

    @Test
    void mapToLoginResponseTest() {
        String refreshToken = "refreshToken";
        String authToken = "token";
        LoginResponse loginResponse = new LoginResponse(account.getFirstName(), account.getLastName(), account.getEmail(), account.getPhotoUrl(),
                account.getEnabled(), roleTypes, authToken, refreshToken, null);
        LoginResponse loginResponseAct = accountMapper.mapToLoginResponse(account, authToken, refreshToken, null);
        assertThat(loginResponseAct).isEqualTo(loginResponse);
    }

    @Test
    void mapFromAccountToDtoTest() {
        RegistrationResponse registrationResponse = new RegistrationResponse(account.getFirstName(), account.getLastName(), account.getEmail());
        RegistrationResponse registrationResponseAct = accountMapper.mapFromAccountToDto(account);
        assertThat(registrationResponseAct).isEqualTo(registrationResponse);
    }

    @Test
    void mapToAccountWithOutPasswordTest() {
        AccountAdminResponse accountAdminResponse = new AccountAdminResponse(account.getId(), account.getFirstName(), account.getLastName(),
                account.getEmail(), null, account.getPhotoUrl(), account.getEnabled(), roleTypes);
        AccountAdminResponse accountAdminResponseAct = accountMapper.mapToAccountWithOutPassword(account);
        assertThat(accountAdminResponseAct).usingRecursiveComparison().ignoringFields("createdDate").isEqualTo(accountAdminResponse);
    }

    @Test
    void getSimpleListOfRolesTest() {
        List<RoleType> roleTypesAct = accountMapper.getSimpleListOfRoles(account);
        assertThat(roleTypesAct).isEqualTo(roleTypes);
    }
}