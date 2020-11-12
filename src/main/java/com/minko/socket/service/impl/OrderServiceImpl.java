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
import com.minko.socket.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final AccountService accountService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Long makeOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.mapFromDtoToOrder(orderRequestDto);
        Long orderId = orderRepository.save(order).getId();
        log.info(orderRequestDto.getOrderItems().toString());
        List<OrderItem> orderItems = orderRequestDto.getOrderItems()
                .stream().map(orderMapper::mapFromDto).collect(Collectors.toList());
        orderItems.forEach(el -> el.setOrder(order));
        orderItemRepository.saveAll(orderItems);
        return orderId;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByEmail(String email) {
        Account account = accountService.getByEmail(email);
        return orderRepository.findByAccountId(account.getId())
                .stream().map(orderMapper::mapToDto).collect(Collectors.toList());
    }

    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long id) {
        return orderItemRepository.findByOrderId(id)
                .stream().map(orderMapper::mapToDtoFromOrderItem)
                .collect(Collectors.toList());
    }
}
