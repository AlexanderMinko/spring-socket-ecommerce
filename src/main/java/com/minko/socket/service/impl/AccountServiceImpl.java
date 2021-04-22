package com.minko.socket.service.impl;

import com.minko.socket.dto.AccountAdminResponse;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Role;
import com.minko.socket.entity.RoleType;
import com.minko.socket.exception.SocketException;
import com.minko.socket.mapper.AccountMapper;
import com.minko.socket.repository.AccountRepository;
import com.minko.socket.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        boolean isExist = accountRepository.existsByEmail(email);
        log.info("In existsByEmail - is account exist: {} by email: {}",
            isExist, email);
        return isExist;
    }

    @Override
    @Transactional(readOnly = true)
    public Account getByEmail(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(
            () -> new SocketException(
                "Account not found with email - " + email));
        log.info("In getByEmail - account: {} found", account);
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public Account getById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(
            () -> new SocketException("Account not found with id - " + id));
        log.info("In getById - account: {} found", account);
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountAdminResponse> getAccounts() {
        List<AccountAdminResponse> accountAdminResponses = accountRepository
            .findAll().stream().map(accountMapper::mapToAccountWithOutPassword)
            .collect(Collectors.toList());
        log.info("In getAccounts - {} accounts found",
            accountAdminResponses.size());
        return accountAdminResponses;
    }

    @Override
    public List<RoleType> getListRolesByAccountEmail(String email) {
        Account account = this.getByEmail(email);
        List<RoleType> roleTypes = account.getRoles().stream()
            .map(Role::getRoleType).collect(Collectors.toList());
        log.info("In getListRolesByAccountEmail - rolesType: {} found",
            roleTypes);
        return roleTypes;
    }

}
