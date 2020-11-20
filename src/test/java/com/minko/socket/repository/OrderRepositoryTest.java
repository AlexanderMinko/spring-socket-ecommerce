package com.minko.socket.repository;

import com.minko.socket.entity.Account;
import com.minko.socket.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findByAccountId() {
        Account account = new Account(null, "fname", "lname", "sabaka@sabaka.ua",
                "axe123", Instant.now(), true, "url", null);
        Order order1 = new Order(null, Instant.now(), account);
        Order order2 = new Order(null, Instant.now(), account);
        Account savedAccount = accountRepository.save(account);
        List<Order> savedOrders = orderRepository.saveAll(Arrays.asList(order1, order2));
        assertThat(orderRepository.findByAccountId(savedAccount.getId())).isEqualTo(savedOrders);
        assertThat(orderRepository.findById(order1.getId())).isEqualTo(Optional.of(order1));
    }

}