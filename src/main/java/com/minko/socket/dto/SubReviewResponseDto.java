package com.minko.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubReviewResponseDto {

    private String subReview;
    private String duration;
    private String reviewerFirstName;
    private String reviewerLastName;
    private String reviewerPhotoUrl;

}
