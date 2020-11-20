package com.minko.socket.repository;

import com.minko.socket.entity.Order;
import com.minko.socket.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findByOrderId() {
        Order order = new Order(null, Instant.now(), null);
        OrderItem orderItem1 = new OrderItem(null, 1, order, null);
        OrderItem orderItem2 = new OrderItem(null, 1, order, null);
        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);
        assertThat(orderItemRepository.findByOrderId(savedOrder.getId())).isEqualTo(savedOrderItems);
    }

}