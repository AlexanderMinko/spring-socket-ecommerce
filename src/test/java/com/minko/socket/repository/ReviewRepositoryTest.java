package com.minko.socket.repository;

import com.minko.socket.entity.Account;
import com.minko.socket.entity.Product;
import com.minko.socket.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Product savedProduct;
    private Review savedReview;
    private Account savedAccount;

    @BeforeEach
    void setUp() {
        Product product = new Product(null, "name", "desc", "url", 12.12, null, null);
        Account account = new Account(null, "fname", "lname", "sabaka@sabaka.ua", "123",
                Instant.now(), true, "url", null);
        Review review = new Review(null, "review", Instant.now(), account, product);
        savedAccount = accountRepository.save(account);
        savedProduct = productRepository.save(product);
        savedReview = reviewRepository.save(review);
    }

    @Test
    void findByProductId() {
        List<Review> reviewsAct = reviewRepository.findByProductId(savedProduct.getId());
        assertThat(reviewsAct).isEqualTo(Collections.singletonList(savedReview));
    }

    @Test
    void findByAccountId() {
        List<Review> reviewsAct = reviewRepository.findByAccountId(savedAccount.getId());
        assertThat(reviewsAct).isEqualTo(Collections.singletonList(savedReview));
    }
}