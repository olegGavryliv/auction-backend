package com.havryliv.auction.converter;

import com.havryliv.auction.dto.ProductDTO;
import com.havryliv.auction.entity.Product;
import com.havryliv.auction.entity.Review;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class ProductConverter {

    public Product fromDTOtoEntity(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .expireTime(LocalDate.ofEpochDay(1))
                .image(productDTO.getImage())
                .rating(productDTO.getRating())
                .build();
    }

    public ProductDTO fromEntityToDTO(Product product) {

        List<Review> reviews = product.getReviews();

        return ProductDTO.builder()
                .id(product.getId())
                .owner(UserConverter.fromEntityToDTO(product.getUser()))
                .image(product.getImage())
                .price(product.getPrice())
                .description(product.getDescription())
                .expireTime(product.getExpireTime())
                .name(product.getName())
                .rating(reviews != null ? reviews.stream().mapToDouble(Review::getRating).average().orElse(0) : 0)
                .reviews(product.getReviews() != null ? product.getReviews()
                        .stream()
                        .map(ReviewConverter::fromEntityToDTO)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();

    }
}

