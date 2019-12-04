package com.havryliv.auction.service;

import com.havryliv.auction.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllByIdProduct(Long postId);

    ReviewDTO addReview(ReviewDTO reviewDTO, String username);
}
