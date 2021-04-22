package com.minko.socket.security;

import com.minko.socket.entity.Account;
import com.minko.socket.repository.AccountRepository;
import com.minko.socket.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("email not found - " + email));
        return AccountPrincipalFactory.create(account);
    }
}
