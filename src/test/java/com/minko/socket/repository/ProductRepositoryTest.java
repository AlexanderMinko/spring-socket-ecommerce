package com.minko.socket.repository;

import com.minko.socket.entity.Category;
import com.minko.socket.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private PageRequest pageRequest;
    private List<Product> products;
    private Product product;
    private Category savedCategory;

    @BeforeEach
    void setUp() {
        Category category = new Category(null, "name", 1);
        product = new Product(null, "name", "desc", "url", 12.12, category, null);
        pageRequest = PageRequest.of(0, 1);
        savedCategory = categoryRepository.save(category);
        Product savedProduct = productRepository.save(product);
        products = Collections.singletonList(savedProduct);
    }

    @Test
    void findByCategoryId() {
        List<Product> productsAct = productRepository.findByCategoryId(savedCategory.getId(), pageRequest).toList();
        assertThat(products).isEqualTo(productsAct);
    }

    @Test
    void findByNameContaining() {
        List<Product> productsAct = productRepository.findByNameContaining(product.getName(), pageRequest).toList();
        assertThat(products).isEqualTo(productsAct);
    }
}