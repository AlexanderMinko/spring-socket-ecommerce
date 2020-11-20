package com.minko.socket.service.impl;

import com.minko.socket.dto.CartItem;
import com.minko.socket.entity.Product;
import com.minko.socket.mapper.CartItemMapper;
import com.minko.socket.service.CartService;
import com.minko.socket.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final ProductService productService;
    private final CartItemMapper cartItemMapper;

    @Override
    public List<CartItem> getCartItems(String serializedShoppingCart) {
        if(serializedShoppingCart.trim().length() > 0) {
            List<CartItem> cartItems = new ArrayList<>();
            String[] cookieValues = serializedShoppingCart.split("\\|");
            for(String value : cookieValues) {
                String[] data = value.split("-");
                Long currentProductId = Long.parseLong(data[0]);
                Product current = productService.getProductById(currentProductId);
                CartItem cartItem = cartItemMapper.mapToCartItemFromProduct(current);
                cartItem.setQuantity(Integer.parseInt(data[1]));
                cartItems.add(cartItem);
            }
            log.info("In getCartItems - {} cartItems deserialized", cartItems.size());
            return cartItems;
        } else {
            log.info("In getCartItems - serialized shopping cart is empty!");
            return Collections.emptyList();
        }
    }
}
