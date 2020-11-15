package com.minko.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubReviewRequestDto {

    private String subReview;
    private String email;
    private Long reviewId;

}
