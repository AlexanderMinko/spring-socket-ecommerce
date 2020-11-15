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
import com.minko.socket.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccountService accountService;
    private final ReviewMapper reviewMapper;
    private final ProductService productService;
    private final SubReviewRepository subReviewRepository;

    @Override
    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new SocketException("Review with not found with id - " + id));
    }

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
                .peek(el -> {
                    el.setSubReviewResponseDtos(this.getAllSubReviewByReviewId(el.getId()));
                    el.setCountOfSubReview(this.getCountOfSubReviewByReviewId(el.getId()));
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllReviewByEmail(String email, Integer size) {
        Account account = accountService.getByEmail(email);
        List<Review> foundedReviews = reviewRepository.findByAccountId(account.getId());
        List<ReviewResponseDto> reviewResponseDtos = foundedReviews
               .stream()
                .map(reviewMapper::mapToDto)
                .limit(size)
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("content", reviewResponseDtos);
        result.put("totalElements", foundedReviews.size());
        return result;
    }

    @Override
    @Transactional
    public void createSubReview(SubReviewRequestDto subReviewRequestDto) {
        Account account = accountService.getByEmail(subReviewRequestDto.getEmail());
        Review review = this.getReviewById(subReviewRequestDto.getReviewId());
        SubReview subReview = reviewMapper.mapFromDtoToSubReview(subReviewRequestDto, account, review);
        subReviewRepository.save(subReview);
    }

    @Override
    public List<SubReviewResponseDto> getAllSubReviewByReviewId(Long id) {
        return subReviewRepository.findByReviewId(id)
                .stream().map(reviewMapper::mapFromSubReviewToDto)
                .collect(Collectors.toList());
    }

    public Integer getCountOfSubReviewByReviewId(Long id) {
        return subReviewRepository.countByReviewId(id);
    }

}
