package com.minko.socket.service.impl;

import com.minko.socket.entity.Review;
import com.minko.socket.mapper.ReviewMapper;
import com.minko.socket.repository.ReviewRepository;
import com.minko.socket.repository.SubReviewRepository;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private ProductService productService;

    @Mock
    private SubReviewRepository subReviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review(1L, "review", Instant.now(), null, null);
    }

    @Test
    void getReviewById() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        Review reviewAct = reviewService.getReviewById(1L);
        assertThat(reviewAct).isEqualTo(review);
    }

    @Test
    void createReview() {
    }

    @Test
    void getAllReviewByProductId() {
    }

    @Test
    void getAllReviewByEmail() {
    }

    @Test
    void createSubReview() {
    }

    @Test
    void getAllSubReviewByReviewId() {
    }

    @Test
    void getCountOfSubReviewByReviewId() {
    }
}