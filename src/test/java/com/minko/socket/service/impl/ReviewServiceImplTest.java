package com.minko.socket.service.impl;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.dto.SubReviewRequestDto;
import com.minko.socket.dto.SubReviewResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Product;
import com.minko.socket.entity.Review;
import com.minko.socket.entity.SubReview;
import com.minko.socket.exception.SocketException;
import com.minko.socket.mapper.ReviewMapper;
import com.minko.socket.repository.ReviewRepository;
import com.minko.socket.repository.SubReviewRepository;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

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

    @Captor
    private ArgumentCaptor<Review> reviewArgumentCaptor;

    private Review review;
    private SubReview subReview;
    private Account account;
    private Product product;
    private ReviewRequestDto reviewRequestDto;
    private SubReviewRequestDto subReviewRequestDto;
    private ReviewResponseDto reviewResponseDto;
    private SubReviewResponseDto subReviewResponseDto;

    @BeforeEach
    void setUp() {
        review = new Review(1L, "review", Instant.now(), null, null);
        subReview = new SubReview(1L, "subReview", Instant.now(), null, null);
        account = new Account(1L, "fname", "lname", "sabaka@saka.ua", "password",
                Instant.now(), true, "url",null);
        product = new Product(1L, "name", "desc", "url", 12.12, null, null);
        reviewRequestDto = new ReviewRequestDto("review", "lol@kek.ua", 1L);
        subReviewRequestDto = new SubReviewRequestDto("text", "sabaka@sabaka@.ua", 1L);
        reviewResponseDto = new ReviewResponseDto(1L, "text", "10", "fanme",
                "lname", "url", 1, null);
        subReviewResponseDto = new SubReviewResponseDto("text", "12", "fname",
                "lname", "url");
    }

    @Test
    @DisplayName("Should return one review by id or throw exception")
    void getReviewByIdTest() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        Review reviewAct = reviewService.getReviewById(1L);
        assertThat(reviewAct).isEqualTo(review);
        assertThatThrownBy(() -> reviewService.getReviewById(2L)).isInstanceOf(SocketException.class)
                .hasMessage("Review with not found with id - " + 2L);
    }

    @Test
    @DisplayName("Should created one review and return it")
    void createReviewTest() {
        when(accountService.getByEmail(anyString())).thenReturn(account);
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(reviewMapper.mapFromDto(any(ReviewRequestDto.class),
                any(Account.class), any(Product.class))).thenReturn(review);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review reviewAct = reviewService.createReview(reviewRequestDto);
        assertThat(reviewAct).isEqualTo(review);
    }

    @Test
    @DisplayName("Should return reviews by product id")
    void getAllReviewByProductIdTest() {
        when(reviewRepository.findByProductId(anyLong())).thenReturn(Collections.singletonList(review));
        when(reviewMapper.mapToDto(any(Review.class))).thenReturn(reviewResponseDto);
        when(subReviewRepository.findByReviewId(anyLong())).thenReturn(Collections.singletonList(subReview));
        when(reviewMapper.mapFromSubReviewToDto(any(SubReview.class))).thenReturn(subReviewResponseDto);
        when(subReviewRepository.countByReviewId(anyLong())).thenReturn(1);
        List<ReviewResponseDto> reviewResponseDtosAct = reviewService.getAllReviewByProductId(1L);
        assertThat(reviewResponseDtosAct).isEqualTo(Collections.singletonList(reviewResponseDto));
    }

    @Test
    @DisplayName("Should return hasMap of reviewResponseDtos and size of returned elements")
    void getAllReviewByEmailTest() {
        when(accountService.getByEmail(anyString())).thenReturn(account);
        when(reviewRepository.findByAccountId(anyLong())).thenReturn(Collections.singletonList(review));
        when(reviewMapper.mapToDto(any(Review.class))).thenReturn(reviewResponseDto);
        Map<String, Object> resultExp = new HashMap<>();
        resultExp.put("content", Collections.singletonList(reviewResponseDto));
        resultExp.put("totalElements", Collections.singletonList(review).size());
        Map<String, Object> resultAct = reviewService.getAllReviewByEmail("sabaka@saka.ua", 5);
        assertThat(resultAct).isEqualTo(resultExp);
    }

    @Test
    @DisplayName("Should created one subReview and return it")
    void createSubReviewTest() {
        when(accountService.getByEmail(anyString())).thenReturn(account);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(review));
        when(reviewMapper.mapFromDtoToSubReview(any(SubReviewRequestDto.class), any(Account.class), any(Review.class)))
                .thenReturn(subReview);
        when(subReviewRepository.save(any(SubReview.class))).thenReturn(subReview);
        SubReview subReviewAct = reviewService.createSubReview(subReviewRequestDto);
        assertThat(subReviewAct).isEqualTo(subReview);
    }

}