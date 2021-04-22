package com.minko.socket.service;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.dto.SubReviewRequestDto;
import com.minko.socket.dto.SubReviewResponseDto;
import com.minko.socket.entity.Review;
import com.minko.socket.entity.SubReview;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    Review getReviewById(Long id);

    Review createReview(ReviewRequestDto reviewRequestDto);

    List<ReviewResponseDto> getAllReviewByProductId(Long id);

    Map<String, Object> getAllReviewByEmail(String email, Integer size);

    SubReview createSubReview(SubReviewRequestDto subReviewRequestDto);

}
