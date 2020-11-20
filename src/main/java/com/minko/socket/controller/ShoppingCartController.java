package com.minko.socket.controller;

import com.minko.socket.dto.CartItem;
import com.minko.socket.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {

    private CartService cartService;

    @GetMapping("/{serializedShoppingCart}")
    public ResponseEntity<List<CartItem>> getShoppingCart(@PathVariable String serializedShoppingCart) {
        return new ResponseEntity<>(cartService.getCartItems(serializedShoppingCart), HttpStatus.OK);
    }
}
