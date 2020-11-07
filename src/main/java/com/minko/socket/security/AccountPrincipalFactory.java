package com.minko.socket.security;

import com.minko.socket.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class AccountPrincipalFactory {
    public static AccountPrincipal create(Account account) {
        List<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleType().name()))
                .collect(Collectors.toList());
        return new AccountPrincipal(account.getEmail(), account.getPassword(), authorities);
    }
}
