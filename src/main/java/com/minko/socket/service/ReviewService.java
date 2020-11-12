package com.minko.socket.service;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    void createReview(ReviewRequestDto reviewRequestDto);

    List<ReviewResponseDto> getAllReviewByProductId(Long id);

    List<ReviewResponseDto> getAllReviewByEmail(String email);
}
