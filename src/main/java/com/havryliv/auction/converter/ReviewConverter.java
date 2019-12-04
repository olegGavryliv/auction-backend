package com.havryliv.auction.converter;

import com.havryliv.auction.dto.ReviewDTO;
import com.havryliv.auction.entity.Review;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class ReviewConverter {

    public ReviewDTO fromEntityToDTO(Review review){
        return ReviewDTO.builder()
                .id(review.getId())
                .reviewer( UserConverter.fromEntityToDTO(review.getReviewer()))
                .productId(review.getProduct().getId())
                .date(review.getTime())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }

    public Review fromDTOToEntity(ReviewDTO reviewDTO){
        return Review.builder()
                .id(reviewDTO.getId())
                .time(LocalDateTime.now())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .build();
    }
}
