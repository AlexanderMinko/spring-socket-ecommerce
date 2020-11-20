package com.minko.socket.service;

import com.minko.socket.dto.CartItem;

import java.util.List;

public interface CartService {

    List<CartItem> getCartItems(String serializedShoppingCart);
}
