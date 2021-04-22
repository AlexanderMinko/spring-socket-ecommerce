package com.minko.socket.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.dto.SubReviewRequestDto;
import com.minko.socket.dto.SubReviewResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Product;
import com.minko.socket.entity.Review;
import com.minko.socket.entity.SubReview;
import com.minko.socket.service.ReviewService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "account", source = "account")
    @Mapping(target = "product", source = "product")
    Review mapFromDto(ReviewRequestDto reviewRequestDto, Account account, Product product);

    @Mapping(target = "duration", expression = "java(getDuration(review))")
    @Mapping(target = "reviewerFirstName", expression = "java(review.getAccount().getFirstName())")
    @Mapping(target = "reviewerLastName", expression = "java(review.getAccount().getLastName())")
    @Mapping(target = "reviewerPhotoUrl", expression = "java(review.getAccount().getPhotoUrl())")
    ReviewResponseDto mapToDto(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "account", source = "account")
    @Mapping(target = "review", source = "review")
    SubReview mapFromDtoToSubReview(
            SubReviewRequestDto subReviewRequestDto, Account account, Review review);

    @Mapping(target = "duration", expression = "java(getDuration(subReview))")
    @Mapping(target = "reviewerFirstName", expression = "java(subReview.getAccount().getFirstName())")
    @Mapping(target = "reviewerLastName", expression = "java(subReview.getAccount().getLastName())")
    @Mapping(target = "reviewerPhotoUrl", expression = "java(subReview.getAccount().getPhotoUrl())")
    SubReviewResponseDto mapFromSubReviewToDto(SubReview subReview);

    default String getDuration(Review review) {
        return TimeAgo.using(review.getCreatedDate().toEpochMilli());
    }

    default String getDuration(SubReview subReview) {
        return TimeAgo.using(subReview.getCreatedDate().toEpochMilli());
    }

}
