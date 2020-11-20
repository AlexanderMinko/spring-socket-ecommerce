package com.minko.socket.service;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.dto.SubReviewRequestDto;
import com.minko.socket.dto.SubReviewResponseDto;
import com.minko.socket.entity.Review;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    Review getReviewById(Long id);

    void createReview(ReviewRequestDto reviewRequestDto);

    List<ReviewResponseDto> getAllReviewByProductId(Long id);

    Map<String, Object> getAllReviewByEmail(String email, Integer size);

    void createSubReview(SubReviewRequestDto subReviewRequestDto);

    List<SubReviewResponseDto> getAllSubReviewByReviewId(Long id);

    Integer getCountOfSubReviewByReviewId(Long id);

}
