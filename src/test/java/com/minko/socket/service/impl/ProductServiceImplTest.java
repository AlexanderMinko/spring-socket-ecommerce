package com.minko.socket.service.impl;

import com.minko.socket.entity.Category;
import com.minko.socket.entity.Producer;
import com.minko.socket.entity.Product;
import com.minko.socket.exception.SocketException;
import com.minko.socket.repository.CategoryRepository;
import com.minko.socket.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private int DUMMY_PAGE1 = 1;
    private int DUMMY_SIZE1 = 1;
    private int DUMMY_SIZE2 = 2;

    private Category category1;
    private Category category2;
    private Product product1;
    private Product product2;

    @BeforeEach
    void init() {
        category1 = new Category(1L, "book", 1);
        category2 = new Category(2L, "me", 2);
        Producer producer = new Producer(1L, "me");
        product1 = new Product(1L, "name1", "desc1", "url1", 1.11, category1, producer);
        product2 = new Product(2L, "name2", "desc2", "url2", 1.11, category2, producer);
    }

    @Test
    @DisplayName("Should return page of all products")
    void getProductsTest() {
        Page<Product> productsExp = new PageImpl<>(Arrays.asList(product1, product2));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productsExp);
        Page<Product> productsAct =  productService.getProducts(DUMMY_PAGE1, DUMMY_SIZE2);
        assertThat(productsExp).isEqualTo(productsAct);
    }

    @Test
    @DisplayName("Should return list of all categories")
    void getCategoriesTest() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
        List<Category> categories = productService.getCategories();
        assertThat(Arrays.asList(category1, category2)).isEqualTo(categories);
    }

    @Test
    @DisplayName("Should return page of products by category id")
    void getProductsByCategoryIdTest() {
        Page<Product> productsExp = new PageImpl<>(Collections.singletonList(product1));
        when(productRepository.findByCategoryId(anyLong(), any(PageRequest.class))).thenReturn(productsExp);
        Page<Product> productsAct =  productService.getProductsByCategoryId(1L, DUMMY_PAGE1, DUMMY_SIZE1);
        assertThat(productsExp).isEqualTo(productsAct);
    }

    @Test
    @DisplayName("Should return page of products by name containing search keyword")
    void getProductsByNameContainingTest() {
        Page<Product> productsExp = new PageImpl<>(Collections.singletonList(product1));
        when(productRepository.findByNameContaining(anyString(), any(Pageable.class))).thenReturn(productsExp);
        Page<Product> productsAct = productService.getProductsByNameContaining("name", DUMMY_PAGE1, DUMMY_SIZE1);
        assertThat(productsAct).isEqualTo(productsExp);

    }

    @Test
    @DisplayName("Should return single product by id or throw exception")
    void getProductByIdTest() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        Product product = productService.getProductById(1L);
        assertThat(product).isEqualTo(product1);
        assertThatThrownBy(() -> productService.getProductById(2L)).isInstanceOf(SocketException.class)
                .hasMessage("product not found with id - " + 2L);
    }

    @Test
    @DisplayName("Should return page of products sorted by price ascending")
    void getProductsSortedByPriceAscTest() {
        Page<Product> productsExp = new PageImpl<>(Arrays.asList(product1, product2));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productsExp);
        Page<Product> productsAct =  productService.getProductsSortedByPriceAsc(DUMMY_PAGE1, DUMMY_SIZE2);
        assertThat(productsAct).isEqualTo(productsExp);
    }

    @Test
    @DisplayName("Should return page of products sorted by price descending")
    void getProductsSortedByPriceDesc() {
        Page<Product> productsExp = new PageImpl<>(Arrays.asList(product1, product2));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productsExp);
        Page<Product> productsAct =  productService.getProductsSortedByPriceDesc(DUMMY_PAGE1, DUMMY_SIZE2);
        assertThat(productsAct).isEqualTo(productsExp);
    }

    @Test
    @DisplayName("Should return page of products sorted by name")
    void getProductsByNameSorted() {
        Page<Product> productsExp = new PageImpl<>(Arrays.asList(product1, product2));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productsExp);
        Page<Product> productsAct =  productService.getProductsByNameSorted(DUMMY_PAGE1, DUMMY_SIZE2);
        assertThat(productsAct).isEqualTo(productsExp);
    }
}