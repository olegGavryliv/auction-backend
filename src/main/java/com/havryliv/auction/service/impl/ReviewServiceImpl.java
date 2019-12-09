package com.havryliv.auction.service.impl;

import com.havryliv.auction.constants.ExceptionMessages;
import com.havryliv.auction.converter.ReviewConverter;
import com.havryliv.auction.dto.ReviewDTO;
import com.havryliv.auction.entity.Product;
import com.havryliv.auction.entity.Review;
import com.havryliv.auction.entity.User;
import com.havryliv.auction.exception.BusinessException;
import com.havryliv.auction.repository.ProductRepository;
import com.havryliv.auction.repository.ReviewRepository;
import com.havryliv.auction.repository.UserRepository;
import com.havryliv.auction.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;

    private ProductRepository productRepository;

    private UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReviewDTO> getAllByIdProduct(Long productId) {
        return reviewRepository.findAllByProductId(productId)
                .stream()
                .map(ReviewConverter::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO, String username) {
        Review review = ReviewConverter.fromDTOToEntity(reviewDTO);
        User dbUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ExceptionMessages.NOT_FOUND, new String[]{"username :" + username},
                        HttpStatus.NOT_FOUND));

        Product dbProduct = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new BusinessException(ExceptionMessages.NOT_FOUND, new String[]{"product id :" + reviewDTO.getProductId()},
                HttpStatus.NOT_FOUND));

        review.setProduct(dbProduct);
        review.setReviewer(dbUser);

        Review dbReview = reviewRepository.save(review);

        updateProductRating(dbProduct);

        return ReviewConverter.fromEntityToDTO(dbReview);
    }

    private void updateProductRating(Product dbProduct) {
        dbProduct.setRating(reviewRepository.getAverageRatingByProductId(dbProduct.getId()));
        productRepository.save(dbProduct);
    }


}
