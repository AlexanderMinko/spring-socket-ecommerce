package com.minko.socket.service.impl;

import com.minko.socket.dto.OrderItemResponseDto;
import com.minko.socket.dto.OrderRequestDto;
import com.minko.socket.dto.OrderResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Order;
import com.minko.socket.entity.OrderItem;
import com.minko.socket.mapper.OrderMapper;
import com.minko.socket.repository.OrderItemRepository;
import com.minko.socket.repository.OrderRepository;
import com.minko.socket.service.AccountService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderItem orderItem;
    private Account account;
    private OrderRequestDto orderRequestDto;
    private OrderResponseDto orderResponseDto;
    private OrderItemResponseDto orderItemResponseDto;

    @BeforeEach
    void setUp() {
        order = new Order(1L, Instant.now(), account);
        orderItem = new OrderItem(1L, 1, order, null);
        account = new Account(1L, "fname", "lname", "sabaka@saka.ua", "password",
                Instant.now(), true, "url",null);
        com.minko.socket.dto.OrderItem orderItemDto = new com.minko.socket.dto.OrderItem(1, 1L);
        orderRequestDto = new OrderRequestDto("mail@i.a", Collections.singletonList(orderItemDto));
        orderResponseDto = new OrderResponseDto(1L, Date.from(Instant.now()));
        orderItemResponseDto = new OrderItemResponseDto(1L, "name", "url", "desc", 12.12, 1);
    }

    @Test
    @DisplayName("Should save order and order items, return order")
    void makeOrder() {
        when(orderMapper.mapFromDtoToOrder(any(OrderRequestDto.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.mapFromDto(any(com.minko.socket.dto.OrderItem.class))).thenReturn(orderItem);
        when(orderItemRepository.saveAll(anyIterable())).thenReturn(Collections.singletonList(orderItem));
        Order orderAct = orderService.makeOrder(orderRequestDto);
        assertThat(orderAct).isEqualTo(order);
    }

    @Test
    @DisplayName("Should return list of orders by account email")
    void getOrdersByEmail() {
        when(accountService.getByEmail(anyString())).thenReturn(account);
        when(orderRepository.findByAccountId(anyLong())).thenReturn(Collections.singletonList(order));
        when(orderMapper.mapToDto(any(Order.class))).thenReturn(orderResponseDto);
        List<OrderResponseDto> orderResponseDtosAct = orderService.getOrdersByEmail("sabaka@saka.ua");
        assertThat(orderResponseDtosAct).isEqualTo(Collections.singletonList(orderResponseDto));
    }

    @Test
    @DisplayName("Should return list of order items by order id")
    void getOrderItemsByOrderId() {
        when(orderItemRepository.findByOrderId(anyLong())).thenReturn(Collections.singletonList(orderItem));
        when(orderMapper.mapToDtoFromOrderItem(any(OrderItem.class))).thenReturn(orderItemResponseDto);
        List<OrderItemResponseDto> orderItemResponseDtosAct = orderService.getOrderItemsByOrderId(1L);
        assertThat(orderItemResponseDtosAct).isEqualTo(Collections.singletonList(orderItemResponseDto));
    }
}