package com.minko.socket.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Product;
import com.minko.socket.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "account", source = "account")
    @Mapping(target = "product", source = "product")
    public abstract Review mapFromDto(ReviewRequestDto reviewRequestDto, Account account, Product product);

    @Mapping(target = "duration", expression = "java(getDuration(review))")
    @Mapping(target = "reviewerFirstName", expression = "java(review.getAccount().getFirstName())")
    @Mapping(target = "reviewerLastName", expression = "java(review.getAccount().getLastName())")
    @Mapping(target = "reviewerPhotoUrl", expression = "java(review.getAccount().getPhotoUrl())")
    public abstract ReviewResponseDto mapToDto(Review review);

    String getDuration(Review review) {
        return TimeAgo.using(review.getCreatedDate().toEpochMilli());
    }
}
