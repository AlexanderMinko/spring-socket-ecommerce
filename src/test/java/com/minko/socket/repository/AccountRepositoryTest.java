package com.minko.socket.repository;

import com.minko.socket.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account account;

    private Account savedAccount;

    @BeforeEach
    void init() {
        account = new Account(null, "fname", "lname", "sabaka1@sabak.ua",
                "axe1234", Instant.now(), true, "url", null);
        savedAccount = accountRepository.save(account);
    }

    @Test
    void findByEmail() {
        assertThat(accountRepository.findByEmail(account.getEmail())).isEqualTo(Optional.of(savedAccount));
    }

    @Test
    void existsByEmail() {
        assertThat(accountRepository.existsByEmail(account.getEmail())).isTrue();
    }
}