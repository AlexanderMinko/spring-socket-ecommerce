package com.minko.socket.service.impl;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Product;
import com.minko.socket.entity.Review;
import com.minko.socket.mapper.ReviewMapper;
import com.minko.socket.repository.ReviewRepository;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.ProductService;
import com.minko.socket.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccountService accountService;
    private final ReviewMapper reviewMapper;
    private final ProductService productService;

    @Override
    @Transactional
    public void createReview(ReviewRequestDto reviewRequestDto) {
        Account account = accountService.getByEmail(reviewRequestDto.getEmail());
        Product product = productService.getProductById(reviewRequestDto.getProductId());
        Review review = reviewMapper.mapFromDto(reviewRequestDto, account, product);
        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviewByProductId(Long id) {
        return reviewRepository.findByProductId(id)
                .stream().map(reviewMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviewByEmail(String email) {
        Account account = accountService.getByEmail(email);
        return reviewRepository.findByAccountId(account.getId())
                .stream().map(reviewMapper::mapToDto)
                .collect(Collectors.toList());
    }

}
