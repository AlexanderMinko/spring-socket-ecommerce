package com.minko.socket.controller;

import com.minko.socket.entity.Category;
import com.minko.socket.entity.Producer;
import com.minko.socket.entity.Product;
import com.minko.socket.security.AccountDetailsServiceImpl;
import com.minko.socket.security.jwt.JwtEntryPoint;
import com.minko.socket.security.jwt.JwtProvider;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    private final static String baseUrl = "/api/products";

    @MockBean
    private ProductService productService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountDetailsServiceImpl accountDetailsService;

    @MockBean
    private JwtEntryPoint jwtEntryPoint;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProducts() throws Exception {
        Category category = new Category(1L, "book", 25);
        Producer producer = new Producer(1L ,"me");
        Product product1 = new Product(1L, "name1", "desc1", "url1", 1.11, category, producer);
        Product product2 = new Product(2L, "name2", "desc2", "url2", 1.11, category, producer);

        Mockito.when(productService.getProducts(anyInt(), anyInt())).thenReturn(new PageImpl<>(Arrays.asList(product1, product2)));

        mockMvc.perform(get(baseUrl + "?page=1&size=2"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content.size()", Matchers.is(2)))
                .andExpect(jsonPath("content[0].id", Matchers.is(1)))
                .andExpect(jsonPath("content[0].name", Matchers.is("name1")))
                .andExpect(jsonPath("content[1].id", Matchers.is(2)))
                .andExpect(jsonPath("content[1].name", Matchers.is("name2")));
    }

//    @Test
//    void getCategories() {
//
//    }
//
//    @Test
//    void getProductsByCategoryId() {
//    }
//
//    @Test
//    void getProductsByNameContaining() {
//    }
//
//    @Test
//    void getProductById() {
//    }
//
//    @Test
//    void getProductsSorterByPriceAsc() {
//    }
//
//    @Test
//    void getProductsSorterByPriceDesc() {
//    }
//
//    @Test
//    void getProductsByNameSorted() {
//    }
}