package com.minko.socket.service;

import com.minko.socket.dto.OrderItemResponseDto;
import com.minko.socket.dto.OrderRequestDto;
import com.minko.socket.dto.OrderResponseDto;
import com.minko.socket.entity.Order;

import java.util.List;

public interface OrderService {

    Order makeOrder(OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrdersByEmail(String email);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long id);

}
