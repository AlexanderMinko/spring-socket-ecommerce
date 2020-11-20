package com.minko.socket.service.impl;

import com.minko.socket.dto.CartItem;
import com.minko.socket.entity.Category;
import com.minko.socket.entity.Producer;
import com.minko.socket.entity.Product;
import com.minko.socket.mapper.CartItemMapper;
import com.minko.socket.service.CartService;
import com.minko.socket.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class CartItemImplTest {

    @Mock
    private ProductService productService;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("Should return deserialized list of CartItems")
    void getCartItemsTest() {
        CartItem cartItem1 = new CartItem(1L, "name1", "url1", "desc1", 1234.56, 1);
        CartItem cartItem2 = new CartItem(2L, "name2", "url2", "desc2", 1234.56, 2);

        List<CartItem> cartItemsExp = new ArrayList<>();
        cartItemsExp.add(cartItem1);
        cartItemsExp.add(cartItem2);

        Category category = new Category(1L, "book", 25);
        Producer producer = new Producer(1L ,"me");
        Product product1 = new Product(1L, "name1", "desc1", "url1", 1234.56, category, producer);
        Product product2 = new Product(2L, "name1", "desc2", "url2", 1234.56, category, producer);

        Mockito.when(productService.getProductById(1L)).thenReturn(product1);
        Mockito.when(productService.getProductById(2L)).thenReturn(product2);
        Mockito.when(cartItemMapper.mapToCartItemFromProduct(product1)).thenReturn(cartItem1);
        Mockito.when(cartItemMapper.mapToCartItemFromProduct(product2)).thenReturn(cartItem2);

        List<CartItem> cartItemsActual = cartService.getCartItems("1-1|2-2");
        List<CartItem> cartItemsActualEmpty = cartService.getCartItems("");
        CartItem cartItemActual1 = cartItemsActual.get(0);
        CartItem cartItemActual2 = cartItemsActual.get(1);

        assertThat(cartItemsExp).isEqualTo(cartItemsActual);
        assertThat(cartItem1).isEqualTo(cartItemActual1);
        assertThat(cartItem2).isEqualTo(cartItemActual2);
        assertThat(Collections.emptyList()).isEqualTo(cartItemsActualEmpty);
    }

}