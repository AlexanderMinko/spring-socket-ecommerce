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
        return accountRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new SocketException("Account not found with email - " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new SocketException("Account not found with id - " +  id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountAdminResponse> getAccounts() {
        return accountRepository.findAll()
                .stream().map(accountMapper::mapToAccountWithOutPassword)
                .collect(Collectors.toList());
    }

    public List<RoleType> getListRolesByAccountEmail(String email) {
        Account account = this.getByEmail(email);
        return account.getRoles()
                .stream().map(Role::getRoleType).collect(Collectors.toList());
    }

}
