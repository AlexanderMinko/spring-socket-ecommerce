package com.minko.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;
    private String review;
    private String duration;
    private String reviewerFirstName;
    private String reviewerLastName;
    private String reviewerPhotoUrl;
    private Integer countOfSubReview;
    private List<SubReviewResponseDto> subReviewResponseDtos;

}
