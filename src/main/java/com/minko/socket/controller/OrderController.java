package com.minko.socket.controller;

import com.minko.socket.dto.OrderItemResponseDto;
import com.minko.socket.dto.OrderRequestDto;
import com.minko.socket.dto.OrderResponseDto;
import com.minko.socket.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(@RequestParam String email) {
        return new ResponseEntity<>(orderService.getOrdersByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/make")
    public ResponseEntity<Long> makeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return new ResponseEntity<>(orderService.makeOrder(orderRequestDto).getId(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderItemResponseDto>> getOrdersByOrderId(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderItemsByOrderId(id), HttpStatus.OK);
    }
}
