package com.minko.socket.controller;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.dto.SubReviewRequestDto;
import com.minko.socket.dto.SubReviewResponseDto;
import com.minko.socket.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviewsByProductId(@PathVariable Long id) {
        return new ResponseEntity<>(reviewService.getAllReviewByProductId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.createReview(reviewRequestDto);
        return new ResponseEntity<>("Review successfully created!", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getReviewByAccountEmail(
            @RequestParam String email, @RequestParam Integer size) {
        return new ResponseEntity<>(reviewService.getAllReviewByEmail(email, size), HttpStatus.OK);
    }

    @PostMapping("/sub")
    public ResponseEntity<String> postSubReview(@RequestBody SubReviewRequestDto subReviewRequestDto) {
        reviewService.createSubReview(subReviewRequestDto);
        return new ResponseEntity<>("SubReview created!", HttpStatus.CREATED);
    }
}
