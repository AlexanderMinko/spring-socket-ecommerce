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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final AccountService accountService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Order makeOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.mapFromDtoToOrder(orderRequestDto);
        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = orderRequestDto.getOrderItems()
                .stream().map(orderMapper::mapFromDto)
                .peek(el -> el.setOrder(order))
                .collect(Collectors.toList());
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);
        log.info("In makeOrder - order: {} successfully create with: {} order items",
                savedOrder, savedOrderItems.size());
        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByEmail(String email) {
        Account account = accountService.getByEmail(email);
        List<OrderResponseDto> orderResponseDtos = orderRepository.findByAccountId(account.getId())
                .stream().map(orderMapper::mapToDto).collect(Collectors.toList());
        log.info("In getOrdersByEmail - {} orderResponseDtos found", orderResponseDtos.size());
        return orderResponseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long id) {
        List<OrderItemResponseDto> orderItemResponseDtos = orderItemRepository.findByOrderId(id)
                .stream().map(orderMapper::mapToDtoFromOrderItem)
                .collect(Collectors.toList());
        log.info("In getOrderItemsByOrderId - {} orderItemResponseDtos found", orderItemResponseDtos.size());
        return orderItemResponseDtos;
    }
}
