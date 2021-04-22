package com.minko.socket.service.impl;

import com.minko.socket.dto.AccountAdminResponse;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Role;
import com.minko.socket.entity.RoleType;
import com.minko.socket.exception.SocketException;
import com.minko.socket.mapper.AccountMapper;
import com.minko.socket.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountAdminResponse accountAdminResponse;

    @BeforeEach
    void setUp() {
        account = new Account(1L, "fname", "lname", "me@i.us", "text",
                Instant.now(), true, "url", Collections.singletonList(new Role(1L, RoleType.ROLE_USER)));
        accountAdminResponse = new AccountAdminResponse(1L, "fname", "lname", "me@i.us",
                Date.from(Instant.now()), "url", true, null);
    }

    @Test
    @DisplayName("Should check account is exist or no and return boolean")
    void existsByEmailTest() {
        when(accountRepository.existsByEmail(account.getEmail())).thenReturn(true);
        boolean isExistAct = accountService.existsByEmail(account.getEmail());
        assertThat(isExistAct).isTrue();
    }

    @Test
    @DisplayName("Should return account by email or throw exception")
    void getByEmailTest() {
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        Account accountAct = accountService.getByEmail(account.getEmail());
        assertThat(accountAct).isEqualTo(account);
        assertThatThrownBy(() -> accountService.getByEmail("me@i.ua")).isInstanceOf(SocketException.class)
                .hasMessage("Account not found with email - " + "me@i.ua");
    }

    @Test
    @DisplayName("Should return account by id or throw exception")
    void getByIdTest() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        Account accountAct = accountService.getById(account.getId());
        assertThat(accountAct).isEqualTo(account);
        assertThatThrownBy(() -> accountService.getById(0L)).isInstanceOf(SocketException.class)
                .hasMessage("Account not found with id - " + 0L);
    }

    @Test
    @DisplayName("Should return list of accountAdminResponses")
    void getAccountsTest() {
        when(accountRepository.findAll()).thenReturn(Collections.singletonList(account));
        when(accountMapper.mapToAccountWithOutPassword(account)).thenReturn(accountAdminResponse);
        List<AccountAdminResponse> accountAdminResponsesAct = accountService.getAccounts();
        assertThat(accountAdminResponsesAct).isEqualTo(Collections.singletonList(accountAdminResponse));
    }

    @Test
    @DisplayName("Should return list of RoleTypes by account email")
    void getListRolesByAccountEmailTest() {
        when(accountRepository.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        Account accountAct = accountService.getByEmail(account.getEmail());
        List<RoleType> roleTypesAct = accountService.getListRolesByAccountEmail(account.getEmail());
        List<RoleType> roleTypes = accountAct.getRoles().stream().map(Role::getRoleType).collect(Collectors.toList());
        assertThat(roleTypesAct).isEqualTo(roleTypes);
    }
}